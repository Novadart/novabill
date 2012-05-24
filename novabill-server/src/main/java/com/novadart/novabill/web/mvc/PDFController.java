package com.novadart.novabill.web.mvc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.healthmarketscience.rmiio.RemoteInputStreamClient;
import com.novadart.novabill.domain.AccountingDocument;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Estimation;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.service.PDFGenerator.DocumentType;
import com.novadart.novabill.service.TokenGenerator;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.PDFGenerator;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.services.shared.ImageDTO;
import com.novadart.services.shared.ImageStoreService;

@Controller
@RequestMapping("/private/pdf")
public class PDFController {
	
	public static final String TOKENS_SESSION_FIELD = "pdf.generation.tokens";
	
	@Autowired
	private PDFGenerator pdfGenerator;
	
	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private ImageStoreService imageStoreService;
	
	@Autowired
	private TokenGenerator tokenGenerator;
	
	@SuppressWarnings("unchecked")
	private Set<String> getTokensSet(HttpSession session){
		Set<String> set = (Set<String>)session.getAttribute(TOKENS_SESSION_FIELD);
		if(set == null){
			set = Collections.synchronizedSet(new HashSet<String>());
			session.setAttribute(TOKENS_SESSION_FIELD, set);
		}
		return set;
	}
	
	private boolean verifyAndRemoveToken(String token, HttpSession session){
		Set<String> tokens = getTokensSet(session);
		if(!tokens.contains(token))
			return false;
		tokens.remove(token);
		return true;
	}
	
	@RequestMapping("/token")
	@ResponseBody
	public String generateToken(HttpSession session) throws NoSuchAlgorithmException{
		String token = tokenGenerator.generateToken();
		Set<String> tokens = getTokensSet(session);
		tokens.add(token);
		return token;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/invoices/{id}")
	@ResponseBody
	public void getInvoicePDF(@PathVariable Long id, @RequestParam(value = "token", required = false) String token,
			final HttpServletResponse response, HttpSession session) throws IOException, DataAccessException, NoSuchObjectException{
		if(token == null || !verifyAndRemoveToken(token, session))
			return;
		final Invoice invoice = Invoice.findInvoice(id);
		if(invoice == null)
			throw new NoSuchObjectException();
		generatePDF(response, invoice, invoice.getBusiness(), PDFGenerator.DocumentType.INVOICE);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/estimations/{id}")
	@ResponseBody
	public void getEstimationPDF(@PathVariable Long id, @RequestParam(value = "token", required = false) String token, 
			final HttpServletResponse response, HttpSession session) throws IOException, DataAccessException, NoSuchObjectException{
		if(token == null || !verifyAndRemoveToken(token, session))
			return;
		final Estimation estimation = Estimation.findEstimation(id);
		if(estimation == null)
			throw new NoSuchObjectException();
		generatePDF(response, estimation, estimation.getBusiness(), PDFGenerator.DocumentType.ESTIMATION);
	}
	
	private void generatePDF(final HttpServletResponse response, final AccountingDocument invoice, Business invoiceOwner, final PDFGenerator.DocumentType docType) throws DataAccessException,
			FileNotFoundException, RemoteException, IOException {
		Business business = Business.findBusiness(utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId());
		if(!business.getId().equals(invoiceOwner.getId()))
			throw new DataAccessException();
		File tempLogoFile = null;
		ImageDTO logoDTO = null;
		try {
			if(business.getLogoId() != null) //business has logo
			{
				logoDTO = imageStoreService.get(business.getLogoId());
				tempLogoFile = File.createTempFile("logo", "." + logoDTO.getFormat().name());
				tempLogoFile.deleteOnExit();
				IOUtils.copy(RemoteInputStreamClient.wrap(logoDTO.getRemoteImageDataInputStream()), new FileOutputStream(tempLogoFile));
			}
			PDFGenerator.BeforeWriteEventHandler bwEvHnld = new PDFGenerator.BeforeWriteEventHandler() {
				@Override
				public void beforeWriteCallback(File file) {
					String fileName = String.format(docType == DocumentType.INVOICE? "invoice-%s-%s.pdf": "estimation-%s-%s.pdf",
							invoice.getAccountingDocumentYear().toString(), invoice.getDocumentID().toString());
					response.setContentType("application/pdf");
					response.setHeader ("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
					response.setHeader ("Content-Length", String.valueOf(file.length()));
				}
			};
			if(tempLogoFile == null)
				pdfGenerator.createAndWrite(response.getOutputStream(), invoice, null, null, null, docType, bwEvHnld);
			else
				pdfGenerator.createAndWrite(response.getOutputStream(), invoice, tempLogoFile.getPath(), logoDTO.getWidth(), logoDTO.getHeight(), docType, bwEvHnld);
		} finally {
			if(tempLogoFile != null)
				tempLogoFile.delete();
		}
	}

}
