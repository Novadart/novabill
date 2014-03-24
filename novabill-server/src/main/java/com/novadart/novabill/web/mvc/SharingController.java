package com.novadart.novabill.web.mvc;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.SharingService;
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
	
	
	
	@RequestMapping(value = "/share/{businessID}/{token}")
	public String share(@PathVariable Long businessID, @PathVariable String token){
		if(sharingService.isValidRequest(businessID, token)){
			return "share";
		}else
			return "invalidSharingRequest";
	}
	
}
