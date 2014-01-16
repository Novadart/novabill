package com.novadart.novabill.service;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.PostConstruct;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import com.novadart.novabill.domain.AccountingDocument;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.Logo;
import com.novadart.novabill.report.DocumentType;
import com.novadart.novabill.report.JRDataSourceFactory;
import com.novadart.novabill.report.JasperReportKeyResolutionException;
import com.novadart.novabill.report.JasperReportService;
import com.novadart.novabill.shared.client.data.DataExportClasses;

@Service
public class DataExporter {
	
	@Value("${path.tmpdir.data_export}")
	private String dataOutLocation;
	
	public static final String[] CLIENT_FIELDS = new String[]{"name", "address", "postcode", "city", "province", "country", "email", "phone",	"mobile", "fax", "web", "vatID", "ssn"};
	
	public static final String[] CLIENT_CONTACT_FIELDS = new String[]{"firstName", "lastName", "email", "phone", "fax", "mobile"};
	
	@Autowired
	private JasperReportService jrService;
	
	@PostConstruct
	protected void init(){
		File outDir = new File(dataOutLocation);
		if(!outDir.exists())
			outDir.mkdir();
	}
	
	private File exportClientData(File outDir, Set<Client> clients,
			ReloadableResourceBundleMessageSource messageSource, Locale locale) throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		File clientsData = File.createTempFile("clients", ".csv", outDir);
		clientsData.deleteOnExit();
		FileWriter fstream = new FileWriter(clientsData);
		BufferedWriter out = new BufferedWriter(fstream);
		String[] headers = new String[CLIENT_FIELDS.length + CLIENT_CONTACT_FIELDS.length];
		int i = 0, j = 0;
		for(; i < CLIENT_FIELDS.length; ++i)
			headers[i] = messageSource.getMessage("export." + CLIENT_FIELDS[i], null, CLIENT_FIELDS[i], locale);
		for(; j < CLIENT_CONTACT_FIELDS.length; ++j)
			headers[i + j] = messageSource.getMessage("export.contact." + CLIENT_CONTACT_FIELDS[j], null, CLIENT_CONTACT_FIELDS[j], locale);
		out.write(StringUtils.join(headers, "\t"));
		out.newLine();
		for(Client client: clients){
			List<String> clientVals = new ArrayList<String>(CLIENT_FIELDS.length + CLIENT_CONTACT_FIELDS.length);
			for(String clientField: CLIENT_FIELDS)
				clientVals.add(BeanUtils.getProperty(client, clientField));
			for(String clientContactField: CLIENT_CONTACT_FIELDS)
				clientVals.add(BeanUtils.getProperty(client.getContact(), clientContactField));
			out.write(StringUtils.join(clientVals, "\t"));
			out.newLine();
		}
		out.flush();
		out.close();
		return clientsData;
	}
	
	private <T extends AccountingDocument> File exportAccountingDocument(File outDir, T doc, Logo logo, Long businessID, DocumentType docType, Boolean putWatermark) throws IOException, JRException, JasperReportKeyResolutionException{
		File docFile = File.createTempFile("doc", ".pdf", outDir);
		docFile.deleteOnExit();
		jrService.exportReportToPdfFile(JRDataSourceFactory.createDataSource(doc, businessID), docType, doc.getLayoutType(), docFile.getPath());
		return docFile;
	}
	
	private <T extends AccountingDocument> List<File> exportAccountingDocumentsData(File outDir, ZipOutputStream zipStream, Collection<T> docs, Logo logo,
			DocumentType docType, Long businessID, Boolean putWatermark, String entryFormat) throws IOException, FileNotFoundException, JRException, JasperReportKeyResolutionException {
		List<File> files = new ArrayList<File>();
		for(T doc: docs){
			File docFile;
				docFile = exportAccountingDocument(outDir, doc, logo, businessID, docType, putWatermark);
			zipStream.putNextEntry(new ZipEntry(String.format(entryFormat, doc.getAccountingDocumentYear(), doc.getDocumentID())));
			FileInputStream invStream = new FileInputStream(docFile);
			IOUtils.copy(invStream, zipStream);
			invStream.close();
			files.add(docFile);
		}
		return files;
	}
	
	public File exportData(Set<DataExportClasses> classes, Business business, Logo logo,
			ReloadableResourceBundleMessageSource messageSource, Locale locale) throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, JRException, JasperReportKeyResolutionException{
		File outDir = new File(dataOutLocation);
		File zipFile = File.createTempFile("export", ".zip", outDir);
		ZipOutputStream zipStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
		File clientsData = null;
		List<File> invoicesFiles = null;
		List<File> estimationFiles = null;
		List<File> creditNoteFiles = null;
		List<File> transportDocsFiles = null;
		try {
			if(classes.contains(DataExportClasses.CLIENT)){
				clientsData = exportClientData(outDir, business.getClients(), messageSource, locale);
				zipStream.putNextEntry(new ZipEntry(messageSource.getMessage("export.clients.zipentry", null, "clients.csv", locale)));
				FileInputStream clientDataStream = new FileInputStream(clientsData);
				IOUtils.copy(clientDataStream, zipStream);
				clientDataStream.close();
			}
			boolean putWatermark = true;
			if(classes.contains(DataExportClasses.INVOICE))
				invoicesFiles = exportAccountingDocumentsData(outDir, zipStream, business.getInvoices(), logo, DocumentType.INVOICE, business.getId(), putWatermark,
						messageSource.getMessage("export.invoices.zipentry.pattern", null, "invoices/invoice_%d_%d.pdf", locale));
			if(classes.contains(DataExportClasses.ESTIMATION))
				estimationFiles = exportAccountingDocumentsData(outDir, zipStream, business.getEstimations(), logo, DocumentType.ESTIMATION, business.getId(), putWatermark,
						messageSource.getMessage("export.estimations.zipentry.pattern", null, "estimations/estimation_%d_%d.pdf", locale));
			if(classes.contains(DataExportClasses.CREDIT_NOTE))
				creditNoteFiles = exportAccountingDocumentsData(outDir, zipStream, business.getCreditNotes(), logo, DocumentType.CREDIT_NOTE, business.getId(), putWatermark,
						messageSource.getMessage("export.creditnotes.zipentry.pattern", null, "creditnotes/creditnotes_%d_%d.pdf", locale));
			if(classes.contains(DataExportClasses.TRANSPORT_DOCUMENT))
				transportDocsFiles = exportAccountingDocumentsData(outDir, zipStream, business.getTransportDocuments(), logo, DocumentType.TRANSPORT_DOCUMENT, business.getId(), putWatermark,
						messageSource.getMessage("export.transportdoc.zipentry.pattern", null, "transportdocs/transportdocs_%d_%d.pdf", locale));
				
			zipStream.close();
			return zipFile;
		} finally {
			if(clientsData != null)
				clientsData.delete();
			if(invoicesFiles != null){
				for(File file: invoicesFiles)
					file.delete();
			}
			if(estimationFiles != null){
				for(File file: estimationFiles)
					file.delete();
			}
			if(creditNoteFiles != null){
				for(File file: creditNoteFiles)
					file.delete();
			}
			if(transportDocsFiles != null){
				for(File file: transportDocsFiles)
					file.delete();
			}
		}
	}


}
