package com.novadart.novabill.service;

import com.novadart.novabill.annotation.Restrictions;
import com.novadart.novabill.authorization.PremiumChecker;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.SharingToken;
import com.novadart.novabill.service.mail.EmailBuilder;
import com.novadart.novabill.service.mail.MailHandlingType;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;


@Service
public class SharingService {
	
	private static final String EMAIL_TEMPLATE_LOCATION = "mail-templates/temp-sharing-notification.vm";
	
	@Value("${sharing.url.pattern}")
	private String sharingUrlPattern;
	
	@Value("${sharing.expiration}")
	private Long sharingExpiration;
	
	@Autowired
	private TokenGenerator tokenGenerator;
	
	@Autowired
	private UtilsService utilsService;
	
	@Restrictions(checkers = {PremiumChecker.class}, businessParamName = "business")
	public void enableSharingTemporarilyAndNotifyParticipant(Business business, String email, MessageSource messageSource, Locale locale)
			throws DataAccessException, FreeUserAccessForbiddenException, NotAuthenticatedException{
		try {
			String token = tokenGenerator.generateToken();
			new SharingToken(email, business.getId(), token).persist();
			String shareLink = String.format(sharingUrlPattern, business.getId(), URLEncoder.encode(token, "UTF-8"));
			new EmailBuilder().to(email)
					.subject(messageSource.getMessage("temp.sharing.notification", null, locale))
					.template(EMAIL_TEMPLATE_LOCATION)
					.templateVar("businessName", business.getName())
					.templateVar("shareLink", shareLink)
					.templateVar("sharingExpiration", sharingExpiration)
					.handlingType(MailHandlingType.EXTERNAL_UNACKNOWLEDGED)
					.build().send();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Token generation failed");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported encoding UTF-8");
		}
	}

	public boolean isValidRequest(Long businessID, String token){
		if(utilsService.isAuthenticated() && utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId().equals(businessID))
			return true; //user has access right to his own data provided he's authenticated
		SharingToken sharingToken = SharingToken.findSharingToken(businessID, token);
		return sharingToken == null? false: 3_600_000l * sharingExpiration + sharingToken.getCreatedOn() > System.currentTimeMillis();
	}
	
}
