package com.novadart.novabill.service.validator;

import com.novadart.novabill.domain.ForgotPassword;
import com.novadart.novabill.domain.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class ForgotPasswordValidator {
	
	public static enum ValidationType{
		FULL_VALIDATION, VALIDATE_ONLY_IF_EMAIL_IN_DB
	}
	
	@Autowired
	private EmailPasswordHolderValidator validator;
	
	public void validate(ForgotPassword forgotPassword, Errors errors, ValidationType validationType){
		if(validationType == ValidationType.FULL_VALIDATION)
			validator.validate(forgotPassword, errors);
		if(forgotPassword.getEmail() == null || Principal.findByUsername(forgotPassword.getEmail()) == null)
				errors.rejectValue("email", "registration.email.notexists");
	}

}
