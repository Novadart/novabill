package com.novadart.novabill.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.novadart.novabill.annotation.Restrictions;
import com.novadart.novabill.authorization.RestricionChecker;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;

privileged aspect CheckRestrictionsAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CheckRestrictionsAspect.class);
	
	@Autowired
	private UtilsService utilsService;
	
	pointcut quotaRestrictedMethod(Restrictions checkQuotas):
		execution(@Restrictions * *(..)) && @annotation(checkQuotas);

	@Transactional(readOnly = true)
	before(Restrictions checkQuotas) throws FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException : quotaRestrictedMethod(checkQuotas){
		LOGGER.debug("Checking quotas for method {}", new Object[]{thisJoinPoint.getSignature().toShortString()});
		Principal principal = Principal.findPrincipal(utilsService.getAuthenticatedPrincipalDetails().getId());
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
