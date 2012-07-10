package com.novadart.novabill.authorization;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.shared.client.exception.AuthorizationException;

public interface RestricionChecker {
	
	public void check(Business business) throws AuthorizationException;

}
