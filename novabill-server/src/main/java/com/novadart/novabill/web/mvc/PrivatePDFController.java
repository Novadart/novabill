package com.novadart.novabill.web.mvc;

import com.novadart.novabill.annotation.Restrictions;
import com.novadart.novabill.authorization.PremiumChecker;
import com.novadart.novabill.shared.client.data.FilteringDateType;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;

@Controller
@RequestMapping("/private/pdf")
public class PrivatePDFController extends PDFController {

	@PreAuthorize("T(com.novadart.novabill.domain.Invoice).findInvoice(#id)?.business?.id == principal.business.id")
	@RequestMapping(method = RequestMethod.GET, value = "/invoices/{id}", produces = "application/pdf")
	@ResponseBody
	@Override
	public ResponseEntity<byte[]> getInvoicePDF(@PathVariable Long id, @RequestParam(value = "token", required = false) String token,
			@RequestParam(value = "print", required = false, defaultValue = "false") boolean print,
			HttpServletResponse response, Locale locale) throws IOException, DataAccessException, NoSuchObjectException {
		return super.getInvoicePDF(id, token, print, response, locale);
	}

	@PreAuthorize("T(com.novadart.novabill.domain.Estimation).findEstimation(#id)?.business?.id == principal.business.id")
	@RequestMapping(method = RequestMethod.GET, value = "/estimations/{id}", produces = "application/pdf")
	@ResponseBody
	@Override
	public ResponseEntity<byte[]> getEstimationPDF(@PathVariable Long id, @RequestParam(value = "token", required = false) String token,
			@RequestParam(value = "print", required = false, defaultValue = "false") boolean print,
			HttpServletResponse response, Locale locale) throws IOException, DataAccessException, NoSuchObjectException {
		return super.getEstimationPDF(id, token, print, response, locale);
	}

	@PreAuthorize("T(com.novadart.novabill.domain.CreditNote).findCreditNote(#id)?.business?.id == principal.business.id")
	@RequestMapping(method = RequestMethod.GET, value = "/creditnotes/{id}", produces = "application/pdf")
	@ResponseBody
	@Override
	public ResponseEntity<byte[]> getCreditNotePDF(@PathVariable Long id, @RequestParam(value = "token", required = false) String token,
			@RequestParam(value = "print", required = false, defaultValue = "false") boolean print,
			HttpServletResponse response, Locale locale) throws IOException, DataAccessException, NoSuchObjectException {
		return super.getCreditNotePDF(id, token, print, response, locale);
	}

	@PreAuthorize("T(com.novadart.novabill.domain.TransportDocument).findTransportDocument(#id)?.business?.id == principal.business.id")
	@Override
	@RequestMapping(method = RequestMethod.GET, value = "/transportdocs/{id}", produces = "application/pdf")
	@ResponseBody
	public ResponseEntity<byte[]> getTransportDocumentPDF(@PathVariable Long id, @RequestParam(value = "token", required = false) String token,
			@RequestParam(value = "print", required = false, defaultValue = "false") boolean print,
			HttpServletResponse response, Locale locale) throws IOException, DataAccessException, NoSuchObjectException {
		return super.getTransportDocumentPDF(id, token, print, response, locale);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/paymentspros", produces = "application/pdf")
	@ResponseBody
	@Restrictions(checkers = {PremiumChecker.class})
	public ResponseEntity<byte[]> getPaymentsProspectPaymentDueDatePDF(@RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = ISO.DATE) Date startDate,
			@RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = ISO.DATE) Date endDate,
			@RequestParam(value = "filteringDateType") FilteringDateType filteringDateType,
			@RequestParam(value = "token", required = false) String token,
			@RequestParam(value = "print", required = false, defaultValue = "false") boolean print,
			HttpServletResponse response, Locale locale) throws FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException {
		return super.getPaymentsProspectPaymentDueDatePDF(startDate, endDate, filteringDateType, token, print, response, locale);
	}

}
