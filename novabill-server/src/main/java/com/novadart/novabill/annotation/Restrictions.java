package com.novadart.novabill.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.novadart.novabill.authorization.RestricionChecker;

@Retention(RetentionPolicy.RUNTIME)
public @interface Restrictions {
	
	Class<? extends RestricionChecker>[] checkers();

}
