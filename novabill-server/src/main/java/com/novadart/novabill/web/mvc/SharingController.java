package com.novadart.novabill.web.mvc;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.CreditNote;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.dto.DTOUtils;
import com.novadart.novabill.report.ReportUtils;
import com.novadart.novabill.service.SharingService;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.export.data.DataExporter;
import com.novadart.novabill.service.export.data.ExportDataBundle;
import com.novadart.novabill.service.web.BusinessService;
import com.novadart.novabill.service.web.BusinessStatsService;
import com.novadart.novabill.shared.client.dto.*;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.web.mvc.command.SharingRequest;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

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

	@Autowired
	private BusinessStatsService businessStatsService;

	@Autowired
	private BusinessService businessService;

	@Autowired
	private UtilsService utilsService;
	
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
	public String processRequestSubmit(@ModelAttribute("sharingRequest") SharingRequest sharingRequest, BindingResult result, 
			SessionStatus status, Locale locale, Model model) throws DataAccessException, FreeUserAccessForbiddenException, NotAuthenticatedException{
		validator.validate(sharingRequest, result);
		if(result.hasErrors()) {
			model.addAttribute("pageName", "Accesso alla Condivisione");
			return "sharing.request";
		}
		Business business = Business.findBusinessByVatIDIfSharingPermit(normalizeAndGetVatID(sharingRequest), sharingRequest.getEmail());
		if(business == null) 
			return "redirect:"+Urls.PUBLIC_SHARE_THANKS;
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
	
	@RequestMapping(value = "/share/{businessID}/invoices/filter", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<InvoiceDTO>> filterSharedInvoices(@PathVariable Long businessID,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = ISO.DATE) Date startDate,
			@RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = ISO.DATE) Date endDate){
		if(sharingService.isValidRequest(businessID, token))
			return new ResponseEntity<>(DTOUtils.toDTOList(Business.getAllInvoicesCreationDateInRange(businessID, startDate, endDate), DTOUtils.invoiceDTOConverter, false), HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}


	@RequestMapping(value = "/share/{businessID}/clients", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<List<ClientDTO>> getClients(@PathVariable Long businessID, @RequestParam(value = "token", required = true) String token){
		if(sharingService.isValidRequest(businessID, token))
			return utilsService.executeActionAsBusiness(()->{
				try {
					return new ResponseEntity<>(businessService.getClients(businessID), HttpStatus.OK);
				} catch (Throwable t) {
					throw new RuntimeException(t);
				}
			}, businessID);
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "/share/{businessID}/creditnotes/filter", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<CreditNoteDTO>> filterSharedCreditNotes(@PathVariable Long businessID,
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = ISO.DATE) Date startDate,
			@RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = ISO.DATE) Date endDate){
		if(sharingService.isValidRequest(businessID, token))
			return new ResponseEntity<>(DTOUtils.toDTOList(Business.getAllCreditNotesCreationDateInRange(businessID, startDate, endDate), DTOUtils.creditNoteDTOConverter, false), HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	
	public void downloadSharedDocs(Long businessID, String token, Date startDate, Date endDate,
			HttpServletResponse response, Locale locale, String exportFileName,
			ExportDataBundle exportDataBundle) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
		File zipFile = null;
		try{
			zipFile = dataExporter.exportData(exportDataBundle, messageSource, locale);
			response.setContentType("application/zip");
			response.setHeader ("Content-Disposition", 
					String.format("attachment; filename=\"%s.zip\"", exportFileName));
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
	
	@RequestMapping(value = "/share/{businessID}/invoices/download", method = RequestMethod.GET)
	@ResponseBody
	public void downloadSharedInvoices(@PathVariable Long businessID, @RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = ISO.DATE) Date startDate,
			@RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = ISO.DATE) Date endDate,
			HttpServletResponse response, Locale locale) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
		if(sharingService.isValidRequest(businessID, token)){
			List<Invoice> invoices = Business.getAllInvoicesCreationDateInRange(businessID, startDate, endDate);
			ExportDataBundle exportDataBundle = new ExportDataBundle();
			exportDataBundle.setBusiness(Business.findBusiness(businessID));
			exportDataBundle.setInvoices(new HashSet<>(invoices));
			String exportFileName =  ReportUtils.cutFileName(String.format(messageSource.getMessage("share.invoice.filename", null, "data", locale),
					ReportUtils.convertToASCII(Business.findBusiness(businessID).getName())));
			downloadSharedDocs(businessID, token, startDate, endDate, response, locale, exportFileName, exportDataBundle);
		}
	}
	
	@RequestMapping(value = "/share/{businessID}/creditnotes/download", method = RequestMethod.GET)
	@ResponseBody
	public void downloadSharedCreditNotes(@PathVariable Long businessID, @RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = ISO.DATE) Date startDate,
			@RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = ISO.DATE) Date endDate,
			HttpServletResponse response, Locale locale) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
		if(sharingService.isValidRequest(businessID, token)){
			List<CreditNote> creditNotes = Business.getAllCreditNotesCreationDateInRange(businessID, startDate, endDate);
			ExportDataBundle exportDataBundle = new ExportDataBundle();
			exportDataBundle.setBusiness(Business.findBusiness(businessID));
			exportDataBundle.setCreditNotes(new HashSet<>(creditNotes));
			String exportFileName =  ReportUtils.cutFileName(String.format(messageSource.getMessage("share.creditnote.filename", null, "data", locale),
					ReportUtils.convertToASCII(Business.findBusiness(businessID).getName())));
			downloadSharedDocs(businessID, token, startDate, endDate, response, locale, exportFileName, exportDataBundle);
		}
	}

	@RequestMapping(value = "/share/{businessID}/bizintel/genstats/{year}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<BIGeneralStatsDTO> getGeneralBIStats(@PathVariable Long businessID,
											 @PathVariable Integer year, @RequestParam(value = "token", required = true) String token) {
		if(sharingService.isValidRequest(businessID, token)){
			return utilsService.executeActionAsBusiness(()-> {
				try {
					return new ResponseEntity<>(businessStatsService.getGeneralBIStats(businessID, year), HttpStatus.OK);
				} catch (Throwable t) {
					throw new RuntimeException(t);
				}
			}, businessID);
		} else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/share/{businessID}/bizintel/clientstats/{clientID}/{year}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<BIClientStatsDTO> getClientBIStats(@PathVariable Long businessID, @PathVariable Long clientID,
															 @PathVariable Integer year,
															 @RequestParam(value = "token", required = true) String token){
		if(sharingService.isValidRequest(businessID, token)){
			return utilsService.executeActionAsBusiness(()-> {
				try {
					return new ResponseEntity<>(businessStatsService.getClientBIStats(businessID, clientID, year), HttpStatus.OK);
				} catch (Throwable t) {
					throw new RuntimeException(t);
				}
			}, businessID);
		} else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}


}
