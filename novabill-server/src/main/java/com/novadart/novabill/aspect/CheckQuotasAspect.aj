package com.novadart.novabill.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.annotation.CheckQuotas;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.quota.QuotaChecker;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.exception.QuotaException;

privileged aspect CheckQuotasAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CheckQuotasAspect.class);
	
	@Autowired
	private UtilsService utilsService;
	
	pointcut quotaRestrictedMethod(CheckQuotas checkQuotas):
		execution(@CheckQuotas * *(..)) && @annotation(checkQuotas);

	@Transactional(readOnly = true)
	before(CheckQuotas checkQuotas) throws QuotaException : quotaRestrictedMethod(checkQuotas){
		LOGGER.debug("Checking quotas for method {}", new Object[]{thisJoinPoint.getSignature().toShortString()});
		Business authenticatedBusiness = Business.findBusiness(utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getId());
		for(Class<? extends QuotaChecker> checkerClass: checkQuotas.checkers())
			try {
				checkerClass.newInstance().check(authenticatedBusiness);
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
	}
	
}
