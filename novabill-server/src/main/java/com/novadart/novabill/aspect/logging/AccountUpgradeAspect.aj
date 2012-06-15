package com.novadart.novabill.aspect.logging;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

privileged aspect AccountUpgradeAspect extends AbstractLogEventEmailSenderAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountUpgradeAspect.class);
	
	pointcut upgrade(String email):
		call(private void com.novadart.novabill.web.mvc.UpgradeAccountController.upgrade(..)) && args(email, ..);
	
	pointcut upgradeError(String email, String message):
		call(private void com.novadart.novabill.web.mvc.UpgradeAccountController.handleError(..)) && args(email, message);
	
	after(String email): upgrade(email){
		handleEvent(LOGGER, "Account upgrade", email, new Date(System.currentTimeMillis()), null);
	}
	
	after(String email, String message): upgradeError(email, message){
		handleEvent(LOGGER, String.format("Account upgrade error: %s", message), email, new Date(System.currentTimeMillis()), null);
	}

}
