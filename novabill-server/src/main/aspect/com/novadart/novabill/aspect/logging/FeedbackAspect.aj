package com.novadart.novabill.aspect.logging;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.novadart.novabill.service.UtilsService;

public privileged aspect FeedbackAspect extends AbstractLogEventEmailSenderAspect {
	
	@Autowired
	private UtilsService utilsService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackAspect.class);
	
	pointcut feedback(String name, String email, String category, String message) :
		execution(public String com.novadart.novabill.web.mvc.FeedbackController.processSubmit(..)) && args(name, email, category, message);
	
	after(String name, String email, String category, String message) returning: feedback(name, email, category, message){
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("name", name);
		vars.put("category", category);
		vars.put("message", message);
		handleEvent(LOGGER, "Feedback", utilsService.getAuthenticatedPrincipalDetails().getUsername(), new Date(System.currentTimeMillis()), vars);
	}

}
