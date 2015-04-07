package com.novadart.novabill.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.novadart.novabill.service.validator.SharingPermitEmailBusinessUniqueValidator;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = SharingPermitEmailBusinessUniqueValidator.class)
@Documented
public @interface SharingPermitEmailBusinessUnique {
	
	String message() default "Email must be unique for a business";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};

}
