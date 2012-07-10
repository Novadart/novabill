package com.novadart.novabill.authorization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.shared.client.exception.AuthorizationError;
import com.novadart.novabill.shared.client.exception.AuthorizationException;

@Configurable
public class NumberOfClientsQuotaReachedChecker implements RestricionChecker {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NumberOfClientsQuotaReachedChecker.class);

	@Value("${quota.numberOfClients}")
	private int numberOfClientsQuota;
	
	public int getNumberOfClientsQuota() {
		return numberOfClientsQuota;
	}

	@Override
	public void check(Business business) throws AuthorizationException {
		LOGGER.debug("Number of clients quota check - quota: {}, roles: {}", new Object[]{numberOfClientsQuota, business.getGrantedRoles()});
		if(business.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_FREE) && business.getClients().size() >= numberOfClientsQuota)
			throw new AuthorizationException(AuthorizationError.NUMBER_OF_CLIENTS_QUOTA_REACHED);
	}

}
