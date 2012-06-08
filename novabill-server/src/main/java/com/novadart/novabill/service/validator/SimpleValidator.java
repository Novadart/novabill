package com.novadart.novabill.service.validator;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.validation.ErrorObject;

@Service
@Primary
public class SimpleValidator {
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private ConstraintViolationToApplicationErrorMapper violationMapper;

	public <T> void validate(T object) throws ValidationException{
		boolean isValid = true;
		List<ErrorObject> errors = new ArrayList<ErrorObject>();
		//JSR-303 validation
		errors.addAll(violationMapper.convert(validator.validate(object)));
		isValid &= errors.size() == 0;
		if(!isValid)
			throw new ValidationException(errors);
	}
	
}
