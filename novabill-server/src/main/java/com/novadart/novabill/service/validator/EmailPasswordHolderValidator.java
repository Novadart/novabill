package com.novadart.novabill.service.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.novadart.novabill.domain.EmailPasswordHolder;

/*
 * EmailPasswordHolderValidator service class validates EmailPasswordHolder entities.
 * Validation amounts to checking the min/max requirements for password length and
 * equality between password field and confirmPassword field.
 */
@Service
public class EmailPasswordHolderValidator {
	
	@Autowired
	private Validator validator;
	
	public static final int MIN_PASSWORD_LENGTH = 6;
	
	public static final int MAX_PASSWORD_LENGTH = 20; 
	
	
	public static boolean isLengthInvalid(String password){
		return password == null || (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH);
	}
	
	public void validate(EmailPasswordHolder emailPasswordHolder, Errors errors){
		validator.validate(emailPasswordHolder, errors);
		if(isLengthInvalid(emailPasswordHolder.getPassword()))
			errors.rejectValue("password", "registration.password.lenght", new Object[]{MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH}, null);
		if(!StringUtils.equals(emailPasswordHolder.getPassword(), emailPasswordHolder.getConfirmPassword()))				
			errors.rejectValue("confirmPassword", "registration.password.mismatch");
	}

}
