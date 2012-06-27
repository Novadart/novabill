package com.novadart.novabill.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.annotation.MailMixin;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.security.RoleTypes;


@Service
@MailMixin
public class PayPalSubscriptionIPNHandlerService {
	
	public static final String SIGNUP = "subscr_signup";
	
	public static final String PAYMENT = "subscr_payment";
	
	private final static String COMPLETED = "Completed";
	
	private final static String PAYMENT_STATUS = "payment_status";
	
	private final static String RECEIVER_EMAIL = "receiver_email";
	
	private final static String CUSTOM = "custom";
	
	@Value("${paypal.email}")
	private String payPalEmail;
	
	@Autowired
	private PrincipalDetailsService principalDetailsService;
	
	public void handle(String transactionType, Map<String, String> parametersMap){
		if(transactionType.equals(PAYMENT)){
			handlePayment(parametersMap);
		}
	}
	
	@Transactional(readOnly = false)
	private void handlePayment(Map<String, String> parametersMap){
		if(!parametersMap.get(PAYMENT_STATUS).equals(COMPLETED)) return;//not completed ignore
		String email = parametersMap.get(RECEIVER_EMAIL);
		if(!email.equals(payPalEmail)) return; //email doesn't match
		//TODO do additional checks here eg amount
		Business business = principalDetailsService.loadUserByUsername(parametersMap.get(CUSTOM)).getPrincipal();
		if(!business.getGrantedRoles().contains(RoleTypes.ROLE_BUSINESS_PREMIUM)){
			business.getGrantedRoles().remove(RoleTypes.ROLE_BUSINESS_FREE);
			business.getGrantedRoles().add(RoleTypes.ROLE_BUSINESS_PREMIUM);
		}
		sendMessage(parametersMap.get(CUSTOM), "Account upgraded", new HashMap<String, Object>(), "mail-templates/upgrade-notification.vm");
	}

}
