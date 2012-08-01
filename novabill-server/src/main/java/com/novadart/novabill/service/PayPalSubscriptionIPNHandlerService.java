package com.novadart.novabill.service;

import java.util.Map;
import org.springframework.stereotype.Service;
import com.novadart.novabill.annotation.MailMixin;
import com.novadart.novabill.domain.Business;

//@Service
@MailMixin
public class PayPalSubscriptionIPNHandlerService extends PayPalIPNHandlerService{
	
	private static final String SIGNUP = "subscr_signup";
	
	private static final String SUBSCRIPTION_PAYMENT = "subscr_payment";

	@Override
	protected boolean check(String transactionType, Map<String, String> parametersMap) {
		if(!transactionType.equals(SUBSCRIPTION_PAYMENT))
			return false;
		if(!parametersMap.get(PAYMENT_STATUS).equals(COMPLETED)) //not completed ignore
			return false;
		//TODO do additional checks here eg amount
		return true;
	}

	@Override
	protected void extendNonFreeAccountExpirationTime(Business business, Map<String, String> parametersMap) {
		// TODO Auto-generated method stub
		
	}

}
