package com.novadart.novabill.aspect;

import com.novadart.novabill.annotation.Trimmed;

public privileged aspect TrimmingAspect {
	
	pointcut trimmedFieldSet(Object value, Object target): set(@Trimmed * *) && args(value) && target(target);
	
	void around(Object value, Object target): trimmedFieldSet(value, target){
		String strValue = (String)value;
		proceed(strValue.trim(), target);
	}

}
