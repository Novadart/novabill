package com.novadart.novabill.aspect;

import com.novadart.novabill.annotation.CheckQuotas;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.security.RoleTypes;
import com.novadart.novabill.quota.QuotaChecker;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.exception.OverQuotaException;

public aspect CheckQuotasAspect {
	
	UtilsService utilsService;
	
	public void setUtilsService(UtilsService utilsService) {
		this.utilsService = utilsService;
	}

	pointcut quotaRestrictedMethod(CheckQuotas checkQuotas):
		execution(@CheckQuotas * *(..)) && @annotation(checkQuotas);

	before(CheckQuotas checkQuotas) throws OverQuotaException : quotaRestrictedMethod(checkQuotas){
		Business authenticatedBusiness = utilsService.getAuthenticatedPrincipalDetails().getPrincipal();
//		if(authenticatedBusiness.getGrantedRoles().contains(RoleTypes.ROLE_BUSINESS_PREMIUM)) //premium business
//			return;
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
