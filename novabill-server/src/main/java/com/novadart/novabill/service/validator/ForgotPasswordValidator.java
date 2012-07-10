package com.novadart.novabill.service.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.ForgotPassword;

@Service
public class ForgotPasswordValidator {
	
	@Autowired
	private EmailPasswordHolderValidator validator;
	
	public void validate(ForgotPassword forgotPassword, Errors errors){
		validator.validate(forgotPassword, errors);
		if(Business.findByEmail(forgotPassword.getEmail()) == null)
				errors.rejectValue("email", "registration.email.notexists");
	}

}
