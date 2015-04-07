package com.novadart.novabill.service.validator;

import com.novadart.novabill.domain.PriceList;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.validation.ErrorCode;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class PriceListValidator {

	@Autowired
	private SimpleValidator simpleValidator;
	
	public void validate(PriceList priceList, boolean checkIfUniqueName) throws ValidationException {
		boolean isValid = true;
		List<ErrorObject> errors = new ArrayList<>();
		try{
			simpleValidator.validate(priceList); //JSR-303 validation
		}catch (ValidationException e) {
			errors.addAll(e.getErrors());
			isValid = false;
		}
		if(checkIfUniqueName){
			if(priceList.nameExists()){
				isValid = false;
				errors.add(new ErrorObject(Field.name, ErrorCode.NOT_UNIQUE));
			}
		}
		if(!isValid)
			throw new ValidationException(errors, priceList.getClass().getName());
	}
	
}
