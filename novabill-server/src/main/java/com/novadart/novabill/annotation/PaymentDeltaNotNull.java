package com.novadart.novabill.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.novadart.novabill.service.validator.PaymentDeltaNotNullValidator;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PaymentDeltaNotNullValidator.class)
@Documented
public @interface PaymentDeltaNotNull {

	String message() default "If PaymentDateType is IMMEDIATE or END_OF_MONTH the paymentDeltaType and paymentDateDelta cannot be null";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
	
}
