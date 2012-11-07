package com.novadart.novabill.authorization;

import java.util.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.shared.client.exception.AuthorizationError;
import com.novadart.novabill.shared.client.exception.AuthorizationException;

@Configurable
public class NumberOfInvoicesPerYearQuotaReachedChecker implements RestricionChecker {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NumberOfInvoicesPerYearQuotaReachedChecker.class);
	
	@Value("${quota.numberOfInvoicesPerYear}")
	private int numberOfInvoicesPerYearQuota;

	public int getNumberOfInvoicesPerYearQuota() {
		return numberOfInvoicesPerYearQuota;
	}

	@Override
	public void check(Principal principal) throws AuthorizationException {
		LOGGER.debug("Number of invoices per year quota check - quota: {}, roles: {}", new Object[]{numberOfInvoicesPerYearQuota, principal.getGrantedRoles()});
		if(principal.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_FREE) && 
				principal.getBusiness().getInvoicesForYear(Calendar.getInstance().get(Calendar.YEAR)).size() >= numberOfInvoicesPerYearQuota)
			throw new AuthorizationException(AuthorizationError.NUMBER_OF_INVOICES_QUOTA_REACHED);
	}

}
