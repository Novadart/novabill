package com.novadart.novabill.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Xsrf {

	String tokensSessionField();
	
	String tokenRequestParam();
	
}
