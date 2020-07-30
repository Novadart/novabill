package com.novadart.novabill.aspect;

import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.novadart.novabill.annotation.Restrictions;
import com.novadart.novabill.authorization.RestricionChecker;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;

privileged aspect CheckRestrictionsAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CheckRestrictionsAspect.class);
	
	@Autowired
	private UtilsService utilsService;
	
	pointcut restrictedMethod(Restrictions restrictions):
		execution(@Restrictions * *(..)) && @annotation(restrictions);

	private Principal getPrincipal(MethodSignature signature, String businessParamName, Object[] parameterValues){
		String[] parameterNames = signature.getParameterNames();
		for(int i = 0; i < parameterNames.length; ++i)
			if(parameterNames[i].equals(businessParamName))
				return ((Business)parameterValues[i]).getPrincipals().iterator().next();
		return null;
	}
	
	@Transactional(readOnly = true)
	before(Restrictions restrictions) throws FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException : restrictedMethod(restrictions) {
		MethodSignature signature = (MethodSignature)thisJoinPoint.getSignature();
		LOGGER.debug("Checking restrictions for method {}", new Object[]{signature.toShortString()});
		Principal principal = null;
		if(utilsService.isAuthenticated())
			principal = Principal.findPrincipal(utilsService.getAuthenticatedPrincipalDetails().getId());
		else if(!restrictions.equals("[unassigned]"))
			principal = getPrincipal(signature, restrictions.businessParamName(), thisJoinPoint.getArgs());
		if(principal == null)
			throw new NotAuthenticatedException();
		for(Class<? extends RestricionChecker> checkerClass: restrictions.checkers())
			try {
				checkerClass.newInstance().check(principal);
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
	}
	
}
