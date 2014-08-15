package com.novadart.novabill.web.mvc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.novadart.novabill.domain.Registration;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.service.validator.RegistrationValidator;

/*
 * RegistrationController controller class handles the process of creating registration request.
 * It provides method that renders the registration form and the subsequent processing of the 
 * form submit. The processing includes validation of the form data and upon successful
 * validation creation of principal object.
 */
@Controller
@SessionAttributes("registration")
public class RegistrationController{
	
	@Autowired
	private RegistrationValidator validator;
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@RequestMapping(value = Urls.PUBLIC_REGISTER, method = RequestMethod.GET)
	public String login(Model model){
		model.addAttribute("pageName", "Registrazione");
		model.addAttribute("registration", new Registration());
		return "frontend.register";
	}
	
	@RequestMapping(value = Urls.PUBLIC_REGISTER, method = RequestMethod.POST)
	public String processSubmit(@ModelAttribute("registration") Registration registration, BindingResult result,
			SessionStatus status, Locale locale, Model model, HttpServletRequest request)
			throws NoSuchAlgorithmException, UnsupportedEncodingException{
		validator.validate(registration, result);
		
		if(result.hasErrors()) {
			model.addAttribute("pageName", "Registrazione");
			model.addAttribute("registration", registration);
			return "frontend.register";
		} else{
			String rawRassword = registration.getPassword();
			registration.setPassword(rawRassword); //force hashing
			Principal principal = new Principal(registration);
			principal.getGrantedRoles().add(RoleType.ROLE_BUSINESS_FREE);
			principal.persist();
			status.setComplete();
			return "forward:"+Urls.PUBLIC_LOGIN_CHECK + String.format("?j_username=%s&j_password=%s", URLEncoder.encode(registration.getEmail(), "UTF-8"), rawRassword);
		}
	}
	
	
}
