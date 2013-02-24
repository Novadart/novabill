package com.novadart.novabill.aspect.logging;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.novadart.novabill.shared.client.exception.ValidationException;

public aspect ValidationAspect extends AbstractLogEventEmailSenderAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ValidationAspect.class);
	
	pointcut simpleValidationError():
		execution(public void com.novadart.novabill.service.validator.SimpleValidator.validate(..));
	
	after() throwing(ValidationException ex): simpleValidationError(){
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("type", ex.getObjectClassName());
		vars.put("errors", String.format("{%s}", StringUtils.join(ex.getErrors(), ", ")));
		handleEvent(LOGGER, "JSR-303 validation error", "N/A", new Date(System.currentTimeMillis()), vars);
	}

}
