package com.novadart.novabill.service.validator;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.novadart.novabill.domain.Taxable;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.validation.ErrorCode;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.Field;

@Service
public class TaxableEntityValidator {
	
	private SimpleValidator simpleValidator;
	
	public void validate(Taxable taxableEntity) throws ValidationException{
		boolean isValid = true;
		List<ErrorObject> errors = new ArrayList<ErrorObject>();
		try{
			simpleValidator.validate(taxableEntity); //JSR-303 validation
		}catch (ValidationException e) {
			errors.addAll(e.getErrors());
			isValid = false;
		}
		if(StringUtils.isBlank(taxableEntity.getVatID()) && StringUtils.isBlank(taxableEntity.getSsn())){
			isValid = false;
			errors.add(new ErrorObject(Field.vatID, ErrorCode.BLANK_OR_NULL));
		}
		if(!isValid)
			throw new ValidationException(errors);
	}

}
