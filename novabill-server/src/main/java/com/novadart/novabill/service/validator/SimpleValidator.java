package com.novadart.novabill.service.validator;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.validation.ErrorObject;

/*
 * SimpleValidator service class is responsible for checking if the passed object adheres
 * to the constraints specified by javax.validation annotations --- JSR-303 validation.
 */
@Service
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
			throw new ValidationException(errors, object);
	}
	
}
