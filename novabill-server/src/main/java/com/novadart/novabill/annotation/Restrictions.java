package com.novadart.novabill.annotation;

import com.novadart.novabill.authorization.RestricionChecker;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Restrictions {
	
	Class<? extends RestricionChecker>[] checkers();
	
	String businessParamName() default "[unassigned]";

}
