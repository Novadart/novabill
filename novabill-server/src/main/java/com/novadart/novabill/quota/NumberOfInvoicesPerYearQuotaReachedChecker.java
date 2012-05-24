package com.novadart.novabill.quota;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.security.RoleTypes;
import com.novadart.novabill.shared.client.exception.QuotaException;

@Configurable
public class NumberOfInvoicesPerYearQuotaReachedChecker implements QuotaChecker {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NumberOfInvoicesPerYearQuotaReachedChecker.class);
	
	@Value("${quota.numberOfInvoicesPerYear}")
	private int numberOfInvoicesPerYearQuota;

	public int getNumberOfInvoicesPerYearQuota() {
		return numberOfInvoicesPerYearQuota;
	}

	@Override
	public void check(Business business) throws QuotaException {
		LOGGER.debug("Number of invoices per year quota check - quota: {}, roles: {}", new Object[]{numberOfInvoicesPerYearQuota, business.getGrantedRoles()});
		if(business.getGrantedRoles().contains(RoleTypes.ROLE_BUSINESS_FREE) && 
				business.getInvoicesForYear(Calendar.getInstance().get(Calendar.YEAR)).size() >= numberOfInvoicesPerYearQuota)
			throw new QuotaException();
	}

}
