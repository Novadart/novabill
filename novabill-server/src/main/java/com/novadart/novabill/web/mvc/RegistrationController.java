package com.novadart.novabill.web.mvc;

import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.service.mail.EmailBuilder;
import com.novadart.novabill.service.mail.MailHandlingType;
import com.novadart.novabill.service.validator.RegistrationValidator;
import com.novadart.novabill.web.mvc.command.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

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
	
	private static final String EMAIL_TEMPLATE_LOCATION = "mail-templates/registration-complete-notification.vm";

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


	private void sendRegistrationCompletedMail(Registration registration, Locale locale) throws UnsupportedEncodingException{
		new EmailBuilder().to(registration.getEmail())
				.subject("Benvenuto in Novabill")
				.template(EMAIL_TEMPLATE_LOCATION)
				.templateVar("registrationEmail", registration.getEmail())
				.handlingType(MailHandlingType.EXTERNAL_UNACKNOWLEDGED)
				.build().send();
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
			principal.getGrantedRoles().add(RoleType.ROLE_BUSINESS_TRIAL);
			principal.persist();
			status.setComplete();
			sendRegistrationCompletedMail(registration, locale);
			return "forward:"+Urls.PUBLIC_LOGIN_CHECK + String.format("?j_username=%s&j_password=%s", URLEncoder.encode(registration.getEmail(), "UTF-8"), rawRassword);
		}
	}


}
