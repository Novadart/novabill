package com.novadart.novabill.quota;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.security.RoleTypes;
import com.novadart.novabill.shared.client.exception.QuotaException;

@Configurable
public class NumberOfClientsQuotaReachedChecker implements QuotaChecker {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NumberOfClientsQuotaReachedChecker.class);

	@Value("${quota.numberOfClients}")
	private int numberOfClientsQuota;
	
	public int getNumberOfClientsQuota() {
		return numberOfClientsQuota;
	}

	@Override
	public void check(Business business) throws QuotaException {
		LOGGER.debug("Number of clients quota check - quota: {}, roles: {}", new Object[]{numberOfClientsQuota, business.getGrantedRoles()});
		if(business.getGrantedRoles().contains(RoleTypes.ROLE_BUSINESS_FREE) && business.getClients().size() >= numberOfClientsQuota)
			throw new QuotaException();
	}

}
