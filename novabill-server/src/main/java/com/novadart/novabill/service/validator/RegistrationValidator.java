package com.novadart.novabill.service.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.novadart.novabill.domain.Registration;

@Service
public class RegistrationValidator {
	
	@Autowired
	private Validator validator;
	
	public static final int MIN_PASSWORD_LENGTH = 4;
	
	public static final int MAX_PASSWORD_LENGTH = 20; 
	
	
	public void validate(Registration registration, Errors errors){
		validator.validate(registration, errors);
		if(registration.getPassword().length() < MIN_PASSWORD_LENGTH || registration.getPassword().length() > MAX_PASSWORD_LENGTH)
			errors.rejectValue("password", "size.length.password", new Object[]{MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH}, null);
		if(!registration.getPassword().equals(registration.getConfirmPassword()))
			errors.rejectValue("confirmPassword", "password.mismatch");
	}

}
