package com.novadart.novabill.aspect.logging;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.novadart.novabill.shared.client.exception.PremiumUpgradeException;

public aspect PremiumUpgradeErrorAspect extends AbstractLogEventEmailSenderAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PremiumUpgradeErrorAspect.class);
	
	private ThreadLocal<Throwable> lastLoggedException = new ThreadLocal<Throwable>();
	
	pointcut premiumUpgradeError():
		execution(public void com.novadart.novabill.paypal.PayPalIPNHandlerService+.handle(..));
	
	after() throwing(PremiumUpgradeException ex): premiumUpgradeError(){
		if(lastLoggedException.get() != ex){
			lastLoggedException.set(ex);
			Map<String, Object> vars = new HashMap<String, Object>();
			vars.put("Payment platform", ex.getPaymentPlatform());
			vars.put("Transaction id", ex.getTransactionID());
			vars.put("Username", ex.getUsername());
			handleEvent(LOGGER, "Premium upgrade error", "N/A", new Date(System.currentTimeMillis()), vars);
		}
	}


}
