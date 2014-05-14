package com.novadart.novabill.aspect.logging;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.novadart.novabill.shared.client.exception.ValidationException;

public aspect ValidationErrorAspect extends AbstractLogEventEmailSenderAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ValidationErrorAspect.class);
	
	private ThreadLocal<Throwable> lastLoggedException = new ThreadLocal<Throwable>();
	
	pointcut validationError():
		execution(public * com.novadart.novabill.service.validator..*(..));
	
	after() throwing(ValidationException ex): validationError(){
		if(lastLoggedException.get() != ex){
			lastLoggedException.set(ex);
			Map<String, Object> vars = new HashMap<String, Object>();
			vars.put("object", ex.getObjectRepr());
			vars.put("errors", String.format("{%s}", StringUtils.join(ex.getErrors(), ", ")));
			handleEvent(LOGGER, "JSR-303 validation error", "N/A", new Date(System.currentTimeMillis()), vars);
		}
	}

}
