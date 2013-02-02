package com.novadart.novabill.aspect.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.novadart.novabill.service.UtilsService;

public aspect ExceptionTraceAspect extends AbstractLogEventEmailSenderAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionTraceAspect.class);
	
	private ThreadLocal<Throwable> lastLoggedException = new ThreadLocal<Throwable>();
	
	@Autowired
	private UtilsService utilsService;
	
	private boolean loggingOn;
	
	public boolean isLoggingOn() {
		return loggingOn;
	}

	public void setLoggingOn(boolean turnOnLogging) {
		this.loggingOn = turnOnLogging;
	}

	pointcut exceptionTraced(): execution(* *.*(..)) && within(com.novadart.novabill..*) && !within(ExceptionTraceAspect) && !within(com.novadart.novabill.test..*);
	
	private String getArgsMesssage(JoinPoint joinPoint){
		Object[] args = joinPoint.getArgs();
		String[] argsStrs = new String[args.length];
		for(int i = 0; i < argsStrs.length; ++i)
			argsStrs[i] = args[i].toString();
		return StringUtils.join(argsStrs, ',');
	}
	
	private String fetchAndFormatForWeb(Throwable ex){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		return sw.toString().replace(System.getProperty("line.separator"), "<br/>\n");
	}
	
	after() throwing(Throwable ex): exceptionTraced(){
		if(loggingOn && lastLoggedException.get() != ex){
			lastLoggedException.set(ex);
			Signature signature = thisJoinPointStaticPart.getSignature();
			String principal = utilsService.isAuthenticated()? utilsService.getAuthenticatedPrincipalDetails().getUsername(): "anonymous";
			String message = String.format("principal: %s, method: %s, args: [%s]", principal, signature.toShortString(), getArgsMesssage(thisJoinPoint));
			LOGGER.error(message, ex);
			Map<String, Object> templateVars = new HashMap<String, Object>();
			templateVars.put("message", message);
			templateVars.put("stackTrace", fetchAndFormatForWeb(ex));
			sendEmailMessage("Exception", principal, new Date(), templateVars, "mail-templates/exception-notification.vm");
		}
	}

}
