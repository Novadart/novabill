package com.novadart.novabill.service.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.web.mvc.command.ChangePassword;

@Service
public class ChangePasswordValidator {
	
	@Autowired
	private Validator validator;
	
	public void validate(ChangePassword changePassword, Errors errors){
		validator.validate(changePassword, errors);
		Principal principal = Principal.findByUsername(changePassword.getEmail());
		ChangePassword temp = new ChangePassword();
		temp.setCreationTime(principal.getCreationTime());
		temp.setPassword(changePassword.getPassword()); //force hashing;
		if(!temp.getPassword().equals(principal.getPassword()))
			errors.rejectValue("password", "changePassword.wrong.password");
		if(!StringUtils.equals(changePassword.getNewPassword(), changePassword.getConfirmNewPassword()))
			errors.rejectValue("confirmNewPassword", "changePassword.password.mismatch");
		if(EmailPasswordHolderValidator.isLengthInvalid(changePassword.getNewPassword()))
			errors.rejectValue("password", "changePassword.password.lenght",
					new Object[]{EmailPasswordHolderValidator.MIN_PASSWORD_LENGTH, EmailPasswordHolderValidator.MAX_PASSWORD_LENGTH}, null);
	}

}
