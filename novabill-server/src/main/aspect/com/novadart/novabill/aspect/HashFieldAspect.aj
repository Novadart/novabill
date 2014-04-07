package com.novadart.novabill.aspect;

import org.springframework.security.authentication.encoding.PasswordEncoder;

import com.novadart.novabill.annotation.Hash;

privileged aspect HashFieldAspect {
	
	private PasswordEncoder encoder;
	
	public void setEncoder(PasswordEncoder encoder) {
		this.encoder = encoder;
	}

	pointcut callSetterOnHashPasswordField(Object target, String value, Hash hashAnnotation): 
		call(@Hash public void *.set*(String)) && target(target) && args(value) && @annotation(hashAnnotation);
	
	
	void around(Object target, String value, Hash hashAnnotation) : 
		callSetterOnHashPasswordField(target, value, hashAnnotation){
		try{
			String hashedValue = encoder.encodePassword(value, null);
			proceed(target, hashedValue, hashAnnotation);
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("Field hashing failed", e);
		}
	}
	
}
