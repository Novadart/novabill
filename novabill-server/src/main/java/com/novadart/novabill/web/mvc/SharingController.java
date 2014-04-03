package com.novadart.novabill.web.mvc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.dto.DTOUtils;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.report.JasperReportKeyResolutionException;
import com.novadart.novabill.service.SharingService;
import com.novadart.novabill.service.export.data.DataExporter;
import com.novadart.novabill.service.export.data.ExportDataBundle;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.web.mvc.command.SharingRequest;

@Controller
@SessionAttributes("sharingRequest")
public class SharingController {

	@Autowired
	private SharingService sharingService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private DataExporter dataExporter;
	
	@RequestMapping(value = Urls.PUBLIC_SHARE_REQUEST, method = RequestMethod.GET)
	public String setupRequestForm(Model model){
		SharingRequest sharingRequest = new SharingRequest();
		model.addAttribute("pageName", "Accesso alla Condivisione");
		model.addAttribute("sharingRequest", sharingRequest);
		return "sharing.request";
	}
	
	private String normalizeAndGetVatID(SharingRequest sharingRequest){
		String vatID = sharingRequest.getVatID().trim().toUpperCase();
		return vatID.startsWith("IT")? vatID: "IT" + vatID;
	}
	
	
	@RequestMapping(value = Urls.PUBLIC_SHARE_REQUEST, method = RequestMethod.POST)
	public String processRequestSubmit(@ModelAttribute("sharingRequest") SharingRequest sharingRequest, BindingResult result, SessionStatus status, Locale locale){
		validator.validate(sharingRequest, result);
		if(result.hasErrors())
			return "sharing.request";
		Business business = Business.findBusinessByVatIDIfSharingPermit(normalizeAndGetVatID(sharingRequest), sharingRequest.getEmail());
		if(business == null){
			Principal principal = Principal.findByUsername(sharingRequest.getEmail());
			if(principal == null || !principal.getBusiness().getVatID().equals(sharingRequest.getVatID()))
				return "redirect:"+Urls.PUBLIC_SHARE_THANKS;
			else
				business = principal.getBusiness();
		}
		sharingService.enableSharingTemporarilyAndNotifyParticipant(business, sharingRequest.getEmail(), messageSource, locale);
		status.setComplete();
		return "redirect:"+Urls.PUBLIC_SHARE_THANKS;
	}
	
	@RequestMapping(value = Urls.PUBLIC_SHARE_THANKS, method = RequestMethod.GET)
	public String thanks(Model model){
		model.addAttribute("pageName", "Richiesta inserita correttamente");
		return "sharing.requestAck";
	}
	
	@RequestMapping(value = Urls.PUBLIC_SHARE_SHARE, method = RequestMethod.GET)
	public String share(@RequestParam Long businessID, @RequestParam String token, Model model){
		if(sharingService.isValidRequest(businessID, token)){
			Business b = Business.findBusiness(businessID);
			model.addAttribute("pageName", b.getName() + " - Condivisione Dati");
			model.addAttribute("businessName", b.getName());
			return "sharing.share";
		}else {
			model.addAttribute("pageName", "Condivisione Disabilitata");
			return "sharing.invalidSharingRequest";
		}
	}
	
	@RequestMapping(value = "/share/{businessID}/filter", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<InvoiceDTO>> filterSharedDocs(@PathVariable Long businessID,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = ISO.DATE) Date startDate,
			@RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = ISO.DATE) Date endDate){
		if(sharingService.isValidRequest(businessID, token))
			return new ResponseEntity<List<InvoiceDTO>>(DTOUtils.toDTOList(Business.getAllInvoicesCreationDateInRange(businessID, startDate, endDate), DTOUtils.invoiceDTOConverter, false), HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "/share/{businessID}/download", method = RequestMethod.GET)
	@ResponseBody
	public void getSharedDocs(@PathVariable Long businessID, @RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = ISO.DATE) Date startDate,
			@RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = ISO.DATE) Date endDate,
			HttpServletResponse response, Locale locale) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException, JRException, JasperReportKeyResolutionException{
		if(sharingService.isValidRequest(businessID, token)){
			List<Invoice> invoices = Business.getAllInvoicesCreationDateInRange(businessID, startDate, endDate);
			File zipFile = null;
			try{
				ExportDataBundle exportDataBundle = new ExportDataBundle();
				exportDataBundle.setBusiness(Business.findBusiness(businessID));
				exportDataBundle.setInvoices(new HashSet<>(invoices));
				zipFile = dataExporter.exportData(exportDataBundle, messageSource, locale);
				response.setContentType("application/zip");
				response.setHeader ("Content-Disposition", 
						String.format("attachment; filename=\"%s.zip\"", messageSource.getMessage("export.filename", null, "data", locale)));
				response.setHeader ("Content-Length", String.valueOf(zipFile.length()));
				ServletOutputStream out = response.getOutputStream();
				IOUtils.copy(new FileInputStream(zipFile), out);
				out.flush();
				out.close();
			}finally{
				if(zipFile != null)
					zipFile.delete();
			}
		}
	}
	
}
