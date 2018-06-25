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
public class NumberOfEstimationsPerYearQuotaReachedChecker implements RestricionChecker {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NumberOfEstimationsPerYearQuotaReachedChecker.class);
	
	@Value("${quota.numberOfEstimationsPerYear}")
	private int numberOfEstimationsPerYearQuota;

	public int getNumberOfEstimationsPerYearQuota() {
		return numberOfEstimationsPerYearQuota;
	}

	@Override
	public void check(Principal principal) throws FreeUserAccessForbiddenException {
		LOGGER.debug("Number of estimations per year quota check - quota: {}, roles: {}", new Object[]{numberOfEstimationsPerYearQuota, principal.getGrantedRoles()});
		if(principal.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_TRIAL) &&
				principal.getBusiness().getEstimationsForYear(Calendar.getInstance().get(Calendar.YEAR)).size() >= numberOfEstimationsPerYearQuota)
			throw new FreeUserAccessForbiddenException(FreeUserAccessErrorType.NUMBER_OF_ESTIMATIONS_QUOTA_REACHED);
	}

}
