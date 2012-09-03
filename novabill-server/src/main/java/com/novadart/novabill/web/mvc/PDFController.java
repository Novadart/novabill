package com.novadart.novabill.web.mvc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novadart.novabill.domain.AccountingDocument;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.CreditNote;
import com.novadart.novabill.domain.Estimation;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.Logo;
import com.novadart.novabill.domain.TransportDocument;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.service.PDFGenerator;
import com.novadart.novabill.service.PDFGenerator.DocumentType;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.XsrfTokenService;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;

@Controller
@RequestMapping("/private/pdf")
public class PDFController{
	
	public static final String TOKENS_SESSION_FIELD = "pdf.generation.tokens";
	
	@Autowired
	private PDFGenerator pdfGenerator;
	
	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private XsrfTokenService xsrfTokenService;
	
	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping(method = RequestMethod.GET, value = "/invoices/{id}")
	@ResponseBody
	public void getInvoicePDF(@PathVariable Long id, @RequestParam(value = "token", required = false) String token,
			final HttpServletResponse response, HttpSession session, Locale locale) throws IOException, DataAccessException, NoSuchObjectException{
		if(token == null || !xsrfTokenService.verifyAndRemoveToken(token, session, TOKENS_SESSION_FIELD))
			return;
		final Invoice invoice = Invoice.findInvoice(id);
		if(invoice == null)
			throw new NoSuchObjectException();
		generatePDF(response, invoice, invoice.getBusiness(), PDFGenerator.DocumentType.INVOICE, locale);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/estimations/{id}")
	@ResponseBody
	public void getEstimationPDF(@PathVariable Long id, @RequestParam(value = "token", required = false) String token, 
			final HttpServletResponse response, HttpSession session, Locale locale) throws IOException, DataAccessException, NoSuchObjectException{
		if(token == null || !xsrfTokenService.verifyAndRemoveToken(token, session, TOKENS_SESSION_FIELD))
			return;
		final Estimation estimation = Estimation.findEstimation(id);
		if(estimation == null)
			throw new NoSuchObjectException();
		generatePDF(response, estimation, estimation.getBusiness(), PDFGenerator.DocumentType.ESTIMATION, locale);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/creditnotes/{id}")
	@ResponseBody
	public void getCreditNotePDF(@PathVariable Long id, @RequestParam(value = "token", required = false) String token, 
			final HttpServletResponse response, HttpSession session, Locale locale) throws IOException, DataAccessException, NoSuchObjectException{
		if(token == null || !xsrfTokenService.verifyAndRemoveToken(token, session, TOKENS_SESSION_FIELD))
			return;
		final CreditNote creditNote = CreditNote.findCreditNote(id);
		if(creditNote == null)
			throw new NoSuchObjectException();
		generatePDF(response, creditNote, creditNote.getBusiness(), PDFGenerator.DocumentType.CREDIT_NOTE, locale);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/transportdocs/{id}")
	@ResponseBody
	public void getTransportDocumentPDF(@PathVariable Long id, @RequestParam(value = "token", required = false) String token, 
			final HttpServletResponse response, HttpSession session, Locale locale) throws IOException, DataAccessException, NoSuchObjectException{
//		if(token == null || !xsrfTokenService.verifyAndRemoveToken(token, session, TOKENS_SESSION_FIELD))
//			return;
		final TransportDocument transportDocument = TransportDocument.findTransportDocument(id);
		if(transportDocument == null)
			throw new NoSuchObjectException();
		generatePDF(response, transportDocument, transportDocument.getBusiness(), PDFGenerator.DocumentType.TRANSPORT_DOCUMENT, locale);
	}
	
	private void generatePDF(final HttpServletResponse response, final AccountingDocument accountingDocument, Business invoiceOwner,
			final PDFGenerator.DocumentType docType, final Locale locale) throws DataAccessException,
			FileNotFoundException, RemoteException, IOException {
		Business business = Business.findBusiness(utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId());
		if(!business.getId().equals(invoiceOwner.getId()))
			throw new DataAccessException();
		File tempLogoFile = null;
		Logo logo = business.getLogo();
		try {
			if(logo != null) //business has logo
			{
				tempLogoFile = File.createTempFile("logo", "." + logo.getFormat().name());
				tempLogoFile.deleteOnExit();
				IOUtils.copy(new ByteArrayInputStream(logo.getData()), new FileOutputStream(tempLogoFile));
			}
			PDFGenerator.BeforeWriteEventHandler bwEvHnld = new PDFGenerator.BeforeWriteEventHandler() {
				@Override
				public void beforeWriteCallback(File file) {
					String fileNamePattern = null;
					if(docType.equals(DocumentType.INVOICE))
						fileNamePattern = messageSource.getMessage("export.invoices.name.pattern", null, "invoice_%d_%d.pdf", locale);
					else if(docType.equals(DocumentType.ESTIMATION))
						fileNamePattern = messageSource.getMessage("export.estimations.name.pattern", null, "estimation_%d_%d.pdf", locale);
					else if(docType.equals(DocumentType.CREDIT_NOTE))
						fileNamePattern = messageSource.getMessage("export.creditnotes.name.pattern", null, "creditnote_%d_%d.pdf", locale);
					else if(docType.equals(DocumentType.TRANSPORT_DOCUMENT))
						fileNamePattern = messageSource.getMessage("export.transportdocs.name.pattern", null, "transportdoc_%d_%d.pdf", locale);
					String fileName = String.format(fileNamePattern, accountingDocument.getAccountingDocumentYear(), accountingDocument.getDocumentID());
					response.setContentType("application/octet-stream");
					response.setHeader ("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
					response.setHeader ("Content-Length", String.valueOf(file.length()));
				}
			};
			if(tempLogoFile == null)
				pdfGenerator.createAndWrite(response.getOutputStream(), accountingDocument, null, null, null, docType, business.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_FREE),bwEvHnld);
			else
				pdfGenerator.createAndWrite(response.getOutputStream(), accountingDocument, tempLogoFile.getPath(), logo.getWidth(), logo.getHeight(), docType, business.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_FREE), bwEvHnld);
		} finally {
			if(tempLogoFile != null)
				tempLogoFile.delete();
		}
	}

}
