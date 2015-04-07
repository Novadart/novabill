package com.novadart.novabill.web.mvc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.novadart.novabill.domain.DocumentAccessToken;
import com.novadart.novabill.domain.Invoice;

@Controller
public class EmailInvoiceController {
	
	@RequestMapping(value = Urls.PUBLIC_EMAIL_INVOICES, method = RequestMethod.GET)
	public String get(@RequestParam Long id, @RequestParam String token, Model model) throws UnsupportedEncodingException{
		if(DocumentAccessToken.findDocumentAccessTokens(id, token).size() == 0)
			return "redirect:" + Urls.PUBLIC_PAGE_NOT_FOUND;
		Invoice invoice = Invoice.findInvoice(id);
		if(invoice == null){
			model.addAttribute("pdfUrl", null);
		} else {
			model.addAttribute("pageName", invoice.getBusiness().getName() + " - Accesso alla Fattura");
			model.addAttribute("businessName", invoice.getBusiness().getName());
			String pdfUrl = String.format("/pdf/invoices/%d?token=%s", id, URLEncoder.encode(token, "UTF-8"));
			model.addAttribute("pdfUrl", pdfUrl);
		}
		return "email.invoice.pdf";
	}

}
