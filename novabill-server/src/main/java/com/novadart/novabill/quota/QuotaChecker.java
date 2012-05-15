package com.novadart.novabill.quota;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.shared.client.exception.OverQuotaException;

public interface QuotaChecker {
	
	public void check(Business business) throws OverQuotaException;

}
