package com.novadart.novabill.web.mvc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.novadart.novabill.service.TokenGenerator;
import com.novadart.novabill.service.validator.RegistrationValidator;

/*
 * RegistrationController controller class handles the process of creating registration request.
 * It provides method that renders the registration form and the subsequent processing of the 
 * form submit. The processing includes validation of the form data, generation of activation 
 * token, sending an email containing a link to activate the account and storing the registration
 * in the DB. Both the stored registration object and the sent link contain the token which is
 * used to cross-reference them. The registration has expiration period specified by the
 * activation period field.
 * Note: More than one registration objects with the same email address can be stored in the DB
 * at the same time.
 */
@Controller
@SessionAttributes("registration")
@MailMixin
public class AuthenticationController{
	
	private static final Long MILLISECS_PER_HOUR = 60 * 1000l;
	
	@Autowired
	private RegistrationValidator validator;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private TokenGenerator tokenGenerator;
	
	@Value("${activation.url.pattern}")
	private String activationUrlPattern;
	
	@Value("${activation.period}")
	private Integer activationPeriod;
	
	private String emailTemplateLocation = "mail-templates/activation-notification.vm";
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model){
		Registration registration = new Registration();
		model.addAttribute("registration", registration);
		model.addAttribute("pageName", "Login");
		return "frontend.login";
	}
	
	private void sendActivationMail(Registration registration, Locale locale) throws UnsupportedEncodingException{
		Map<String, Object> templateVars = new HashMap<String, Object>();
		String activationLink = String.format(activationUrlPattern,
				URLEncoder.encode(registration.getEmail(), "UTF-8"), URLEncoder.encode(registration.getActivationToken(), "UTF-8"));
		templateVars.put("activationLink", activationLink);
		templateVars.put("activationPeriod", activationPeriod);
		sendMessage(registration.getEmail(), messageSource.getMessage("activation.notification", null, locale), templateVars, emailTemplateLocation);
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String processSubmit(@ModelAttribute("registration") Registration registration, BindingResult result, SessionStatus status, Locale locale, Model model)
			throws NoSuchAlgorithmException, UnsupportedEncodingException{
		validator.validate(registration, result);
		
		if(result.hasErrors()) {
			model.addAttribute("pageName", "Register");
			return "register";
		} else{
			String rawRassword = registration.getPassword();
			registration.setPassword(rawRassword); //force hashing
			registration.setConfirmPassword(rawRassword); //force hashing
			registration.setActivationToken(tokenGenerator.generateToken());
			registration.setExpirationDate(new Date(System.currentTimeMillis() + activationPeriod * MILLISECS_PER_HOUR));
			sendActivationMail(registration.merge(), locale);
			status.setComplete();
			return "redirect:/registrationCompleted";
		}
	}

}
