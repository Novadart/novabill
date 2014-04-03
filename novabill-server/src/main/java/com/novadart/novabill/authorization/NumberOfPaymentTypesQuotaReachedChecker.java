package com.novadart.novabill.authorization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;

import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.service.web.BusinessService;
import com.novadart.novabill.shared.client.exception.AuthorizationError;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;

@Configurable
public class NumberOfPaymentTypesQuotaReachedChecker implements RestricionChecker {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NumberOfPaymentTypesQuotaReachedChecker.class);

	@Autowired
	private BusinessService businessService;
	
	@Value("${quota.numberOfPaymentTypes}")
	private int numberOfPaymentTypesQuota;

	public int getNumberOfPaymentTypesQuota() {
		return numberOfPaymentTypesQuota;
	}

	@Override
	public void check(Principal principal) throws AuthorizationException, NotAuthenticatedException, DataAccessException {
		LOGGER.debug("Number of payment types quota check - quota: {}, roles: {}", new Object[]{numberOfPaymentTypesQuota, principal.getGrantedRoles()});
		if(principal.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_FREE) &&
				businessService.getPaymentTypes(principal.getBusiness().getId()).size() >= numberOfPaymentTypesQuota)
			throw new AuthorizationException(AuthorizationError.NUMBER_OF_PAYMENTTYPES_QUOTA_REACHED);

	}

}
