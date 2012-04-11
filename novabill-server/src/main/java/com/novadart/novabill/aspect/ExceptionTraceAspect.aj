package com.novadart.novabill.aspect;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.springframework.beans.factory.annotation.Autowired;
import com.novadart.novabill.service.UtilsService;

public aspect ExceptionTraceAspect {
	
	private Logger logger = Logger.getLogger("exceptions");
	
	private ThreadLocal<Throwable> lastLoggedException = new ThreadLocal<Throwable>();
	
	@Autowired
	private UtilsService utilsService;
	
	pointcut exceptionTraced(): execution(* *.*(..)) && within(com.novadart.novabill..*) && !within(ExceptionTraceAspect);
	
	private String getArgsMesssage(JoinPoint joinPoint){
		Object[] args = joinPoint.getArgs();
		String[] argsStrs = new String[args.length];
		for(int i = 0; i < argsStrs.length; ++i)
			argsStrs[i] = args[i].toString();
		return StringUtils.join(argsStrs, ',');
	}
	
	after() throwing(Throwable ex): exceptionTraced(){
		if(lastLoggedException.get() != ex){
			lastLoggedException.set(ex);
			Signature signature = thisJoinPointStaticPart.getSignature();
			String principal = utilsService.isAuthenticated()? utilsService.getAuthenticatedPrincipalDetails().getUsername(): "anonymous";
			String message = String.format("principal: %s, method: %s, args: [%s]", principal, signature.toShortString(), getArgsMesssage(thisJoinPoint));
			logger.log(Level.ERROR, message, ex);
		}
	}

}
