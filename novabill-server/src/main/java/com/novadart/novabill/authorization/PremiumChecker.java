package com.novadart.novabill.authorization;

import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.shared.client.exception.AuthorizationError;
import com.novadart.novabill.shared.client.exception.AuthorizationException;

public class PremiumChecker implements RestricionChecker {

	@Override
	public void check(Principal principal) throws AuthorizationException {
		if(!principal.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_PREMIUM))
			throw new AuthorizationException(AuthorizationError.NOT_PREMIUM_USER);
	}

}
