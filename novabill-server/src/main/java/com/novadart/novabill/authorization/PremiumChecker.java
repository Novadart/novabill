package com.novadart.novabill.authorization;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.shared.client.exception.AuthorizationError;
import com.novadart.novabill.shared.client.exception.AuthorizationException;

public class PremiumChecker implements RestricionChecker {

	@Override
	public void check(Business business) throws AuthorizationException {
		if(!business.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_PREMIUM))
			throw new AuthorizationException(AuthorizationError.NOT_PREMIUM_USER);
	}

}
