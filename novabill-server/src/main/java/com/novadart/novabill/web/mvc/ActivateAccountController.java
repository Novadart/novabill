package com.novadart.novabill.web.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Registration;
import com.novadart.novabill.domain.security.RoleTypes;

@Controller
@RequestMapping("/activate")
@SessionAttributes({"registration"})
public class ActivateAccountController {

	@RequestMapping(method = RequestMethod.GET)
	public String setupForm(@RequestParam("email") String email, @RequestParam("token") String token, Model model){
		try {
			model.addAttribute("registration", Registration.findRegistration(email, token));
		} catch (Exception e) {
			return "invalidActivationRequest";
		}
		return "activate";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@Transactional(readOnly = false)
	public String processSubmit(@RequestParam("j_username") String j_username, @RequestParam("j_password") String j_password,
			@ModelAttribute("registration") Registration registration, Model model, SessionStatus status) throws CloneNotSupportedException{
		
		Registration activationRequest = (Registration)registration.clone();
		activationRequest.setPassword(j_password);
		if(!activationRequest.getPassword().equals(registration.getPassword())){
			model.addAttribute("error", true);
			return "activate";
		}
		Business business = new Business(registration);
		business.getGrantedRoles().add(RoleTypes.ROLE_BUSINESS_FREE);
		business.persist();
		registration.remove();
		status.setComplete();
		return "forward:/resources/j_spring_security_check";
	}
	
}
