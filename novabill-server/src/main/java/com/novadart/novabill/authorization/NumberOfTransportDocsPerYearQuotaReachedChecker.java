package com.novadart.novabill.authorization;

import java.util.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.shared.client.exception.FreeUserAccessErrorType;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;

@Configurable
public class NumberOfTransportDocsPerYearQuotaReachedChecker implements RestricionChecker {

	private static final Logger LOGGER = LoggerFactory.getLogger(NumberOfTransportDocsPerYearQuotaReachedChecker.class);
	
	@Value("${quota.numberOfTransportDocsPerYear}")
	private int numberOfTransportDocsPerYearQuota;

	public int getNumberOfTransportDocsPerYearQuota() {
		return numberOfTransportDocsPerYearQuota;
	}

	@Override
	public void check(Principal principal) throws FreeUserAccessForbiddenException {
		LOGGER.debug("Number of transport documents per year quota check - quota: {}, roles: {}", new Object[]{numberOfTransportDocsPerYearQuota, principal.getGrantedRoles()});
		if(principal.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_TRIAL) &&
				principal.getBusiness().getTransportDocsForYear(Calendar.getInstance().get(Calendar.YEAR)).size() >= numberOfTransportDocsPerYearQuota)
			throw new FreeUserAccessForbiddenException(FreeUserAccessErrorType.NUMBER_OF_TRANSPORT_DOCUMENTS_QUOTA_REACHED);
	}

}
