package com.novadart.novabill.service.validator;

import com.novadart.novabill.annotation.TaxFieldsNotNull;
import com.novadart.novabill.domain.Taxable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Configurable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Configurable
public class TaxFieldsNotNullValidator implements ConstraintValidator<TaxFieldsNotNull, Taxable> {

	@Override
	public void initialize(TaxFieldsNotNull constraintAnnotation) {}

	@Override
	public boolean isValid(Taxable taxable, ConstraintValidatorContext context) {
		return StringUtils.isNotBlank(taxable.getVatID()) || StringUtils.isNotBlank(taxable.getSsn());
	}

}
