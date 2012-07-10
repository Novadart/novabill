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
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.ForgotPassword;
import com.novadart.novabill.service.TokenGenerator;
import com.novadart.novabill.service.validator.ForgotPasswordValidator;

@Controller
@RequestMapping("/forgot-password")
@SessionAttributes("forgotPassword")
@MailMixin
public class ForgotPasswordController {
	
	private static final String DEFAULT_PASSWORD = "password";
	
	private static final Long MILLISECS_PER_HOUR = 60 * 1000l;
	
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
	
	@RequestMapping(method = RequestMethod.GET)
	public String setupForm(Model model){
		ForgotPassword forgotPassword = new ForgotPassword(DEFAULT_PASSWORD, DEFAULT_PASSWORD);
		model.addAttribute("forgotPassword", forgotPassword);
		return "forgotPassword";
	}
	
	private void sendActivationMail(ForgotPassword forgotPassword, Locale locale) throws UnsupportedEncodingException{
		Map<String, Object> templateVars = new HashMap<String, Object>();
		String passwordRecoveryLink = String.format(passwordRecoveryUrlPattern,
				URLEncoder.encode(forgotPassword.getEmail(), "UTF-8"), URLEncoder.encode(forgotPassword.getActivationToken(), "UTF-8"));
		templateVars.put("passwordRecoveryLink", passwordRecoveryLink);
		templateVars.put("passwordRecoveryPeriod", passwordRecoveryPeriod);
		sendMessage(forgotPassword.getEmail(), messageSource.getMessage("password.recovery.notification", null, locale), templateVars, "mail-templates/password-recovery-notification.vm");
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String processSubmit(@ModelAttribute("forgotPassword") ForgotPassword forgotPassword, BindingResult result, SessionStatus status, Locale locale)
			throws NoSuchAlgorithmException, UnsupportedEncodingException{
		validator.validate(forgotPassword, result);
		if(result.hasErrors())
			return "forgotPassword";
		else{
			forgotPassword.setActivationToken(tokenGenerator.generateToken());
			forgotPassword.setCreationTime(Business.findByEmail(forgotPassword.getEmail()).getCreationTime());
			forgotPassword.setPassword(DEFAULT_PASSWORD); //force hashing
			forgotPassword.setConfirmPassword(DEFAULT_PASSWORD); //force hashing
			forgotPassword.setExpirationDate(new Date(System.currentTimeMillis() + passwordRecoveryPeriod * MILLISECS_PER_HOUR));
			sendActivationMail(forgotPassword.merge(), locale);
			status.setComplete();
			return "redirect:/passwordRecoveryCommenced";
		}
	}

}