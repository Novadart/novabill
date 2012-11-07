package com.novadart.novabill.service.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import com.novadart.novabill.domain.Registration;
import com.novadart.novabill.domain.security.Principal;

@Service
public class RegistrationValidator {
	
	@Autowired
	private EmailPasswordHolderValidator validator;
	
	public void validate(Registration registration, Errors errors){
		validator.validate(registration, errors);
		if(Principal.findByUsername(registration.getEmail()) != null)
				errors.rejectValue("email", "registration.email.exists");
	}

}
