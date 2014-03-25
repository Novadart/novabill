package com.novadart.novabill.web.mvc;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.time.DateUtils;
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
import com.novadart.novabill.domain.dto.DTOUtils;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.SharingService;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.web.mvc.command.SharingRequest;

@Controller
@RequestMapping("/share")
@SessionAttributes("sharingRequest")
public class SharingController {

	@Autowired
	private SharingService sharingService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private Validator validator;
	
	@RequestMapping(value = "/ask", method = RequestMethod.GET)
	public String setupRequestForm(Model model){
		SharingRequest sharingRequest = new SharingRequest();
		model.addAttribute("sharingRequest", sharingRequest);
		return "sharingRequest";
	}
	
	@RequestMapping(value = "/ask", method = RequestMethod.POST)
	public String processRequestSubmit(@ModelAttribute("sharingRequest") SharingRequest sharingRequest, BindingResult result, SessionStatus status, Locale locale){
		validator.validate(sharingRequest, result);
		if(result.hasErrors())
			return "sharingRequest";
		Business business = Business.findBusinessByVatIDIfSharingPermit(sharingRequest.getVatID(), sharingRequest.getEmail());
		if(business == null){
			Principal principal = Principal.findByUsername(sharingRequest.getEmail());
			if(principal == null || !principal.getBusiness().getVatID().equals(sharingRequest.getVatID()))
				return "redirect:/sharingRequestAck";
			else
				business = principal.getBusiness();
		}
		sharingService.enableSharingTemporarilyAndNotifyParticipant(business, sharingRequest.getEmail(), messageSource, locale);
		status.setComplete();
		return "redirect:/sharingRequestAck";
	}
	
	
	
	@RequestMapping(value = "/share/{businessID}/{token}", method = RequestMethod.GET)
	public String share(@PathVariable Long businessID, @PathVariable String token, Model model){
		if(sharingService.isValidRequest(businessID, token)){
			model.addAttribute("invoices", Business.getAllInvoicesCreationDateInRange(businessID, DateUtils.truncate(new Date(), Calendar.YEAR), null));
			return "share";
		}else
			return "invalidSharingRequest";
	}
	
	@RequestMapping(value = "/share/{businessID}/{token}/filter", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<List<InvoiceDTO>> filterSharedDocs(@PathVariable Long businessID, @PathVariable String token,
			@RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = ISO.DATE) Date startDate,
			@RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = ISO.DATE) Date endDate){
		if(sharingService.isValidRequest(businessID, token))
			return new ResponseEntity<List<InvoiceDTO>>(DTOUtils.toDTOList(Business.getAllInvoicesCreationDateInRange(businessID, startDate, endDate), DTOUtils.invoiceDTOConverter, false), HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
}
