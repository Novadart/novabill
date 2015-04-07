package com.novadart.novabill.authorization;

import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.service.web.BusinessService;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessErrorType;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;

@Configurable
public class NumberOfCommoditiesQuotaReachedChecker implements RestricionChecker {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NumberOfCommoditiesQuotaReachedChecker.class);

	@Autowired
	private BusinessService businessService;
	
	@Value("${quota.numberOfCommodities}")
	private int numberOfCommoditiesQuota;

	public int getNumberOfCommoditiesQuota() {
		return numberOfCommoditiesQuota;
	}

	@Override
	public void check(Principal principal) throws FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException {
		LOGGER.debug("Number of commodities quota check - quota: {}, roles: {}", new Object[]{numberOfCommoditiesQuota, principal.getGrantedRoles()});
		if(principal.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_FREE) &&
				businessService.getCommodities(principal.getBusiness().getId()).size() >= numberOfCommoditiesQuota)
			throw new FreeUserAccessForbiddenException(FreeUserAccessErrorType.NUMBER_OF_COMMODITIES_QUOTA_REACHED);
	}
		
}
