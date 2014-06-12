package com.novadart.novabill.paypal;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.service.PrincipalDetailsService;
import com.novadart.novabill.service.web.PremiumEnablerService;
import com.novadart.novabill.shared.client.exception.PremiumUpgradeException;

public abstract class PayPalIPNHandlerService {
	
	protected final static String COMPLETED = "Completed";
	
	protected final static String PAYMENT_STATUS = "payment_status";
	
	protected final static String CUSTOM = "custom";
	
	protected final static String ITEM_NAME = "item_name";
	
	protected final static String TRANSACTION_ID = "txn_id";
	
	@Autowired
	private PrincipalDetailsService principalDetailsService;
	
	@Autowired
	protected PaymentPlansLoader paymentPlans;
	
	@Autowired
	private PremiumEnablerService premiumEnablerService;

	@Transactional(readOnly = false)
	public void handle(String transactionType, Map<String, String> parametersMap) throws PremiumUpgradeException {
		preProcess(parametersMap);
		if(!check(transactionType, parametersMap))
			return;
		String email = parametersMap.get(CUSTOM);
		Business business = principalDetailsService.loadUserByUsername(email).getBusiness();
		premiumEnablerService.enablePremiumForNMonths(business, paymentPlans.getPayPalPaymentPlanDescriptor(parametersMap.get(ITEM_NAME)).getPayedPeriodInMonths());
//		sendMessage(parametersMap.get(CUSTOM), "Account upgraded", new HashMap<String, Object>(), "mail-templates/upgrade-notification.vm",
//				"This is a test message".getBytes(), "test.txt");
		try {
			premiumEnablerService.notifyAndInvoiceBusiness(business, paymentPlans.getPayPalPaymentPlanDescriptor(parametersMap.get(ITEM_NAME)).getItemName(), email);
		} catch (PremiumUpgradeException e) {
			e.setUsername(email);
			e.setTransactionID(parametersMap.get(TRANSACTION_ID));
			throw e;
		}
		postProcess(parametersMap);
	}
	
	abstract protected boolean check(String transactionType, Map<String, String> parametersMap);
	
	protected void preProcess(Map<String, String> parametersMap){}
	
	protected void postProcess(Map<String, String> parametersMap){}
	
}
