package com.novadart.novabill.service.validator;

import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.web.mvc.command.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

/*
 * RegistrationValidator service class validates Registration entities. Registration
 * entity must be a valid EmailPasswordHolder entity and it should not hold the email
 * of an existing user.
 */
@Service
public class RegistrationValidator {
	
	@Autowired
	private EmailPasswordHolderValidator validator;
	
	public void validate(Registration registration, Errors errors){
		validator.validate(registration, errors);
		if(registration.getEmail() != null && Principal.findByUsername(registration.getEmail()) != null)
				errors.rejectValue("email", "registration.email.exists");
		if(!registration.isAgreementAccepted())
			errors.rejectValue("agreementAccepted", "registration.agreementAccepted.error");
	}

}
