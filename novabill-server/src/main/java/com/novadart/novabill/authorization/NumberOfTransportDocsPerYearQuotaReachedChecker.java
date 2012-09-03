package com.novadart.novabill.authorization;

import java.util.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.shared.client.exception.AuthorizationError;
import com.novadart.novabill.shared.client.exception.AuthorizationException;

public class NumberOfTransportDocsPerYearQuotaReachedChecker implements RestricionChecker {

	private static final Logger LOGGER = LoggerFactory.getLogger(NumberOfTransportDocsPerYearQuotaReachedChecker.class);
	
	@Value("${quota.numberOfTransportDocsPerYear}")
	private int numberOfTransportDocsPerYearQuota;

	public int getNumberOfTransportDocsPerYearQuota() {
		return numberOfTransportDocsPerYearQuota;
	}

	@Override
	public void check(Business business) throws AuthorizationException {
		LOGGER.debug("Number of transport documents per year quota check - quota: {}, roles: {}", new Object[]{numberOfTransportDocsPerYearQuota, business.getGrantedRoles()});
		if(business.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_FREE) && 
				business.getTransportDocsForYear(Calendar.getInstance().get(Calendar.YEAR)).size() >= numberOfTransportDocsPerYearQuota)
			throw new AuthorizationException(AuthorizationError.NUMBER_OF_TRANSPORT_DOCUMENTS_QUOTA_REACHED);
	}

}
