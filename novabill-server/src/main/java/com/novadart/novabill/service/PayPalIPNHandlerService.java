package com.novadart.novabill.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.novadart.novabill.annotation.MailMixin;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.security.RoleType;

@MailMixin
public abstract class PayPalIPNHandlerService {
	
	protected final static String COMPLETED = "Completed";
	
	protected final static String PAYMENT_STATUS = "payment_status";
	
	protected final static String CUSTOM = "custom";
	
	@Autowired
	private PrincipalDetailsService principalDetailsService;

	@Transactional(readOnly = false)
	public void handle(String transactionType, Map<String, String> parametersMap){
		preProcess(parametersMap);
		if(!check(transactionType, parametersMap))
			return;
		Business business = principalDetailsService.loadUserByUsername(parametersMap.get(CUSTOM)).getPrincipal();
		makePremium(business);
		extendNonFreeAccountExpirationTime(business, parametersMap);
		sendMessage(parametersMap.get(CUSTOM), "Account upgraded", new HashMap<String, Object>(), "mail-templates/upgrade-notification.vm");
		postProcess(parametersMap);
	}
	
	abstract protected boolean check(String transactionType, Map<String, String> parametersMap);
	
	abstract protected void extendNonFreeAccountExpirationTime(Business business, Map<String, String> parametersMap);
	
	protected void preProcess(Map<String, String> parametersMap){}
	
	protected void postProcess(Map<String, String> parametersMap){}
	
	protected void makePremium(Business business){
		if(!business.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_PREMIUM)){
			business.getGrantedRoles().remove(RoleType.ROLE_BUSINESS_FREE);
			business.getGrantedRoles().add(RoleType.ROLE_BUSINESS_PREMIUM);
		}
	}
	
}