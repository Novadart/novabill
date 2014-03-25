package com.novadart.novabill.aspect;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.common.collect.ImmutableMap;
import com.novadart.novabill.annotation.RestExceptionProcessingMixin;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.ValidationException;

public aspect RestExceptionProcessorAspect {
	
	declare parents : @RestExceptionProcessingMixin * implements RestExceptionProcessor;

	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Map<String, Object> RestExceptionProcessor.invalidRequestExceptionHandler(ValidationException ex){
		return ImmutableMap.<String, Object>of("error", "VALIDATION ERROR", "message", ex.getErrors());
	}

	@ExceptionHandler(value = {AccessDeniedException.class, DataAccessException.class})
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public Map<String, Object> RestExceptionProcessor.accessDeniedExceptionHandler(){
		return ImmutableMap.<String, Object>of("error", "UNAUTHORIZED ACCESS");
	}
	
	@ExceptionHandler(value = {RuntimeException.class})
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public Map<String, Object> RestExceptionProcessor.runtimeExceptionHandler(){
		return ImmutableMap.<String, Object>of("error", "INTERNAL SERVER ERROR");
	}
	
}
