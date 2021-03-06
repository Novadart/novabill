package com.novadart.novabill.web.mvc;

import com.novadart.novabill.domain.*;
import com.novadart.novabill.report.DocumentType;
import com.novadart.novabill.report.JRDataSourceFactory;
import com.novadart.novabill.report.JasperReportService;
import com.novadart.novabill.report.ReportUtils;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.data.FilteringDateType;
import com.novadart.novabill.shared.client.data.LayoutType;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.novadart.novabill.service.PDFStorageService.pdfFileToByteArray;

public class PDFController{

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private JasperReportService jrService;

	@Autowired
	private UtilsService utilsService;

	protected ResponseEntity<byte[]> getInvoicePDF(Long id, String token, boolean print,
			HttpServletResponse response, Locale locale) throws IOException, DataAccessException, NoSuchObjectException{
		Invoice invoice = Invoice.findInvoice(id);
		if(invoice == null)
			throw new NoSuchObjectException();
		String pdfName = ReportUtils.cutFileName( String.format(messageSource.getMessage("export.invoices.name.pattern", null, "invoice_%d_%s_%s.pdf", locale),
				invoice.getAccountingDocumentYear(), invoice.getExpandedDocumentId(), ReportUtils.convertToASCII(invoice.getClient().getName())) );
		response.setHeader("Content-Disposition", String.format("%s; filename=%s", print? "inline": "attachment", pdfName));
		return new ResponseEntity<>(pdfFileToByteArray(invoice.getDocumentPath()), HttpStatus.OK);
	}

	protected ResponseEntity<byte[]> getEstimationPDF(Long id, String token, boolean print,
			HttpServletResponse response, Locale locale) throws IOException, DataAccessException, NoSuchObjectException {
		Estimation estimation = Estimation.findEstimation(id);
		if(estimation == null)
			throw new NoSuchObjectException();
		String pdfName = ReportUtils.cutFileName( String.format(messageSource.getMessage("export.estimations.name.pattern", null, "estimation_%d_%s_%s.pdf", locale),
				estimation.getAccountingDocumentYear(), estimation.getExpandedDocumentId(), ReportUtils.convertToASCII(estimation.getClient().getName())) );
		response.setHeader("Content-Disposition", String.format("%s; filename=%s", print? "inline": "attachment", pdfName));
		return new ResponseEntity<>(pdfFileToByteArray(estimation.getDocumentPath()), HttpStatus.OK);
	}

	protected ResponseEntity<byte[]> getCreditNotePDF(Long id, String token, boolean print,
			HttpServletResponse response, Locale locale) throws IOException, DataAccessException, NoSuchObjectException {
		CreditNote creditNote = CreditNote.findCreditNote(id);
		if(creditNote == null)
			throw new NoSuchObjectException();
		String pdfName = ReportUtils.cutFileName( String.format(messageSource.getMessage("export.creditnotes.name.pattern", null, "creditnote_%d_%s_%s.pdf", locale),
				creditNote.getAccountingDocumentYear(), creditNote.getExpandedDocumentId(), ReportUtils.convertToASCII(creditNote.getClient().getName())) );
		response.setHeader("Content-Disposition", String.format("%s; filename=%s", print? "inline": "attachment", pdfName));
		return new ResponseEntity<>(pdfFileToByteArray(creditNote.getDocumentPath()), HttpStatus.OK);
	}

	protected ResponseEntity<byte[]> getTransportDocumentPDF(Long id, String token, boolean print,
			HttpServletResponse response, Locale locale) throws IOException, DataAccessException, NoSuchObjectException {
		TransportDocument transportDocument = TransportDocument.findTransportDocument(id);
		if(transportDocument == null)
			throw new NoSuchObjectException();
		String pdfName = ReportUtils.cutFileName( String.format(messageSource.getMessage("export.transportdocs.name.pattern", null, "transportdoc_%d_%s_%s.pdf", locale),
				transportDocument.getAccountingDocumentYear(), transportDocument.getExpandedDocumentId(), ReportUtils.convertToASCII(transportDocument.getClient().getName())) );
		response.setHeader("Content-Disposition", String.format("%s; filename=%s", print? "inline": "attachment", pdfName));
		return new ResponseEntity<>(pdfFileToByteArray(transportDocument.getDocumentPath()), HttpStatus.OK);
	}

	protected ResponseEntity<byte[]> getPaymentsProspectPaymentDueDatePDF(Date startDate, Date endDate, FilteringDateType filteringDateType, String token,
			boolean print, HttpServletResponse response, Locale locale) throws FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException {
		Business business  = Business.findBusiness(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId());
		List<Invoice> invoices = business.getAllUnpaidInvoicesInDateRange(filteringDateType, startDate, endDate);
		String pdfName = messageSource.getMessage("export.paymentspros.name.pattern", null, "Payments_prospect.pdf", locale);
		response.setHeader("Content-Disposition", String.format("%s; filename=%s", print? "inline": "attachment", pdfName));
		return new ResponseEntity<byte[]>(jrService.exportReportToPdf(JRDataSourceFactory.createDataSource(invoices, startDate, endDate, filteringDateType),
				DocumentType.PAYMENTS_PROSPECT, LayoutType.DENSE), HttpStatus.OK);
	}
	
}
