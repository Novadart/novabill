package com.novadart.novabill.service.validator;

import com.novadart.novabill.annotation.PaymentDeltaNotNull;
import com.novadart.novabill.domain.PaymentType;
import com.novadart.novabill.shared.client.dto.PaymentDateType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class PaymentDeltaNotNullValidator implements ConstraintValidator<PaymentDeltaNotNull, PaymentType> {

	@Override
	public void initialize(PaymentDeltaNotNull constraintAnnotation) {}

	@Override
	public boolean isValid(PaymentType value, ConstraintValidatorContext context) {
		if(PaymentDateType.IMMEDIATE.equals(value.getPaymentDateGenerator()) ||
				PaymentDateType.END_OF_MONTH.equals(value.getPaymentDateGenerator()))
			return value.getPaymentDateDelta() != null && value.getPaymentDeltaType() != null;
		return true;
	}

}
