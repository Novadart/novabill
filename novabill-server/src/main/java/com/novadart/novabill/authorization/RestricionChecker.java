package com.novadart.novabill.authorization;

import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;

public interface RestricionChecker {
	
	public void check(Principal principal) throws FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException;

}
