package com.novadart.novabill.web.mvc;

import com.novadart.novabill.domain.DocumentAccessToken;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.Notification;
import com.novadart.novabill.service.web.InvoiceService;
import com.novadart.novabill.shared.client.dto.NotificationType;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;


@Controller
@RequestMapping("/pdf")
public class PublicPDFController extends PDFController {
	
	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private ReloadableResourceBundleMessageSource messageSource;

	private void createInvoiceDownloadedNotification(Invoice invoice, String message){
		Notification notification = new Notification();
		notification.setType(NotificationType.INVOICE_DOWNLOADED);
		notification.setMessage(message);
		notification.setBusiness(invoice.getBusiness());
		invoice.getBusiness().getNotifications().add(notification);
		notification.persist();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/invoices/{id}", produces = "application/pdf")
	@ResponseBody
	@Override
	public ResponseEntity<byte[]> getInvoicePDF(@PathVariable Long id, @RequestParam(value = "token", required = false) String token,
			@RequestParam(value = "print", required = false, defaultValue = "false") boolean print,
			HttpServletResponse response, Locale locale) throws IOException, DataAccessException, NoSuchObjectException {
		if(DocumentAccessToken.findDocumentAccessTokens(id, token).size() == 0)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		ResponseEntity<byte[]> result = super.getInvoicePDF(id, token, print, response, locale);
		Invoice invoice = Invoice.findInvoice(id);
		invoiceService.markViewedByClient(invoice.getBusiness().getId(), id, System.currentTimeMillis());
		createInvoiceDownloadedNotification(invoice,
				String.format(messageSource.getMessage("invoices.downloaded.notification", null, locale), invoice.getClient().getName(), invoice.getDocumentID()));
		return result;
	}
	
	
	
	
}
