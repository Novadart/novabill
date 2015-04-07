package com.novadart.novabill.web.mvc;

import com.novadart.novabill.domain.ForgotPassword;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.validator.ForgotPasswordValidator;
import com.novadart.novabill.service.validator.ForgotPasswordValidator.ValidationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Date;
import java.util.List;

@Controller
@SessionAttributes("forgotPassword")
public class PasswordRecoveryController {
	
	@Autowired
	private ForgotPasswordValidator validator;

	@RequestMapping(value = Urls.PUBLIC_PASSWORD_RECOVERY, method = RequestMethod.GET)
	public String setupForm(@RequestParam("email") String email, @RequestParam("token") String token, Model model) throws CloneNotSupportedException{
		model.addAttribute("pageName", "Creazione Nuova Password");
		List<ForgotPassword> r = ForgotPassword.findForgotPasswords(email, token);
		if(r.size() != 1)
			return "frontend.passwordRecoveryFailure";
		ForgotPassword forgotPassword = r.get(0);
		if(forgotPassword.getExpirationDate().before(new Date())){ //expired
			forgotPassword.remove();
			return "frontend.passwordRecoveryFailure";
		}
		model.addAttribute("forgotPassword", ((ForgotPassword)forgotPassword.clone()).clearPasswordFields());
		return "frontend.passwordRecovery";
	}
	
	@RequestMapping(value = Urls.PUBLIC_PASSWORD_RECOVERY, method = RequestMethod.POST)
	@Transactional(readOnly = false)
	public String processSubmit(@ModelAttribute("forgotPassword") ForgotPassword forgotPassword, BindingResult result, SessionStatus status, Model model){
		validator.validate(forgotPassword, result, ValidationType.FULL_VALIDATION);
		model.addAttribute("pageName", "Creazione Nuova Password");
		if(result.hasErrors()) {
			return "frontend.passwordRecovery";
		}
		else{
			if(ForgotPassword.findForgotPasswords(forgotPassword.getEmail(), forgotPassword.getActivationToken()).size() != 1)
				return "frontend.passwordRecoveryFailure";
			Principal principal = Principal.findByUsername(forgotPassword.getEmail());
			principal.setPassword(forgotPassword.getPassword());
			forgotPassword.remove();
			status.setComplete();
			return "redirect:" + Urls.PUBLIC_PASSWORD_RECOVERY_OK;
		}
	}
	
	@RequestMapping(value = Urls.PUBLIC_PASSWORD_RECOVERY_OK, method = RequestMethod.GET)
	public String passwordRecoveryComplete(Model model){
		model.addAttribute("pageName", "Creazione Nuova Password Completata");
		return "frontend.passwordRecoverySuccess";
		
	}

}
