package com.novadart.novabill.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.novadart.novabill.quota.QuotaChecker;

@Retention(RetentionPolicy.RUNTIME)
public @interface CheckQuotas {
	
	Class<? extends QuotaChecker>[] checkers();

}
