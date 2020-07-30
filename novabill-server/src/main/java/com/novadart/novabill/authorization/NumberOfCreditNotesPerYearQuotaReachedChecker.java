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
public class NumberOfCreditNotesPerYearQuotaReachedChecker implements RestricionChecker {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NumberOfCreditNotesPerYearQuotaReachedChecker.class);
	
	@Value("${quota.numberOfCreditNotesPerYear}")
	private int numberOfCreditNotesPerYearQuota;

	public int getNumberOfInvoicesPerYearQuota() {
		return numberOfCreditNotesPerYearQuota;
	}

	@Override
	public void check(Principal principal) throws FreeUserAccessForbiddenException {
		LOGGER.debug("Number of credit notes per year quota check - quota: {}, roles: {}", new Object[]{numberOfCreditNotesPerYearQuota, principal.getGrantedRoles()});
		if(principal.getGrantedRoles().contains(RoleType.ROLE_BUSINESS_TRIAL) &&
				principal.getBusiness().getCreditNotesForYear(Calendar.getInstance().get(Calendar.YEAR)).size() >= numberOfCreditNotesPerYearQuota)
			throw new FreeUserAccessForbiddenException(FreeUserAccessErrorType.NUMBER_OF_CREDIT_NOTES_QUOTA_REACHED);
	}

}
