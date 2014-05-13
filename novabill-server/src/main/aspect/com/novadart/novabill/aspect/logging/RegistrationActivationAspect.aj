package com.novadart.novabill.aspect.logging;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.novadart.novabill.domain.Registration;

public aspect RegistrationActivationAspect extends AbstractLogEventEmailSenderAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationActivationAspect.class);
	
	pointcut registration(Registration registration) :
		execution(public String com.novadart.novabill.web.mvc.RegistrationController.processSubmit(..)) && args(registration, ..);
	
	pointcut activation(Registration registration) :
		execution(public String com.novadart.novabill.web.mvc.ActivateAccountController.processSubmit(..)) && args(*, *, registration, ..);
	
	after(Registration registration) returning: registration(registration){
		handleEvent(LOGGER, "Registration", registration.getEmail(), new Date(System.currentTimeMillis()), null);
	}
	
	after(Registration registration) returning: activation(registration){
		handleEvent(LOGGER, "Activation", registration.getEmail(), new Date(System.currentTimeMillis()), null);
	}

}
