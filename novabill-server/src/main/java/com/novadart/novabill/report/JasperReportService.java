package com.novadart.novabill.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;
import javax.annotation.PostConstruct;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.novadart.novabill.shared.client.data.LayoutType;


@Service
public class JasperReportService implements ResourceLoaderAware{
	
	private ResourceLoader resourceLoader;
	
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JasperReportService.class);
	
	private static final JasperReportDescriptor[] REPORT_DESCRIPTORS = new JasperReportDescriptor[]{
		new JasperReportDescriptor("tidy.invoice", "/WEB-INF/reports/tidy/tidyInvoice.jrxml", null),
		new JasperReportDescriptor("tidy.estimation", "/WEB-INF/reports/tidy/tidyEstimation.jrxml", null),
		new JasperReportDescriptor("tidy.transdoc", "/WEB-INF/reports/tidy/tidyTransDoc.jrxml", null),
		new JasperReportDescriptor("tidy.crednote", "/WEB-INF/reports/tidy/tidyCredNote.jrxml", null)
	};
	
	private static final Map<LayoutType, String> LAYOUT_RESOURCE_BUNDLES_BASE_NAMES = new HashMap<LayoutType, String>();
	{
		LAYOUT_RESOURCE_BUNDLES_BASE_NAMES.put(LayoutType.TIDY, "/WEB-INF/i18n/tidy.properties");
	}
	
	private Map<String, JasperReport> compiledJasperReports;
	
	private Map<String, List<String>> subreportsMapping;
	
	private Map<LayoutType, ResourceBundle> resourceBundles;
	
	private JasperReport compileReport(JasperReportDescriptor jrDesc) throws JRException, IOException{
		LOGGER.info(String.format("Compiling jaspert report with key: %s with path: %s", jrDesc.getKey(), jrDesc.getPath()));
		Resource resource = resourceLoader.getResource(jrDesc.getPath());
		JasperDesign design = JRXmlLoader.load(resource.getInputStream());
		return JasperCompileManager.compileReport(design);
	}
	
	private void compileJasperReports() throws JRException, IOException{
		compiledJasperReports = new HashMap<String, JasperReport>();
		Deque<JasperReportDescriptor> compilationQueue = new LinkedList<JasperReportDescriptor>();
		for(JasperReportDescriptor jsDesc: REPORT_DESCRIPTORS)
			compilationQueue.addLast(jsDesc);
		while(!compilationQueue.isEmpty()){
			JasperReportDescriptor head = compilationQueue.pollFirst();
			if(!compiledJasperReports.containsKey(head.getKey()))
				compiledJasperReports.put(head.getKey(), compileReport(head));
			for(JasperReportDescriptor child: head.getFirstLevelSubreportDescs())
				compilationQueue.addLast(child);
		}
	}
	
	private void loadResourceBundles() throws IOException {
		resourceBundles = new HashMap<LayoutType, ResourceBundle>();
		for(LayoutType layoutType: LayoutType.values()){
			String base = LAYOUT_RESOURCE_BUNDLES_BASE_NAMES.get(layoutType);
			LOGGER.info(String.format("Loading resource bundle %s", base));
			Resource resource = resourceLoader.getResource(base);
			resourceBundles.put(layoutType, new PropertyResourceBundle(resource.getInputStream()));
		}
	}
	
	@PostConstruct
	public synchronized void initialize() throws JRException, IOException{
		compileJasperReports();
		subreportsMapping = new HashMap<String, List<String>>();
		for(JasperReportDescriptor jrDesc: REPORT_DESCRIPTORS){
			Set<JasperReportDescriptor> subreportDescs = jrDesc.getSubreportDescs();
			List<String> subreportKeys = new ArrayList<String>(subreportDescs.size());
			for(JasperReportDescriptor jsrDesc: subreportDescs)
				subreportKeys.add(jsrDesc.getKey());
			subreportsMapping.put(jrDesc.getKey(), subreportKeys);
		}
		loadResourceBundles();
	}

	private String resolveJasperReportKey(DocumentType docType, LayoutType layoutType) throws JasperReportKeyResolutionException{
		if(docType.equals(DocumentType.INVOICE)){
			if(layoutType.equals(LayoutType.TIDY))
				return "tidy.invoice";
		}
		if(docType.equals(DocumentType.ESTIMATION)){
			if(layoutType.equals(LayoutType.TIDY))
				return "tidy.estimation";
		}
		if(docType.equals(DocumentType.CREDIT_NOTE)){
			if(layoutType.equals(LayoutType.TIDY))
				return "tidy.crednote";
		}
		if(docType.equals(DocumentType.TRANSPORT_DOCUMENT)){
			if(layoutType.equals(LayoutType.TIDY))
				return "tidy.transdoc";
		}
		throw new JasperReportKeyResolutionException(String.format("View could not be resolved; document type: %s, layout type: %s", docType.toString(), layoutType.toString()));
	}
	
	private JasperPrint createJasperPrint(JRBeanCollectionDataSource dataSource, DocumentType docType, LayoutType layoutType) throws JasperReportKeyResolutionException, JRException{
		String key = resolveJasperReportKey(docType, layoutType);
		JasperReport jasperReport = compiledJasperReports.get(key);
		Map<String, Object> reportParameters = new HashMap<String, Object>();
		for(String subreportKey: subreportsMapping.get(key))
			reportParameters.put(subreportKey, compiledJasperReports.get(subreportKey));
		reportParameters.put(JRParameter.REPORT_RESOURCE_BUNDLE, resourceBundles.get(layoutType));
		return JasperFillManager.fillReport(jasperReport, reportParameters, dataSource);
	}
	
	public byte[] exportReportToPdf(JRBeanCollectionDataSource dataSource, DocumentType docType, LayoutType layoutType) throws JRException, JasperReportKeyResolutionException{
		return JasperExportManager.exportReportToPdf(createJasperPrint(dataSource, docType, layoutType));
	}
	
}
