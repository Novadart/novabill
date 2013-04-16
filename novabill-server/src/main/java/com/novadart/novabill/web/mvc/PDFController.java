package com.novadart.novabill.web.mvc;

import java.io.IOException;
import java.util.Locale;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.novadart.novabill.annotation.Xsrf;
import com.novadart.novabill.domain.CreditNote;
import com.novadart.novabill.domain.Estimation;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.TransportDocument;
import com.novadart.novabill.report.AccountingDocumentJRDataSource;
import com.novadart.novabill.report.DocumentType;
import com.novadart.novabill.report.JasperReportService;
import com.novadart.novabill.report.JasperReportKeyResolutionException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;

@Controller
@RequestMapping("/private/pdf")
public class PDFController{
	
	public static final String TOKENS_SESSION_FIELD = "pdf.generation.tokens";
	public static final String TOKEN_REQUEST_PARAM = "token";
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private JasperReportService jrService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/invoices/{id}", produces = "application/pdf")
	@Xsrf(tokenRequestParam = TOKEN_REQUEST_PARAM, tokensSessionField = TOKENS_SESSION_FIELD)
	@ResponseBody
	public byte[] getInvoicePDF(@PathVariable Long id, @RequestParam(value = "token", required = false) String token,
			HttpServletResponse response, Locale locale) throws IOException, DataAccessException, NoSuchObjectException, JasperReportKeyResolutionException, JRException{
		Invoice invoice = Invoice.findInvoice(id);
		if(invoice == null)
			throw new NoSuchObjectException();
		String pdfName = String.format(messageSource.getMessage("export.invoices.name.pattern", null, "invoice_%d_%d.pdf", locale), invoice.getAccountingDocumentYear(), invoice.getDocumentID());
		response.setHeader("Content-Disposition", String.format("attachment; filename=%s", pdfName));
		return jrService.exportReportToPdf(new AccountingDocumentJRDataSource<Invoice>(invoice).getDataSource(), DocumentType.INVOICE, invoice.getLayoutType());
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/estimations/{id}", produces = "application/pdf")
	@ResponseBody
	@Xsrf(tokenRequestParam = TOKEN_REQUEST_PARAM, tokensSessionField = TOKENS_SESSION_FIELD)
	public byte[] getEstimationPDF(@PathVariable Long id, @RequestParam(value = "token", required = false) String token, 
			HttpServletResponse response, Locale locale) throws IOException, DataAccessException, NoSuchObjectException, JRException, JasperReportKeyResolutionException{
		Estimation estimation = Estimation.findEstimation(id);
		if(estimation == null)
			throw new NoSuchObjectException();
		String pdfName = String.format(messageSource.getMessage("export.estimations.name.pattern", null, "estimation_%d_%d.pdf", locale), estimation.getAccountingDocumentYear(), estimation.getDocumentID());
		response.setHeader("Content-Disposition", String.format("attachment; filename=%s", pdfName));
		return jrService.exportReportToPdf(new AccountingDocumentJRDataSource<Estimation>(estimation).getDataSource(), DocumentType.ESTIMATION, estimation.getLayoutType());
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/creditnotes/{id}", produces = "application/pdf")
	@ResponseBody
	@Xsrf(tokenRequestParam = TOKEN_REQUEST_PARAM, tokensSessionField = TOKENS_SESSION_FIELD)
	public byte[] getCreditNotePDF(@PathVariable Long id, @RequestParam(value = "token", required = false) String token, 
			HttpServletResponse response, Locale locale) throws IOException, DataAccessException, NoSuchObjectException, JRException, JasperReportKeyResolutionException{
		CreditNote creditNote = CreditNote.findCreditNote(id);
		if(creditNote == null)
			throw new NoSuchObjectException();
		String pdfName = String.format(messageSource.getMessage("export.creditnotes.name.pattern", null, "creditnote_%d_%d.pdf", locale), creditNote.getAccountingDocumentYear(), creditNote.getDocumentID());
		response.setHeader("Content-Disposition", String.format("attachment; filename=%s", pdfName));
		return jrService.exportReportToPdf(new AccountingDocumentJRDataSource<CreditNote>(creditNote).getDataSource(), DocumentType.CREDIT_NOTE, creditNote.getLayoutType());
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/transportdocs/{id}", produces = "application/pdf")
	@ResponseBody
	@Xsrf(tokenRequestParam = TOKEN_REQUEST_PARAM, tokensSessionField = TOKENS_SESSION_FIELD)
	public byte[] getTransportDocumentPDF(@PathVariable Long id, @RequestParam(value = "token", required = false) String token, 
			HttpServletResponse response, Locale locale) throws IOException, DataAccessException, NoSuchObjectException, JRException, JasperReportKeyResolutionException{
		TransportDocument transportDocument = TransportDocument.findTransportDocument(id);
		if(transportDocument == null)
			throw new NoSuchObjectException();
		String pdfName = String.format(messageSource.getMessage("export.transportdocs.name.pattern", null, "transportdoc_%d_%d.pdf", locale), transportDocument.getAccountingDocumentYear(), transportDocument.getDocumentID());
		response.setHeader("Content-Disposition", String.format("attachment; filename=%s", pdfName));
		return jrService.exportReportToPdf(new AccountingDocumentJRDataSource<TransportDocument>(transportDocument).getDataSource(), DocumentType.TRANSPORT_DOCUMENT, transportDocument.getLayoutType());
	}

}
