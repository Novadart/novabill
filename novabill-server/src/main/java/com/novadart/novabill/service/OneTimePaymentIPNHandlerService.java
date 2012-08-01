package com.novadart.novabill.service;

import java.util.Map;
import org.springframework.stereotype.Service;
import com.novadart.novabill.annotation.MailMixin;
import com.novadart.novabill.domain.Business;

@Service
@MailMixin
public class OneTimePaymentIPNHandlerService extends PayPalIPNHandlerService {
	
	public static final String WEB_ACCEPT = "web_accept";

	@Override
	protected boolean check(String transactionType, Map<String, String> parametersMap) {
		if(!transactionType.equals(WEB_ACCEPT))
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
