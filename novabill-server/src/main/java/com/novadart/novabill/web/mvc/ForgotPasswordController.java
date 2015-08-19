package com.novadart.novabill.web.mvc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Locale;

import com.novadart.novabill.service.mail.EmailBuilder;
import com.novadart.novabill.service.mail.MailHandlingType;
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
import com.novadart.novabill.domain.ForgotPassword;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.TokenGenerator;
import com.novadart.novabill.service.validator.ForgotPasswordValidator;
import com.novadart.novabill.service.validator.ForgotPasswordValidator.ValidationType;

@Controller
@SessionAttributes("forgotPassword")
public class ForgotPasswordController {
	
	private static final String DEFAULT_PASSWORD = "password";
	
	private static final Long MILLISECS_PER_HOUR = 3_600_000l;
	
	@Autowired
	private ForgotPasswordValidator validator;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private TokenGenerator tokenGenerator;
	
	@Value("${password.recovery.period}")
	private Integer passwordRecoveryPeriod;
	
	@Value("${password.recovery.url.pattern}")
	private String passwordRecoveryUrlPattern;
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@RequestMapping(value = Urls.PUBLIC_FORGOT_PASSWORD, method = RequestMethod.GET)
	public String setupForm(Model model){
		ForgotPassword forgotPassword = new ForgotPassword(DEFAULT_PASSWORD, DEFAULT_PASSWORD);
		model.addAttribute("forgotPassword", forgotPassword);
		model.addAttribute("pageName", "Password Reset");
		return "frontend.forgotPassword";
	}
	
	@RequestMapping(value = Urls.PUBLIC_FORGOT_PASSWORD_OK, method = RequestMethod.GET)
	public String setupPasswordRecoveryOk(Model model){
		ForgotPassword forgotPassword = new ForgotPassword(DEFAULT_PASSWORD, DEFAULT_PASSWORD);
		model.addAttribute("forgotPassword", forgotPassword);
		model.addAttribute("pageName", "Password Reset");
		return "frontend.forgotPasswordOk";
	}
	
	private void sendActivationMail(ForgotPassword forgotPassword, Locale locale) throws UnsupportedEncodingException{
		String passwordRecoveryLink = String.format(passwordRecoveryUrlPattern,
				URLEncoder.encode(forgotPassword.getEmail(), "UTF-8"), URLEncoder.encode(forgotPassword.getActivationToken(), "UTF-8"));
		new EmailBuilder().to(forgotPassword.getEmail())
				.subject(messageSource.getMessage("password.recovery.notification", null, locale))
				.template("mail-templates/password-recovery-notification.vm")
				.templateVar("passwordRecoveryLink", passwordRecoveryLink)
				.templateVar("passwordRecoveryPeriod", passwordRecoveryPeriod)
				.handlingType(MailHandlingType.EXTERNAL_UNACKNOWLEDGED)
				.build().send();
	}
	
	@RequestMapping(value = Urls.PUBLIC_FORGOT_PASSWORD, method = RequestMethod.POST)
	public String processSubmit(@ModelAttribute("forgotPassword") ForgotPassword forgotPassword, BindingResult result, 
			SessionStatus status, Locale locale, Model model)
			throws NoSuchAlgorithmException, UnsupportedEncodingException{
		validator.validate(forgotPassword, result, ValidationType.VALIDATE_ONLY_IF_EMAIL_IN_DB);
		if(result.hasErrors()) {
			model.addAttribute("pageName", "Password Reset");
			return "frontend.forgotPassword";
		}
		else{
			forgotPassword.setActivationToken(tokenGenerator.generateToken());
			forgotPassword.setCreationTime(Principal.findByUsername(forgotPassword.getEmail()).getCreationTime());
			forgotPassword.setPassword(DEFAULT_PASSWORD); //force hashing
			forgotPassword.setConfirmPassword(DEFAULT_PASSWORD); //force hashing
			forgotPassword.setExpirationDate(new Date(System.currentTimeMillis() + passwordRecoveryPeriod * MILLISECS_PER_HOUR));
			sendActivationMail(forgotPassword.merge(), locale);
			status.setComplete();
			return "redirect:" + Urls.PUBLIC_FORGOT_PASSWORD_OK;
		}
	}

}
