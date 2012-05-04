package com.novadart.novabill.web.mvc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.healthmarketscience.rmiio.RemoteInputStreamClient;
import com.novadart.novabill.domain.AbstractInvoice;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Estimation;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.service.PDFGenerator.DocumentType;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.PDFGenerator;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.services.shared.ImageDTO;
import com.novadart.services.shared.ImageStoreService;

@Controller
@RequestMapping("/private/pdf")
public class PDFController {
	
	@Autowired
	PDFGenerator pdfGenerator;
	
	@Autowired
	UtilsService utilsService;
	
	@Autowired
	ImageStoreService imageStoreService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/invoices/{id}")
	@ResponseBody
	public void getInvoicePDF(@PathVariable Long id, final HttpServletResponse response) throws IOException, DataAccessException, NoSuchObjectException{
		final Invoice invoice = Invoice.findInvoice(id);
		if(invoice == null)
			throw new NoSuchObjectException();
		generatePDF(response, invoice, invoice.getBusiness(), PDFGenerator.DocumentType.INVOICE);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/estimations/{id}")
	@ResponseBody
	public void getEstimationPDF(@PathVariable Long id, final HttpServletResponse response) throws IOException, DataAccessException, NoSuchObjectException{
		final Estimation estimation = Estimation.findEstimation(id);
		if(estimation == null)
			throw new NoSuchObjectException();
		generatePDF(response, estimation, estimation.getBusiness(), PDFGenerator.DocumentType.ESTIMATION);
	}
	
	private void generatePDF(final HttpServletResponse response, final AbstractInvoice invoice, Business invoiceOwner, final PDFGenerator.DocumentType docType) throws DataAccessException,
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
							invoice.getInvoiceYear().toString(), invoice.getInvoiceID().toString());
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
