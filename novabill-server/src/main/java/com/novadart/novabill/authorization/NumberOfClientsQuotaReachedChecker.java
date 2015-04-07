package com.novadart.novabill.authorization;

import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.shared.client.exception.FreeUserAccessErrorType;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;

@Configurable
public class NumberOfClientsQuotaReachedChecker implements RestricionChecker {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NumberOfClientsQuotaReachedChecker.class);

	@Value("${quota.numberOfClients}")
	private int numberOfClientsQuota;
	
	public int getNumberOfClientsQuota() {
		return numberOfClientsQuota;
	}

	@Override
	public void check(Principal principal) throws FreeUserAccessForbiddenException {
		LOGGER.debug("Number of clients quota check - quota: {}, roles: {}", new Object[]{numberOfClientsQuota, principal.getGrantedRoles()});
		if(principal.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_FREE) && principal.getBusiness().getClients().size() >= numberOfClientsQuota)
			throw new FreeUserAccessForbiddenException(FreeUserAccessErrorType.NUMBER_OF_CLIENTS_QUOTA_REACHED);
	}

}
