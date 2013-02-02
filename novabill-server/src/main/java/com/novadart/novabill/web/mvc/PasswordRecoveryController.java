package com.novadart.novabill.web.mvc;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import com.novadart.novabill.domain.ForgotPassword;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.validator.ForgotPasswordValidator;
import com.novadart.novabill.service.validator.ForgotPasswordValidator.ValidationType;

@Controller
@RequestMapping("/password-recovery")
@SessionAttributes("forgotPassword")
public class PasswordRecoveryController {
	
	@Autowired
	private ForgotPasswordValidator validator;

	@RequestMapping(method = RequestMethod.GET)
	public String setupForm(@RequestParam("email") String email, @RequestParam("token") String token, Model model) throws CloneNotSupportedException{
		for(ForgotPassword forgotPassword : ForgotPassword.findForgotPasswords(email, token)){
			if(forgotPassword.getExpirationDate().before(new Date())){ //expired
				forgotPassword.remove();
				continue;
			}
			model.addAttribute("forgotPassword", ((ForgotPassword)forgotPassword.clone()).clearPasswordFields());
			return "passwordRecovery";
		}
		return "invalidForgotPasswordRequest";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@Transactional(readOnly = false)
	public String processSubmit(@ModelAttribute("forgotPassword") ForgotPassword forgotPassword, BindingResult result, SessionStatus status){
		validator.validate(forgotPassword, result, ValidationType.FULL_VALIDATION);
		if(result.hasErrors())
			return "passwordRecovery";
		else{
			Principal principal = Principal.findByUsername(forgotPassword.getEmail());
			principal.setPassword(forgotPassword.getPassword());
			forgotPassword.remove();
			status.setComplete();
			return "passwordRecoverySuccess";
		}
	}

}
