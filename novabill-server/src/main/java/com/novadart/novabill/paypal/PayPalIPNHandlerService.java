package com.novadart.novabill.paypal;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.annotation.MailMixin;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.service.PrincipalDetailsService;

@MailMixin
public abstract class PayPalIPNHandlerService {
	
	protected final static String COMPLETED = "Completed";
	
	protected final static String PAYMENT_STATUS = "payment_status";
	
	protected final static String CUSTOM = "custom";
	
	protected final static String ITEM_NAME = "item_name";
	
	@Autowired
	private PrincipalDetailsService principalDetailsService;
	
	@Autowired
	protected PaymentPlansLoader paymentPlans;

	@Transactional(readOnly = false)
	public void handle(String transactionType, Map<String, String> parametersMap){
		preProcess(parametersMap);
		if(!check(transactionType, parametersMap))
			return;
		Business business = principalDetailsService.loadUserByUsername(parametersMap.get(CUSTOM)).getBusiness();
		makePremium(business);
		extendNonFreeAccountExpirationTime(business,
				paymentPlans.getPayPalPaymentPlanDescriptor(parametersMap.get(ITEM_NAME)).getPayedPeriodInMonths());
		sendMessage(parametersMap.get(CUSTOM), "Account upgraded", new HashMap<String, Object>(), "mail-templates/upgrade-notification.vm");
		postProcess(parametersMap);
	}
	
	abstract protected boolean check(String transactionType, Map<String, String> parametersMap);
	
	abstract protected void extendNonFreeAccountExpirationTime(Business business, int numberOfMonths);
	
	protected void preProcess(Map<String, String> parametersMap){}
	
	protected void postProcess(Map<String, String> parametersMap){}
	
	protected void makePremium(Business business){
		for(Principal principal: business.getPrincipals()){
			if(!principal.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_PREMIUM)){
				principal.getGrantedRoles().remove(RoleType.ROLE_BUSINESS_FREE);
				principal.getGrantedRoles().add(RoleType.ROLE_BUSINESS_PREMIUM);
			}
		}
	}
	
}
