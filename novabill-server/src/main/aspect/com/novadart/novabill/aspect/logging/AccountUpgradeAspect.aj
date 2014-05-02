package com.novadart.novabill.aspect.logging;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.novadart.novabill.domain.Business;

privileged aspect AccountUpgradeAspect extends AbstractLogEventEmailSenderAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountUpgradeAspect.class);
	
	pointcut upgrade(Business business):
		execution(protected void com.novadart.novabill.paypal.OneTimePaymentIPNHandlerService.extendNonFreeAccountExpirationTime(..)) && args(business, ..);
	
	pointcut upgradeError(String email, String message):
		call(private void com.novadart.novabill.paypal.OneTimePaymentIPNHandlerService.handleError(..)) && args(email, message);
	
	after(Business business): upgrade(business){
		handleEvent(LOGGER, "Account upgrade", business.getEmail(), new Date(System.currentTimeMillis()), null);
	}
	
	after(String email, String message): upgradeError(email, message){
		handleEvent(LOGGER, String.format("Account upgrade error: %s", message), email, new Date(System.currentTimeMillis()), null);
	}

}
