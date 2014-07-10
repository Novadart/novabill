package com.novadart.novabill.paypal;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class OneTimePaymentIPNHandlerService extends PayPalIPNHandlerService {
	
	private static final String WEB_ACCEPT = "web_accept";
	
	private static final String MC_CURRENCY = "mc_currency";
	
	private void handleError(String email, String message){}
	
	private boolean checkCurrencyAndAmount(Map<String, String> parametersMap, PaymentPlanDescriptor paymentPlan){
		if(!parametersMap.get(MC_CURRENCY).equalsIgnoreCase("EUR")){
			handleError(parametersMap.get(CUSTOM), "Currency doesn't match");
			return false;
		}
		return true;
	}

	@Override
	protected boolean check(String transactionType, Map<String, String> parametersMap) {
		PaymentPlanDescriptor paymentPlan = paymentPlans.getPayPalPaymentPlanDescriptor(parametersMap.get(ITEM_NAME));
		if(!transactionType.equals(WEB_ACCEPT))
			return false;
		if(!parametersMap.get(PAYMENT_STATUS).equals(COMPLETED)) //not completed, ignore
			return false;
		if(parametersMap.get(ITEM_NAME).equals(paymentPlan.getItemName()) && !checkCurrencyAndAmount(parametersMap, paymentPlan))
			return false;
		return true;
	}

}
