package com.novadart.novabill.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.annotation.MailMixin;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.security.RoleTypes;
import com.novadart.novabill.web.mvc.PayPalIPNListenerController.NameValuePair;


@Service
@MailMixin
public class PayPalSubscriptionIPNHandlerService {
	
	public static final String SIGNUP = "subscr_signup";
	
	private final static String COMPLETED = "Completed";
	
	private final static String PAYMENT_STATUS = "payment_status";
	
	private final static String RECEIVER_EMAIL = "receiver_email";
	
	private final static String CUSTOM = "custom";
	
	@Value("${paypal.email}")
	private String payPalEmail;
	
	@Autowired
	private PrincipalDetailsService principalDetailsService;
	
	private Map<String, String> createParametersMap(List<NameValuePair> parameters){
		Map<String, String> map = new HashMap<String, String>();
		for(NameValuePair pair: parameters)
			map.put(pair.getName(), pair.getValue());
		return map;
	}
	
	public void handle(String transactionType, List<NameValuePair> parameters){
		Map<String, String> parametersMap = createParametersMap(parameters);
		if(transactionType.equals(SIGNUP))
			handleSignup(parametersMap);
	}
	
	@Transactional(readOnly = false)
	private void handleSignup(Map<String, String> parametersMap){
		if(!parametersMap.get(PAYMENT_STATUS).equals(COMPLETED)) return;//not completed ignore
		String email = parametersMap.get(RECEIVER_EMAIL); 
		if(!email.equals(payPalEmail)) return; //email doesn't match
		//TODO do additional checks here eg amount
		Business business = principalDetailsService.loadUserByUsername(parametersMap.get(CUSTOM)).getPrincipal();
		business.getGrantedRoles().remove(RoleTypes.ROLE_BUSINESS_FREE);
		business.getGrantedRoles().add(RoleTypes.ROLE_BUSINESS_PREMIUM);
		sendMessage(parametersMap.get(CUSTOM), "Account upgraded", new HashMap<String, Object>(), "mail-templates/upgrade-notification.vm");
	}

}
