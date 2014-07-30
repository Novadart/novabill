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
	
	public static final int MIN_PASSWORD_LENGTH = 8;
	
	public static final int MAX_PASSWORD_LENGTH = 20; 
	
	
	public static boolean isLengthInvalid(String password){
		return password == null || (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH);
	}
	
	private static boolean containsDigit(String s){
		for(int i = 0; i < s.length(); i++)
			if(Character.isDigit(s.charAt(i))) return true;
		return false;
	}
	
	public static boolean isPasswordStrong(String password){
		if(password == null) return false;
		if(password.equals(password.toUpperCase())) return false; // no lowercase letter
		if(password.equals(password.toLowerCase())) return false; // no uppercase letter
		if(password.matches("[A-Za-z0-9 ]*")) return false; // no special character
		if(!containsDigit(password)) return false; // no digit
		return true;
	}
	
	public void validate(EmailPasswordHolder emailPasswordHolder, Errors errors){
		validator.validate(emailPasswordHolder, errors);
		if(isLengthInvalid(emailPasswordHolder.getPassword()))
			errors.rejectValue("password", "registration.password.lenght", new Object[]{MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH}, null);
		if(!StringUtils.equals(emailPasswordHolder.getPassword(), emailPasswordHolder.getConfirmPassword()))				
			errors.rejectValue("confirmPassword", "registration.password.mismatch");
		//if(!isPasswordStrong(emailPasswordHolder.getPassword()))
		//	errors.rejectValue("password", "registration.password.strength");
	}

}
