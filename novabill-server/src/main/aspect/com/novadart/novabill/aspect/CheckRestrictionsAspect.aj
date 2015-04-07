package com.novadart.novabill.aspect;

import com.novadart.novabill.annotation.Restrictions;
import com.novadart.novabill.authorization.RestricionChecker;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

privileged aspect CheckRestrictionsAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CheckRestrictionsAspect.class);
	
	@Autowired
	private UtilsService utilsService;
	
	pointcut quotaRestrictedMethod(Restrictions checkQuotas):
		execution(@Restrictions * *(..)) && @annotation(checkQuotas);

	private Principal getPrincipal(MethodSignature signature, String businessParamName, Object[] parameterValues){
		String[] parameterNames = signature.getParameterNames();
		for(int i = 0; i < parameterNames.length; ++i)
			if(parameterNames[i].equals(businessParamName))
				return ((Business)parameterValues[i]).getPrincipals().iterator().next();
		return null;
	}
	
	@Transactional(readOnly = true)
	before(Restrictions checkQuotas) throws FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException : quotaRestrictedMethod(checkQuotas) {
		MethodSignature signature = (MethodSignature)thisJoinPoint.getSignature();
		LOGGER.debug("Checking quotas for method {}", new Object[]{signature.toShortString()});
		Principal principal = null;
		if(utilsService.isAuthenticated())
			principal = Principal.findPrincipal(utilsService.getAuthenticatedPrincipalDetails().getId());
		else if(!checkQuotas.equals("[unassigned]"))
			principal = getPrincipal(signature, checkQuotas.businessParamName(), thisJoinPoint.getArgs());
		if(principal == null)
			throw new NotAuthenticatedException();
		for(Class<? extends RestricionChecker> checkerClass: checkQuotas.checkers())
			try {
				checkerClass.newInstance().check(principal);
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
	}
	
}
