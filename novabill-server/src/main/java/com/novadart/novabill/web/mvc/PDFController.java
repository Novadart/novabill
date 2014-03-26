package com.novadart.novabill.web.mvc;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novadart.novabill.annotation.Xsrf;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.CreditNote;
import com.novadart.novabill.domain.Estimation;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.TransportDocument;
import com.novadart.novabill.report.DocumentType;
import com.novadart.novabill.report.JRDataSourceFactory;
import com.novadart.novabill.report.JasperReportKeyResolutionException;
import com.novadart.novabill.report.JasperReportService;
import com.novadart.novabill.report.ReportUtils;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.data.FilteringDateType;
import com.novadart.novabill.shared.client.data.LayoutType;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;

@Controller
@RequestMapping("/private/pdf")
public class PDFController{

	public static final String TOKEN_REQUEST_PARAM = "token";

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private JasperReportService jrService;

	@Autowired
	private UtilsService utilsService;
	

	@RequestMapping(method = RequestMethod.GET, value = "/invoices/{id}", produces = "application/pdf")
	//@Xsrf(tokenRequestParam = TOKEN_REQUEST_PARAM, tokensSessionField = XsrfTokenSessionFieldNames.PDF_GENERATION_TOKENS_SESSION_FIELD)
	@ResponseBody
	public byte[] getInvoicePDF(@PathVariable Long id, @RequestParam(value = "token", required = false) String token,
			@RequestParam(value = "print", required = false, defaultValue = "false") boolean print,
			HttpServletResponse response, Locale locale) throws IOException, DataAccessException, NoSuchObjectException, JasperReportKeyResolutionException, JRException{
		Invoice invoice = Invoice.findInvoice(id);
		if(invoice == null)
			throw new NoSuchObjectException();
		String pdfName = String.format(messageSource.getMessage("export.invoices.name.pattern", null, "invoice_%d_%d_%s.pdf", locale),  
				invoice.getAccountingDocumentYear(), invoice.getDocumentID(), ReportUtils.convertToASCII(invoice.getClient().getName()));
		response.setHeader("Content-Disposition", String.format("attachment; filename=%s", pdfName));
		return jrService.exportReportToPdf(JRDataSourceFactory.createDataSource(invoice, invoice.getBusiness().getId()),
				DocumentType.INVOICE, invoice.getLayoutType(), print);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/estimations/{id}", produces = "application/pdf")
	@ResponseBody
	@Xsrf(tokenRequestParam = TOKEN_REQUEST_PARAM, tokensSessionField = XsrfTokenSessionFieldNames.PDF_GENERATION_TOKENS_SESSION_FIELD)
	public byte[] getEstimationPDF(@PathVariable Long id, @RequestParam(value = "token", required = false) String token,
			@RequestParam(value = "print", required = false, defaultValue = "false") boolean print,
			HttpServletResponse response, Locale locale) throws IOException, DataAccessException, NoSuchObjectException, JRException, JasperReportKeyResolutionException{
		Estimation estimation = Estimation.findEstimation(id);
		if(estimation == null)
			throw new NoSuchObjectException();
		String pdfName = String.format(messageSource.getMessage("export.estimations.name.pattern", null, "estimation_%d_%d_%s.pdf", locale),
				estimation.getAccountingDocumentYear(), estimation.getDocumentID(), ReportUtils.convertToASCII(estimation.getClient().getName()));
		response.setHeader("Content-Disposition", String.format("attachment; filename=%s", pdfName));
		return jrService.exportReportToPdf(JRDataSourceFactory.createDataSource(estimation, estimation.getBusiness().getId()),
				DocumentType.ESTIMATION, estimation.getLayoutType(), print);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/creditnotes/{id}", produces = "application/pdf")
	@ResponseBody
	@Xsrf(tokenRequestParam = TOKEN_REQUEST_PARAM, tokensSessionField = XsrfTokenSessionFieldNames.PDF_GENERATION_TOKENS_SESSION_FIELD)
	public byte[] getCreditNotePDF(@PathVariable Long id, @RequestParam(value = "token", required = false) String token,
			@RequestParam(value = "print", required = false, defaultValue = "false") boolean print,
			HttpServletResponse response, Locale locale) throws IOException, DataAccessException, NoSuchObjectException, JRException, JasperReportKeyResolutionException{
		CreditNote creditNote = CreditNote.findCreditNote(id);
		if(creditNote == null)
			throw new NoSuchObjectException();
		String pdfName = String.format(messageSource.getMessage("export.creditnotes.name.pattern", null, "creditnote_%d_%d_%s.pdf", locale),
				creditNote.getAccountingDocumentYear(), creditNote.getDocumentID(), ReportUtils.convertToASCII(creditNote.getClient().getName()));
		response.setHeader("Content-Disposition", String.format("attachment; filename=%s", pdfName));
		return jrService.exportReportToPdf(JRDataSourceFactory.createDataSource(creditNote, creditNote.getBusiness().getId()),
				DocumentType.CREDIT_NOTE, creditNote.getLayoutType(), print);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/transportdocs/{id}", produces = "application/pdf")
	@ResponseBody
	@Xsrf(tokenRequestParam = TOKEN_REQUEST_PARAM, tokensSessionField = XsrfTokenSessionFieldNames.PDF_GENERATION_TOKENS_SESSION_FIELD)
	public byte[] getTransportDocumentPDF(@PathVariable Long id, @RequestParam(value = "token", required = false) String token,
			@RequestParam(value = "print", required = false, defaultValue = "false") boolean print,
			HttpServletResponse response, Locale locale) throws IOException, DataAccessException, NoSuchObjectException, JRException, JasperReportKeyResolutionException{
		TransportDocument transportDocument = TransportDocument.findTransportDocument(id);
		if(transportDocument == null)
			throw new NoSuchObjectException();
		String pdfName = String.format(messageSource.getMessage("export.transportdocs.name.pattern", null, "transportdoc_%d_%d_%s.pdf", locale),
				transportDocument.getAccountingDocumentYear(), transportDocument.getDocumentID(), ReportUtils.convertToASCII(transportDocument.getClient().getName()));
		response.setHeader("Content-Disposition", String.format("attachment; filename=%s", pdfName));
		return jrService.exportReportToPdf(JRDataSourceFactory.createDataSource(transportDocument, transportDocument.getBusiness().getId()),
				DocumentType.TRANSPORT_DOCUMENT, transportDocument.getLayoutType(), print);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/paymentspros", produces = "application/pdf")
	@ResponseBody
	@Xsrf(tokenRequestParam = TOKEN_REQUEST_PARAM, tokensSessionField = XsrfTokenSessionFieldNames.PDF_GENERATION_TOKENS_SESSION_FIELD)
	public byte[] getPaymentsProspectPaymentDueDatePDF(@RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = ISO.DATE) Date startDate,
			@RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = ISO.DATE) Date endDate,
			@RequestParam(value = "filteringDateType") FilteringDateType filteringDateType,
			@RequestParam(value = "token", required = false) String token,
			@RequestParam(value = "print", required = false, defaultValue = "false") boolean print,
			HttpServletResponse response, Locale locale) throws JRException, JasperReportKeyResolutionException {
		Business business  = Business.findBusiness(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId());
		List<Invoice> invoices = business.getAllUnpaidInvoicesInDateRange(filteringDateType, startDate, endDate);
		String pdfName = messageSource.getMessage("export.paymentspros.name.pattern", null, "Payments_prospect.pdf", locale);
		response.setHeader("Content-Disposition", String.format("attachment; filename=%s", pdfName));
		return jrService.exportReportToPdf(JRDataSourceFactory.createDataSource(invoices, startDate, endDate, filteringDateType),
				DocumentType.PAYMENTS_PROSPECT, LayoutType.DENSE, print);
	}
	
}
