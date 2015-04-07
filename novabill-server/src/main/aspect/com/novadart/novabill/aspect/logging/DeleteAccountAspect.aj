package com.novadart.novabill.aspect.logging;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;

import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.web.mvc.command.DeleteAccount;

public aspect DeleteAccountAspect extends AbstractLogEventEmailSenderAspect {
	
	@Autowired
	private UtilsService utilsService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteAccountAspect.class);
	
	pointcut deleteAccount(DeleteAccount deleteAccount, BindingResult result) :
		execution(public String com.novadart.novabill.web.mvc.DeleteAccountController.processSubmit(..)) && args(deleteAccount, result, ..);
	
	after (DeleteAccount deleteAccount, BindingResult result) returning : deleteAccount(deleteAccount, result) {
		if(!result.hasErrors())
			handleEvent(LOGGER, "Delete account", utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(System.currentTimeMillis()), null);
	}

}
