package com.novadart.novabill.service.validator;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import com.novadart.novabill.domain.AccountingDocument;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.validation.ErrorObject;

@Service
@Primary
public class AccountingDocumentValidator{
	
	@Autowired
	private SimpleValidator simpleValidator;
	
	public void validate(AccountingDocument accountingDocument) throws ValidationException{
		boolean isValid = true;
		List<ErrorObject> errors = new ArrayList<ErrorObject>();
		try{
			simpleValidator.validate(accountingDocument); //JSR-303 validation
		}catch (ValidationException e) {
			errors.addAll(e.getErrors());
			isValid = false;
		}
		if(!isValid)
			throw new ValidationException(errors);
	}

}
