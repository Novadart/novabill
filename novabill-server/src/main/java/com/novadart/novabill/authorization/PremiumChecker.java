package com.novadart.novabill.authorization;

import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.shared.client.exception.FreeUserAccessErrorType;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;

public class PremiumChecker implements RestricionChecker {

	@Override
	public void check(Principal principal) throws FreeUserAccessForbiddenException {
		if(!principal.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_PREMIUM))
			throw new FreeUserAccessForbiddenException(FreeUserAccessErrorType.NOT_PREMIUM_USER);
	}

}
