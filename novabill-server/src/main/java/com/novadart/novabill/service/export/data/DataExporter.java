package com.novadart.novabill.service.export.data;

import com.google.common.base.Joiner;
import com.novadart.novabill.domain.AccountingDocument;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.Logo;
import com.novadart.novabill.report.DocumentType;
import com.novadart.novabill.report.ReportUtils;
import com.novadart.novabill.service.PDFStorageService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.novadart.novabill.service.PDFStorageService.getStoredFileExtension;
import static com.novadart.novabill.service.PDFStorageService.handleFile;

@Service
public class DataExporter {
	
	@Value("${path.tmpdir.data_export}")
	private String dataOutLocation;

	@Autowired
	private PDFStorageService documentStorageService;

	public static final String[] CLIENT_FIELDS = new String[]{"name", "address", "postcode", "city", "province", "country", "email", "phone",	"mobile", "fax", "web", "vatID", "ssn"};
	
	public static final String[] CLIENT_CONTACT_FIELDS = new String[]{"firstName", "lastName", "email", "phone", "fax", "mobile"};
	
	@PostConstruct
	protected void init(){
		File outDir = new File(dataOutLocation);
		if(!outDir.exists())
			outDir.mkdir();
	}
	
	private File exportClientData(File outDir, Set<Client> clients,
			MessageSource messageSource, Locale locale) throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
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
		Joiner joiner = Joiner.on(";");
		out.write(joiner.join(headers));
		out.newLine();
		for(Client client: clients){
			List<String> clientVals = new ArrayList<String>(CLIENT_FIELDS.length + CLIENT_CONTACT_FIELDS.length);
			for(String clientField: CLIENT_FIELDS)
				clientVals.add(BeanUtils.getProperty(client, clientField));
			for(String clientContactField: CLIENT_CONTACT_FIELDS)
				clientVals.add(BeanUtils.getProperty(client.getContact(), clientContactField));
			out.write(joiner.join(clientVals));
			out.newLine();
		}
		out.flush();
		out.close();
		return clientsData;
	}
	
	private <T extends AccountingDocument> File exportAccountingDocument(File outDir, T doc, Logo logo, Long businessID, DocumentType docType, Boolean putWatermark) throws IOException {
		File docFile = File.createTempFile("doc", getStoredFileExtension(doc.getDocumentPath()), outDir);
		docFile.deleteOnExit();
		handleFile(doc.getDocumentPath(), new PDFStorageService.Handler<Void>() {
			@Override
			public Void handle(InputStream inputStream) {
				try {
					Files.copy(inputStream, docFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				return null;
			}
		});
		return docFile;
	}
	
	private <T extends AccountingDocument> List<File> exportAccountingDocumentsData(File outDir, ZipOutputStream zipStream, Collection<T> docs, Logo logo,
			DocumentType docType, Long businessID, Boolean putWatermark, String entryFormat) throws IOException, FileNotFoundException {
		List<File> files = new ArrayList<File>();
		for(T doc: docs){
			File docFile;
				docFile = exportAccountingDocument(outDir, doc, logo, businessID, docType, putWatermark);
			String clientName = doc.getClient().getName();
			zipStream.putNextEntry(new ZipEntry(String.format(entryFormat, doc.getAccountingDocumentYear(), doc.getDocumentID(), ReportUtils.convertToASCII(clientName))));
			FileInputStream invStream = new FileInputStream(docFile);
			IOUtils.copy(invStream, zipStream);
			invStream.close();
			files.add(docFile);
		}
		return files;
	}
	
	public File exportData(ExportDataBundle exportDataBundle, MessageSource messageSource,
			Locale locale) throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		File outDir = new File(dataOutLocation);
		File zipFile = File.createTempFile("export", ".zip", outDir);
		ZipOutputStream zipStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
		File clientsData = null;
		List<File> invoicesFiles = null;
		List<File> estimationFiles = null;
		List<File> creditNoteFiles = null;
		List<File> transportDocsFiles = null;
		try {
			if(exportDataBundle.getClients() != null){
				clientsData = exportClientData(outDir, exportDataBundle.getClients(), messageSource, locale);
				zipStream.putNextEntry(new ZipEntry(messageSource.getMessage("export.clients.zipentry", null, "clients.csv", locale)));
				FileInputStream clientDataStream = new FileInputStream(clientsData);
				IOUtils.copy(clientDataStream, zipStream);
				clientDataStream.close();
			}
			boolean putWatermark = true;
			Long businessID = exportDataBundle.getBusiness().getId();
			Logo logo = exportDataBundle.getLogo();
			if(exportDataBundle.getInvoices() != null)
				invoicesFiles = exportAccountingDocumentsData(outDir, zipStream, exportDataBundle.getInvoices(), logo, DocumentType.INVOICE, businessID, putWatermark,
						messageSource.getMessage("export.invoices.zipentry.pattern", null, "invoices/invoice_%d_%d_%s.pdf", locale));
			if(exportDataBundle.getEstimations() != null)
				estimationFiles = exportAccountingDocumentsData(outDir, zipStream, exportDataBundle.getEstimations(), logo, DocumentType.ESTIMATION, businessID, putWatermark,
						messageSource.getMessage("export.estimations.zipentry.pattern", null, "estimations/estimation_%d_%d_%s.pdf", locale));
			if(exportDataBundle.getCreditNotes() != null)
				creditNoteFiles = exportAccountingDocumentsData(outDir, zipStream, exportDataBundle.getCreditNotes(), logo, DocumentType.CREDIT_NOTE, businessID, putWatermark,
						messageSource.getMessage("export.creditnotes.zipentry.pattern", null, "creditnotes/creditnotes_%d_%d_%s.pdf", locale));
			if(exportDataBundle.getTransportDocuments() != null)
				transportDocsFiles = exportAccountingDocumentsData(outDir, zipStream, exportDataBundle.getTransportDocuments(), logo, DocumentType.TRANSPORT_DOCUMENT, businessID, putWatermark,
						messageSource.getMessage("export.transportdoc.zipentry.pattern", null, "transportdocs/transportdocs_%d_%d_%s.pdf", locale));
				
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
