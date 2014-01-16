package com.novadart.novabill.service.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novadart.novabill.domain.Commodity;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.validation.ErrorCode;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;

@Service
public class CommodityValidator {

	@Autowired
	private SimpleValidator simpleValidator;
	
	public void validate(Commodity commodity, boolean checkIfUniqueSku) throws ValidationException {
		boolean isValid = true;
		List<ErrorObject> errors = new ArrayList<>();
		try{
			simpleValidator.validate(commodity); //JSR-303 validation
		}catch (ValidationException e) {
			errors.addAll(e.getErrors());
			isValid = false;
		}
		if(checkIfUniqueSku){
			if(commodity.skuExists()){
				isValid = false;
				errors.add(new ErrorObject(Field.sku, ErrorCode.NOT_UNIQUE));
			}
		}
		if(!isValid)
			throw new ValidationException(errors, commodity.getClass().getName());
	}
	
}
