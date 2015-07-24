package com.novadart.novabill.aspect.logging;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.novadart.novabill.service.UtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.novadart.novabill.shared.client.exception.ClientUIException;

public aspect ClientUIAspect extends AbstractLogEventEmailSenderAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientUIAspect.class);
	
	@Autowired
	private UtilsService utilsService;
	
	pointcut postClientUIError():
		call(public void com.novadart.novabill.web.mvc.ajax.ClientUIErrorController.postError(..));
	
	after() throwing(ClientUIException ex): postClientUIError() {
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("error", ex.getErrorMessage());
		handleEvent(LOGGER, "Client UI Error", utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(System.currentTimeMillis()), vars);
	}

}
