package com.novadart.novabill.aspect.logging;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.shared.client.exception.PremiumUpgradeException;

privileged aspect PremiumUpgradeAspect extends AbstractLogEventEmailSenderAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PremiumUpgradeAspect.class);
	
	private ThreadLocal<Throwable> lastLoggedException = new ThreadLocal<Throwable>();
	
	pointcut upgrade(Business business, int numberOfMonths):
		execution(public void com.novadart.novabill.service.web.PremiumEnablerService.enablePremiumForNMonths(..)) && args(business, numberOfMonths);
	
	pointcut premiumUpgradeError():
		execution(public void com.novadart.novabill.paypal.PayPalIPNHandlerService+.handle(..));
	
	pointcut upgradeError(String email, String message):
		call(private void com.novadart.novabill.paypal.OneTimePaymentIPNHandlerService.handleError(..)) && args(email, message);
	
	after(Business business, int numberOfMonths) returning: upgrade(business, numberOfMonths){
		Map<String, Object> vars = new HashMap<>();
		vars.put("VatID", business.getVatID());
		handleEvent(LOGGER, String.format("Account upgrade for %d months", numberOfMonths), business.getEmail(), new Date(System.currentTimeMillis()), vars);
	}
	
	after() throwing(PremiumUpgradeException ex): premiumUpgradeError(){
		if(lastLoggedException.get() != ex){
			lastLoggedException.set(ex);
			Map<String, Object> vars = new HashMap<String, Object>();
			vars.put("Payment platform", ex.getPaymentPlatform());
			vars.put("Transaction id", ex.getTransactionID());
			vars.put("Username", ex.getUsername());
			vars.put("VatID", ex.getVatID());
			handleEvent(LOGGER, "Premium upgrade error", "N/A", new Date(System.currentTimeMillis()), vars);
		}
	}

	after(String email, String message) returning: upgradeError(email, message){
		handleEvent(LOGGER, String.format("Premium upgrade error: %s", message), email, new Date(System.currentTimeMillis()), null);
	}
	
}
