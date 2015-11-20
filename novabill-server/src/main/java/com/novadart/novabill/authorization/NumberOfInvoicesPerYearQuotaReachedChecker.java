package com.novadart.novabill.authorization;

import java.util.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.domain.security.RoleType;
import com.novadart.novabill.service.web.BusinessService;
import com.novadart.novabill.shared.client.exception.FreeUserAccessErrorType;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;

@Configurable
public class NumberOfInvoicesPerYearQuotaReachedChecker implements RestricionChecker {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NumberOfInvoicesPerYearQuotaReachedChecker.class);
	
	@Autowired
	private BusinessService businessService;
	
	@Value("${quota.numberOfInvoicesPerYear}")
	private int numberOfInvoicesPerYearQuota;

	public int getNumberOfInvoicesPerYearQuota() {
		return numberOfInvoicesPerYearQuota;
	}

	@Override
	public void check(Principal principal) throws FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException {
		LOGGER.debug("Number of invoices per year quota check - quota: {}, roles: {}", new Object[]{numberOfInvoicesPerYearQuota, principal.getGrantedRoles()});
		if(principal.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_TRIAL) &&
				businessService.getInvoices(principal.getBusiness().getId(), Calendar.getInstance().get(Calendar.YEAR)).size() >= numberOfInvoicesPerYearQuota)
			throw new FreeUserAccessForbiddenException(FreeUserAccessErrorType.NUMBER_OF_INVOICES_QUOTA_REACHED);
	}

}
