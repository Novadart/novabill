package com.novadart.novabill.paypal;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OneTimePaymentIPNHandlerService extends PayPalIPNHandlerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OneTimePaymentIPNHandlerService.class);
	
	private static final String WEB_ACCEPT = "web_accept";
	
	private static final String MC_CURRENCY = "mc_currency";
	
	private boolean checkCurrencyAndAmount(Map<String, String> parametersMap, PaymentPlanDescriptor paymentPlan){
		if(!parametersMap.get(MC_CURRENCY).equalsIgnoreCase("EUR")){
			LOGGER.warn(String.format("Currency type for transaction %s does not match. Expected EUR, actual: %s",
					parametersMap.get(TRANSACTION_ID), parametersMap.get(MC_CURRENCY)));
			return false;
		}
		return true;
	}

	@Override
	protected boolean check(String transactionType, Map<String, String> parametersMap) {
		if(!transactionType.equals(WEB_ACCEPT)) {
			LOGGER.warn(String.format("Transaction type for transaction %s does not match. Expected %s, actual: %s",
					parametersMap.get(TRANSACTION_ID), WEB_ACCEPT, transactionType));
			return false;
		}
		if(!parametersMap.get(PAYMENT_STATUS).equals(COMPLETED)) { //not completed, ignore
			LOGGER.warn(String.format("Payment status for transaction %s not completed.",
					parametersMap.get(TRANSACTION_ID)));
			return false;
		}
		if(!checkCurrencyAndAmount(parametersMap,
				paymentPlans.getPayPalPaymentPlanDescriptor(parametersMap.get(ITEM_NAME))))
			return false;
		return true;
	}

}
