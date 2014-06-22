package com.novadart.novabill.web.mvc;

import java.util.Date;
import java.util.List;
import java.util.Locale;

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
@RequestMapping(Urls.PUBLIC_ACTIVATE)
@SessionAttributes({"registration"})
public class ActivateAccountController {
	
	public static final String ACTIVATION_SUCCESS_URL = "forward:/resources/login_check";
	
	@Autowired
	private UtilsService utilsService;

	@RequestMapping(method = RequestMethod.GET)
	public String setupForm(@RequestParam("email") String email, @RequestParam("token") String token, Model model) throws CloneNotSupportedException{
		model.addAttribute("pageName", "Attivazione Account");
		if(Principal.findByUsername(email) != null) //registered user already exists
			return "frontend.invalidActivationRequest";
		List<Registration> r = Registration.findRegistrations(email, token);
		if(r.size() != 1)
			return "frontend.invalidActivationRequest";
		Registration registration = r.get(0);
		if(registration.getExpirationDate().before(new Date())){ //expired
			registration.remove();
			return "frontend.invalidActivationRequest";
		}
		model.addAttribute("pageName", "Attivazione Account");
		model.addAttribute("registration", registration.clone());
		return "frontend.activate";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@Transactional(readOnly = false)
	public String processSubmit(@RequestParam("j_username") String j_username, @RequestParam("j_password") String j_password,
			@ModelAttribute("registration") Registration registration, Model model, SessionStatus status, Locale locale) throws CloneNotSupportedException{
		model.addAttribute("pageName", "Attivazione Account");
		if(!utilsService.isPasswordValid(registration.getPassword(), j_password)){
			model.addAttribute("wrongPassword", true);
			return "frontend.activate";
 		}
		Principal principal = new Principal(registration);
		principal.getGrantedRoles().add(RoleType.ROLE_BUSINESS_FREE);
		principal.persist();
		registration.remove();
		status.setComplete();
		return ACTIVATION_SUCCESS_URL;
	}
	
}
