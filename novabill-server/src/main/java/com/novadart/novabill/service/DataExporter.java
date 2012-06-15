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

import com.healthmarketscience.rmiio.RemoteInputStreamClient;
import com.novadart.novabill.domain.AccountingDocument;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.service.PDFGenerator.DocumentType;
import com.novadart.novabill.shared.client.data.DataExportClasses;
import com.novadart.services.shared.ImageDTO;

@Service
public class DataExporter {
	
	@Value("${path.tmpdir.data_export}")
	private String dataOutLocation;
	
	public static final String[] CLIENT_FIELDS = new String[]{"name", "address", "postcode", "city", "province", "country", "email", "phone",	"mobile", "fax", "web", "vatID", "ssn"};
	
	@Autowired
	private PDFGenerator pdfGenerator;
	
	@SuppressWarnings("unused")
	@PostConstruct
	private void init(){
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
		String[] headers = new String[CLIENT_FIELDS.length];
		for(int i = 0; i < headers.length; ++i)
			headers[i] = messageSource.getMessage("export." + CLIENT_FIELDS[i], null, CLIENT_FIELDS[i], locale);
		out.write(StringUtils.join(headers, ","));
		out.newLine();
		for(Client client: clients){
			List<String> clientVals = new ArrayList<String>(CLIENT_FIELDS.length);
			for(String clientField: CLIENT_FIELDS)
				clientVals.add(BeanUtils.getProperty(client, clientField));
			out.write(StringUtils.join(clientVals, ","));
			out.newLine();
		}
		out.flush();
		out.close();
		return clientsData;
	}
	
	private File exportAccountingDocument(File outDir, AccountingDocument accountingDocument, String pathToLogo, Integer logoWidth,
			Integer logoHeight, DocumentType docType) throws IOException{
		File docFile = File.createTempFile("doc", ".pdf", outDir);
		docFile.deleteOnExit();
		pdfGenerator.createAndWrite(new FileOutputStream(docFile), accountingDocument, pathToLogo, logoWidth, logoHeight, docType, null);
		return docFile;
	}
	
	private List<File> exportAccountingDocumentsData(File outDir, ZipOutputStream zipStream, Business business, File tempLogoFile,
			ImageDTO logoDTO, DocumentType docType, String entryFormat) throws IOException, FileNotFoundException {
		List<File> files = new ArrayList<File>();
		Set<? extends AccountingDocument> docs = docType.equals(DocumentType.INVOICE)? business.getInvoices(): business.getEstimations();
		for(AccountingDocument doc: docs){
			File docFile;
			if(tempLogoFile != null)
				docFile = exportAccountingDocument(outDir, doc, tempLogoFile.getPath(), logoDTO.getWidth(), logoDTO.getHeight(), docType);
			else
				docFile = exportAccountingDocument(outDir, doc, null, null, null, docType);
			zipStream.putNextEntry(new ZipEntry(String.format(entryFormat, doc.getAccountingDocumentYear(), doc.getDocumentID())));
			FileInputStream invStream = new FileInputStream(docFile);
			IOUtils.copy(invStream, zipStream);
			invStream.close();
			files.add(docFile);
		}
		return files;
	}
	
	public File exportData(Set<DataExportClasses> classes, Business business, ImageDTO logoDTO,
			ReloadableResourceBundleMessageSource messageSource, Locale locale) throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		File outDir = new File(dataOutLocation);
		File zipFile = File.createTempFile("export", ".zip", outDir);
		ZipOutputStream zipStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
		File clientsData = null;
		File tempLogoFile = null;
		List<File> invoicesFiles = null;
		List<File> estimationFiles = null;
		try {
			if(classes.contains(DataExportClasses.CLIENT)){
				clientsData = exportClientData(outDir, business.getClients(), messageSource, locale);
				zipStream.putNextEntry(new ZipEntry(messageSource.getMessage("export.clients.zipentry", null, "clients.csv", locale)));
				FileInputStream clientDataStream = new FileInputStream(clientsData);
				IOUtils.copy(clientDataStream, zipStream);
				clientDataStream.close();
			}
			if(logoDTO != null){
				tempLogoFile = File.createTempFile("logo", "." + logoDTO.getFormat().name());
				tempLogoFile.deleteOnExit();
				IOUtils.copy(RemoteInputStreamClient.wrap(logoDTO.getRemoteImageDataInputStream()), new FileOutputStream(tempLogoFile));
			}
			if(classes.contains(DataExportClasses.INVOICE))
				invoicesFiles = exportAccountingDocumentsData(outDir, zipStream, business, tempLogoFile, logoDTO, DocumentType.INVOICE,
						messageSource.getMessage("export.invoices.zipentry.pattern", null, "invoices/invoice_%d_%d.pdf", locale));
			if(classes.contains(DataExportClasses.ESTIMATION))
				estimationFiles = exportAccountingDocumentsData(outDir, zipStream, business, tempLogoFile, logoDTO, DocumentType.ESTIMATION,
						messageSource.getMessage("export.estimations.zipentry.pattern", null, "estimations/estimation_%d_%d.pdf", locale));
			zipStream.close();
			return zipFile;
		} finally {
			if(clientsData != null)
				clientsData.delete();
			if(tempLogoFile != null)
				tempLogoFile.delete();
			if(invoicesFiles != null)
				for(File file: invoicesFiles)
					file.delete();
			if(estimationFiles != null){
				for(File file: estimationFiles)
					file.delete();
			}
		}
	}


}
