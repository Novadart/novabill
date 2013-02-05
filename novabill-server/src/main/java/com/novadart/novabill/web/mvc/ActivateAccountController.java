package com.novadart.novabill.web.mvc;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.service.UtilsService;

/*
 * ActivateAccountController controller method handles the activation of previously made
 * registration request. The registration data is identified by the email address and the
 * activation token. In order to activate the account the user clicks on a link sent via email
 * that leads to the activation page where the password needs to be re-entered. Upon
 * submitting the activation request data is validated for correctness and a principal and 
 * an associated business objects are created. 
 */
@Controller
@RequestMapping("/activate")
@SessionAttributes({"registration"})
public class ActivateAccountController {
	
	@Autowired
	private UtilsService utilsService;

	@RequestMapping(method = RequestMethod.GET)
	public String setupForm(@RequestParam("email") String email, @RequestParam("token") String token, Model model) throws CloneNotSupportedException{
		if(Principal.findByUsername(email) != null) //registered user already exists
			return "invalidActivationRequest";
		for(Registration registration: Registration.findRegistrations(email, token)){
			if(registration.getExpirationDate().before(new Date())){ //expired
				registration.remove();
				continue;
			}
			model.addAttribute("registration", registration.clone());
			return "activate";
		}
		return "invalidActivationRequest";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@Transactional(readOnly = false)
	public String processSubmit(@RequestParam("j_username") String j_username, @RequestParam("j_password") String j_password,
			@ModelAttribute("registration") Registration registration, Model model, SessionStatus status) throws CloneNotSupportedException{
		if(!StringUtils.equals(utilsService.hash(j_password, registration.getCreationTime()), registration.getPassword())){
			model.addAttribute("wrongPassword", true);
			return "activate";
 		}
		Principal principal = new Principal(registration);
		principal.getGrantedRoles().add(RoleType.ROLE_BUSINESS_FREE);
		Business business = new Business();
		principal.setBusiness(business);
		business.getPrincipals().add(principal);
		business.persist();
		registration.remove();
		status.setComplete();
		return "forward:/resources/j_spring_security_check";
	}
	
}
