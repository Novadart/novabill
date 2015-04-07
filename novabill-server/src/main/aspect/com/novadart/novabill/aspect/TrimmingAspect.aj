package com.novadart.novabill.aspect;

public privileged aspect TrimmingAspect {
	
	pointcut trimmedFieldSet(Object value, Object target): set(@Trimmed * *) && args(value) && target(target);
	
	void around(Object value, Object target): trimmedFieldSet(value, target){
		proceed(value == null? null: ((String)value).trim(), target);
	}

}
