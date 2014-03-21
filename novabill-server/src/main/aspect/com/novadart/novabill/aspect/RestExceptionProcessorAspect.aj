package com.novadart.novabill.aspect;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.novadart.novabill.annotation.RestExceptionProcessingMixin;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.ValidationException;

public aspect RestExceptionProcessorAspect {
	
	declare parents : @RestExceptionProcessingMixin * implements RestExceptionProcessor;

	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public String RestExceptionProcessor.invalidRequestExceptionHandler(){
		return "BAD REQUEST!";
	}

	@ExceptionHandler(value = {AccessDeniedException.class, DataAccessException.class})
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public String RestExceptionProcessor.accessDeniedExceptionHandler(){
		return "UNAUTHORIZED ACCESS!";
	}
	
	@ExceptionHandler(value = {RuntimeException.class})
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public String RestExceptionProcessor.runtimeExceptionHandler(){
		return "INTERNAL SERVER ERROR!";
	}
	
}
