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
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.PostConstruct;
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
import com.novadart.novabill.service.PDFGenerator.DocumentType;
import com.novadart.novabill.shared.client.data.DataExportClasses;

@Service
public class DataExporter {
	
	@Value("${path.tmpdir.data_export}")
	private String dataOutLocation;
	
	public static final String[] CLIENT_FIELDS = new String[]{"name", "address", "postcode", "city", "province", "country", "email", "phone",	"mobile", "fax", "web", "vatID", "ssn"};
	
	public static final String[] CLIENT_CONTACT_FIELDS = new String[]{"firstName", "lastName", "email", "phone", "fax", "mobile"};
	
	@Autowired
	private PDFGenerator pdfGenerator;
	
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
		out.write(StringUtils.join(headers, ","));
		out.newLine();
		for(Client client: clients){
			List<String> clientVals = new ArrayList<String>(CLIENT_FIELDS.length + CLIENT_CONTACT_FIELDS.length);
			for(String clientField: CLIENT_FIELDS)
				clientVals.add(BeanUtils.getProperty(client, clientField));
			for(String clientContactField: CLIENT_CONTACT_FIELDS)
				clientVals.add(BeanUtils.getProperty(client.getContact(), clientContactField));
			out.write(StringUtils.join(clientVals, ","));
			out.newLine();
		}
		out.flush();
		out.close();
		return clientsData;
	}
	
	private File exportAccountingDocument(File outDir, AccountingDocument accountingDocument, Logo logo, DocumentType docType, Boolean putWatermark) throws IOException{
		File docFile = File.createTempFile("doc", ".pdf", outDir);
		docFile.deleteOnExit();
		pdfGenerator.createAndWrite(new FileOutputStream(docFile), accountingDocument, logo, docType, putWatermark, null);
		return docFile;
	}
	
	private List<File> exportAccountingDocumentsData(File outDir, ZipOutputStream zipStream, Business business, Logo logo,
			DocumentType docType, Boolean putWatermark, String entryFormat) throws IOException, FileNotFoundException {
		List<File> files = new ArrayList<File>();
		Set<? extends AccountingDocument> docs = new HashSet<AccountingDocument>();
		if(docType.equals(DocumentType.INVOICE))
			docs = business.getInvoices();
		else if(docType.equals(DocumentType.ESTIMATION))
			docs = business.getEstimations();
		else if(docType.equals(DocumentType.CREDIT_NOTE))
			docs = business.getCreditNotes();
		else if(docType.equals(DocumentType.TRANSPORT_DOCUMENT))
			docs = business.getTransportDocuments();
		for(AccountingDocument doc: docs){
			File docFile;
				docFile = exportAccountingDocument(outDir, doc, logo, docType, putWatermark);
			zipStream.putNextEntry(new ZipEntry(String.format(entryFormat, doc.getAccountingDocumentYear(), doc.getDocumentID())));
			FileInputStream invStream = new FileInputStream(docFile);
			IOUtils.copy(invStream, zipStream);
			invStream.close();
			files.add(docFile);
		}
		return files;
	}
	
	public File exportData(Set<DataExportClasses> classes, Business business, Logo logo,
			ReloadableResourceBundleMessageSource messageSource, Locale locale) throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
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
				invoicesFiles = exportAccountingDocumentsData(outDir, zipStream, business, logo, DocumentType.INVOICE, putWatermark,
						messageSource.getMessage("export.invoices.zipentry.pattern", null, "invoices/invoice_%d_%d.pdf", locale));
			if(classes.contains(DataExportClasses.ESTIMATION))
				estimationFiles = exportAccountingDocumentsData(outDir, zipStream, business, logo, DocumentType.ESTIMATION, putWatermark,
						messageSource.getMessage("export.estimations.zipentry.pattern", null, "estimations/estimation_%d_%d.pdf", locale));
			if(classes.contains(DataExportClasses.CREDIT_NOTE))
				creditNoteFiles = exportAccountingDocumentsData(outDir, zipStream, business, logo, DocumentType.CREDIT_NOTE, putWatermark,
						messageSource.getMessage("export.creditnotes.zipentry.pattern", null, "creditnotes/creditnotes_%d_%d.pdf", locale));
			if(classes.contains(DataExportClasses.TRANSPORT_DOCUMENT))
				transportDocsFiles = exportAccountingDocumentsData(outDir, zipStream, business, logo, DocumentType.TRANSPORT_DOCUMENT, putWatermark,
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
