package com.novadart.novabill.paypal;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.novadart.novabill.domain.Business;

@Service
public class OneTimePaymentIPNHandlerService extends PayPalIPNHandlerService {
	
	private static final String WEB_ACCEPT = "web_accept";
	
	private static final String MC_GROSS = "mc_gross";
	
	private static final String MC_CURRENCY = "mc_currency";
	
	private void handleError(String email, String message){}
	
	private boolean checkCurrencyAndAmount(Map<String, String> parametersMap, PayPalPaymentPlanDescriptor paymentPlan){
		if(new BigDecimal(parametersMap.get(MC_GROSS)).compareTo(paymentPlan.getMcGross()) != 0){
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
		PayPalPaymentPlanDescriptor paymentPlan = paymentPlans.getPayPalPaymentPlanDescriptor(parametersMap.get(ITEM_NAME));
		if(!transactionType.equals(WEB_ACCEPT))
			return false;
		if(!parametersMap.get(PAYMENT_STATUS).equals(COMPLETED)) //not completed, ignore
			return false;
		if(parametersMap.get(ITEM_NAME).equals(paymentPlan.getItemName()) && !checkCurrencyAndAmount(parametersMap, paymentPlan))
			return false;
		return true;
	}

	@Override
	protected void extendNonFreeAccountExpirationTime(Business business, int numberOfMonths) {
		Long current = System.currentTimeMillis(), nonFreeAccountExpirationTime = business.getSettings().getNonFreeAccountExpirationTime();
		Long zero = nonFreeAccountExpirationTime == null || nonFreeAccountExpirationTime < current? current: nonFreeAccountExpirationTime;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(zero);
		calendar.add(Calendar.MONTH, numberOfMonths);
		business.getSettings().setNonFreeAccountExpirationTime(calendar.getTimeInMillis());
	}

}
