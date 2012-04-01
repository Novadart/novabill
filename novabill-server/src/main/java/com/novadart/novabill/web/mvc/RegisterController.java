package com.novadart.novabill.web.mvc;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
import com.novadart.novabill.annotation.MailMixin;
import com.novadart.novabill.domain.Registration;
import com.novadart.novabill.service.validator.RegistrationValidator;


@Controller
@RequestMapping("/register")
@SessionAttributes("registration")
@MailMixin
public class RegisterController{
	
	@Autowired
	private RegistrationValidator validator;
	
	@Autowired
	private MessageSource messageSource;
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String setupForm(Model model){
		Registration registration = new Registration();
		model.addAttribute("registration", registration);
		return "register";
	}
	
	private void sendActivationMail(Registration registration, Locale locale){
		Map<String, Object> templateVars = new HashMap<String, Object>();
		templateVars.put("activationLink", "#");
		sendMessage(registration.getEmail(), messageSource.getMessage("activation.notification", null, locale), templateVars, "mail-templates/activation-notification.vm");
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String processSubmit(@ModelAttribute("registration") Registration registration, BindingResult result, SessionStatus status, Locale locale){
		validator.validate(registration, result);
		if(result.hasErrors())
			return "register";
		else{
			String rawRassword = registration.getPassword();
			registration.setPassword(rawRassword); //force hashing
			registration.setConfirmPassword(rawRassword); //force hashing
			sendActivationMail(registration.merge(), locale);
			status.setComplete();
			return "redirect:/registrationCompleted";
		}
	}

}
