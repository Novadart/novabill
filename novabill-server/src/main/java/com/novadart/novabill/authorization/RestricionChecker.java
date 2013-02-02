package com.novadart.novabill.authorization;

import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.shared.client.exception.AuthorizationException;

public interface RestricionChecker {
	
	public void check(Principal business) throws AuthorizationException;

}
