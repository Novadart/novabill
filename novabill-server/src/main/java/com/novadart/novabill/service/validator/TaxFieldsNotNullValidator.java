package com.novadart.novabill.service.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.novadart.novabill.annotation.TaxFieldsNotNull;
import com.novadart.novabill.domain.Taxable;

public class TaxFieldsNotNullValidator implements ConstraintValidator<TaxFieldsNotNull, Taxable> {

	@Override
	public void initialize(TaxFieldsNotNull constraintAnnotation) {}

	@Override
	public boolean isValid(Taxable value, ConstraintValidatorContext context) {
		return !(StringUtils.isBlank(value.getVatID()) && StringUtils.isBlank(value.getSsn()));
	}

}
