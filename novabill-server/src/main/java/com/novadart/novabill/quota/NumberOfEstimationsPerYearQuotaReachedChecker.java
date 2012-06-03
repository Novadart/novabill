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
public class NumberOfEstimationsPerYearQuotaReachedChecker implements QuotaChecker {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NumberOfEstimationsPerYearQuotaReachedChecker.class);
	
	@Value("${quota.numberOfEstimationsPerYear}")
	private int numberOfEstimationsPerYearQuota;

	public int getNumberOfEstimationsPerYearQuota() {
		return numberOfEstimationsPerYearQuota;
	}

	@Override
	public void check(Business business) throws QuotaException {
		LOGGER.debug("Number of estimations per year quota check - quota: {}, roles: {}", new Object[]{numberOfEstimationsPerYearQuota, business.getGrantedRoles()});
		if(business.getGrantedRoles().contains(RoleTypes.ROLE_BUSINESS_FREE) && 
				business.getEstimationsForYear(Calendar.getInstance().get(Calendar.YEAR)).size() >= numberOfEstimationsPerYearQuota)
			throw new QuotaException();
	}

}
