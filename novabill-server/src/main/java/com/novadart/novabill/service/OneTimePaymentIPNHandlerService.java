package com.novadart.novabill.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.novadart.novabill.domain.Business;

@Service
public class OneTimePaymentIPNHandlerService extends PayPalIPNHandlerService {
	
	private static final String WEB_ACCEPT = "web_accept";
	
	private static final String ITEM_NAME = "item_name";
	
	private static final String MC_GROSS = "mc_gross";
	
	private static final String MC_CURRENCY = "mc_currency";
	
	@Value("${paypal.premiumOneYearItemName}")
	private String premiumOneYearItemName;
	
	@Value("${paypal.premiumOneYearMCGross}")
	private BigDecimal premiumOneYearMCGross;
	
	private void handleError(String email, String message){}
	
	private boolean checkPremiumOneYear(Map<String, String> parametersMap){
		if(new BigDecimal(parametersMap.get(MC_GROSS)).compareTo(premiumOneYearMCGross) != 0){
			handleError(parametersMap.get(CUSTOM), "Amount doesn't match");
			return false;
		}
		if(!parametersMap.get(MC_CURRENCY).equalsIgnoreCase("EUR")){
			handleError(parametersMap.get(CUSTOM), "Currency doesn't match");
			return false;
		}
		return true;
	}

	@Override
	protected boolean check(String transactionType, Map<String, String> parametersMap) {
		if(!transactionType.equals(WEB_ACCEPT))
			return false;
		if(!parametersMap.get(PAYMENT_STATUS).equals(COMPLETED)) //not completed ignore
			return false;
		if(parametersMap.get(ITEM_NAME).equals(premiumOneYearItemName) && !checkPremiumOneYear(parametersMap))
			return false;
		return true;
	}

	@Override
	protected void extendNonFreeAccountExpirationTime(Business business, Map<String, String> parametersMap) {
		Long current = System.currentTimeMillis(), nonFreeAccountExpirationTime = business.getNonFreeAccountExpirationTime();
		Long zero = nonFreeAccountExpirationTime == null || nonFreeAccountExpirationTime < current? current: nonFreeAccountExpirationTime;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(zero);
		if(parametersMap.get(ITEM_NAME).equals(premiumOneYearItemName))
			calendar.add(Calendar.YEAR, 1);
		business.setNonFreeAccountExpirationTime(calendar.getTimeInMillis());
	}

}
