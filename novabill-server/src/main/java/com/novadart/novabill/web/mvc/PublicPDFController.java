package com.novadart.novabill.web.mvc;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novadart.novabill.domain.DocumentAccessToken;
import com.novadart.novabill.report.JasperReportKeyResolutionException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;


@Controller
@RequestMapping("/pdf")
public class PublicPDFController extends PDFController {

	@RequestMapping(method = RequestMethod.GET, value = "/invoices/{id}", produces = "application/pdf")
	@ResponseBody
	@Override
	public ResponseEntity<byte[]> getInvoicePDF(@PathVariable Long id, @RequestParam(value = "token", required = false) String token,
			@RequestParam(value = "print", required = false, defaultValue = "false") boolean print,
			HttpServletResponse response, Locale locale) throws IOException, DataAccessException, NoSuchObjectException, JasperReportKeyResolutionException, JRException {
		if(DocumentAccessToken.findDocumentAccessTokens(id, token).size() == 0)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		return super.getInvoicePDF(id, token, true, response, locale);
	}
	
	
	
	
}
