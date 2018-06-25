package com.novadart.novabill.aspect;

import com.novadart.novabill.annotation.Hash;
import org.springframework.security.crypto.password.PasswordEncoder;

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
			String hashedValue = encoder.encode(value);
			proceed(target, hashedValue, hashAnnotation);
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException("Field hashing failed", e);
		}
	}
	
}
