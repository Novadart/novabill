package com.novadart.novabill.aspect;

import com.google.common.collect.ImmutableMap;
import com.novadart.novabill.shared.client.exception.ClientUIException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

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
	
	@ExceptionHandler(value = {FreeUserAccessForbiddenException.class})
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	@ResponseBody
	public Map<String, Object> RestExceptionProcessor.freeUserAccessForbiddenExceptionHandler(){
		return ImmutableMap.<String, Object>of("error", "FREE USER ACCESS FORBIDDEN");
	}

	@ExceptionHandler(value = {ClientUIException.class})
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	public void RestExceptionProcessor.clientUIErrorExceptionHandler(){}
	
	@ExceptionHandler(value = {RuntimeException.class})
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public Map<String, Object> RestExceptionProcessor.runtimeExceptionHandler(){
		return ImmutableMap.<String, Object>of("error", "INTERNAL SERVER ERROR");
	}
	
}
