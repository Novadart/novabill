package com.novadart.novabill.aspect.logging;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.novadart.novabill.domain.Business;

privileged aspect AccountUpgradeAspect extends AbstractLogEventEmailSenderAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountUpgradeAspect.class);
	
	pointcut upgrade(Business business, int numberOfMonths):
		execution(public void com.novadart.novabill.service.web.PremiumEnablerService.enablePremiumForNMonths(..)) && args(business, numberOfMonths);
	
	pointcut upgradeError(String email, String message):
		call(private void com.novadart.novabill.paypal.OneTimePaymentIPNHandlerService.handleError(..)) && args(email, message);
	
	after(Business business, int numberOfMonths): upgrade(business, numberOfMonths){
		handleEvent(LOGGER, String.format("Account upgrade for %d months", numberOfMonths), business.getEmail(), new Date(System.currentTimeMillis()), null);
	}
	
	after(String email, String message): upgradeError(email, message){
		handleEvent(LOGGER, String.format("Account upgrade error: %s", message), email, new Date(System.currentTimeMillis()), null);
	}

}
