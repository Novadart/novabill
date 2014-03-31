package com.novadart.novabill.report;

import java.io.ByteArrayOutputStream;
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
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
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
		new JasperReportDescriptor("tidy.invoice", "classpath:/reports/tidy/tidyInvoice.jrxml", null),
		new JasperReportDescriptor("tidy.estimation", "classpath:/reports/tidy/tidyEstimation.jrxml", null),
		new JasperReportDescriptor("tidy.transdoc", "classpath:/reports/tidy/tidyTransDoc.jrxml", null),
		new JasperReportDescriptor("tidy.crednote", "classpath:/reports/tidy/tidyCredNote.jrxml", null),
		new JasperReportDescriptor("dense.invoice", "classpath:/reports/dense/denseInvoice.jrxml", null),
		new JasperReportDescriptor("dense.crednote", "classpath:/reports/dense/denseCredNote.jrxml", null),
		new JasperReportDescriptor("dense.estimation", "classpath:/reports/dense/denseEstimation.jrxml", null),
		new JasperReportDescriptor("dense.transdoc", "classpath:/reports/dense/denseTransDoc.jrxml", null),
		new JasperReportDescriptor("paymentsProspect", "classpath:/reports/dense/densePaymentsProspect.jrxml", null)
	};
	
	private static final Map<LayoutType, String> LAYOUT_RESOURCE_BUNDLES_BASE_NAMES = new HashMap<LayoutType, String>();
	{
		LAYOUT_RESOURCE_BUNDLES_BASE_NAMES.put(LayoutType.TIDY, "classpath:/reports/tidy/i18n/tidy.properties");
		LAYOUT_RESOURCE_BUNDLES_BASE_NAMES.put(LayoutType.DENSE, "classpath:/reports/dense/i18n/dense.properties");
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
		if(DocumentType.INVOICE.equals(docType)){
			if(LayoutType.TIDY.equals(layoutType))
				return "tidy.invoice";
			if(LayoutType.DENSE.equals(layoutType))
				return "dense.invoice";
		}
		if(DocumentType.ESTIMATION.equals(docType)){
			if(LayoutType.TIDY.equals(layoutType))
				return "tidy.estimation";
			if(LayoutType.DENSE.equals(layoutType))
				return "dense.estimation";
		}
		if(DocumentType.CREDIT_NOTE.equals(docType)){
			if(LayoutType.TIDY.equals(layoutType))
				return "tidy.crednote";
			if(LayoutType.DENSE.equals(layoutType))
				return "dense.crednote";
		}
		if(DocumentType.TRANSPORT_DOCUMENT.equals(docType)){
			if(LayoutType.TIDY.equals(layoutType))
				return "tidy.transdoc";
			if(LayoutType.DENSE.equals(layoutType))
				return "dense.transdoc";
		}
		if(DocumentType.PAYMENTS_PROSPECT.equals(docType))
			return "paymentsProspect";
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
	
	public byte[] exportReportToPdf(JRBeanCollectionDataSource dataSource, DocumentType docType, LayoutType layoutType, boolean embedPrint) throws JRException, JasperReportKeyResolutionException{
		if(embedPrint){
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT, createJasperPrint(dataSource, docType, layoutType));
			exporter.setParameter(JRPdfExporterParameter.PDF_JAVASCRIPT, "setTimeout(function(){ this.print({bUI: true,bSilent: false,bShrinkToFit: true}); }, 1000)");
			exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, out);
			exporter.exportReport();
			return out.toByteArray();
		}else
			return JasperExportManager.exportReportToPdf(createJasperPrint(dataSource, docType, layoutType));
	}
	
	public void exportReportToPdfFile(JRBeanCollectionDataSource dataSource, DocumentType docType, LayoutType layoutType, String destFileName) throws JRException, JasperReportKeyResolutionException{
		JasperExportManager.exportReportToPdfFile(createJasperPrint(dataSource, docType, layoutType), destFileName);
	}
	
}
