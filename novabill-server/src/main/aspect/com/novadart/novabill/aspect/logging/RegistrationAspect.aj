package com.novadart.novabill.aspect.logging;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;

import com.novadart.novabill.web.mvc.command.Registration;

public aspect RegistrationAspect extends AbstractLogEventEmailSenderAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationAspect.class);
	
	pointcut registration(Registration registration, BindingResult result) :
		call(public String com.novadart.novabill.web.mvc.RegistrationController.processSubmit(..)) && args(registration, result, ..);
	
	after(Registration registration, BindingResult result) returning: registration(registration, result){
		if(!result.hasErrors())
			handleEvent(LOGGER, "Registration", registration.getEmail(), new Date(System.currentTimeMillis()), null);
	}
	
}
