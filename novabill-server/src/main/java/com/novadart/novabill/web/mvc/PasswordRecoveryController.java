package com.novadart.novabill.web.mvc;

import java.util.Date;
import javax.persistence.NoResultException;
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

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.ForgotPassword;
import com.novadart.novabill.service.validator.ForgotPasswordValidator;

@Controller
@RequestMapping("/password-recovery")
@SessionAttributes("forgotPassword")
public class PasswordRecoveryController {
	
	@Autowired
	private ForgotPasswordValidator validator;

	@RequestMapping(method = RequestMethod.GET)
	public String setupForm(@RequestParam("email") String email, @RequestParam("token") String token, Model model){
		try{
			ForgotPassword forgotPassword = ForgotPassword.findForgotPassword(email, token);
			if(forgotPassword.getExpirationDate().before(new Date())){ //expired
				forgotPassword.remove();
				return "invalidActivationRequest";
			}
			forgotPassword.clearPasswordFields();
			model.addAttribute("forgotPassword", forgotPassword);
		}catch (NoResultException e) {
			return "invalidForgotPasswordRequest";
		}
		return "passwordRecovery";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@Transactional(readOnly = false)
	public String processSubmit(@ModelAttribute("forgotPassword") ForgotPassword forgotPassword, BindingResult result, SessionStatus status){
		validator.validate(forgotPassword, result);
		if(result.hasErrors())
			return "passwordRecovery";
		else{
			Business business = Business.findByEmail(forgotPassword.getEmail());
			business.setPassword(forgotPassword.getPassword());
			forgotPassword.remove();
			status.setComplete();
			return "passwordRecoverySuccess";
		}
	}

}
