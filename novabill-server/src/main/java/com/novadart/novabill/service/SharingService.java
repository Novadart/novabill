package com.novadart.novabill.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.novadart.novabill.annotation.MailMixin;
import com.novadart.novabill.domain.SharingPermit;


@Service
@MailMixin
public class SharingService {
	
	private static final String EMAIL_TEMPLATE_LOCATION = "mail-templates/sharing-notification.vm";
	
	@Value("${sharing.url.pattern}")
	private String sharingUrlPattern;
	
	@Value("${sharing.expiration}")
	private Long sharingExpiration;
	
	@Autowired
	private TokenGenerator tokenGenerator;
	
	public void issueSharingPermitTemporarilyAndNotifyParticipant(Long businessID, String email, MessageSource messageSource, Locale locale){
		try {
			String token = tokenGenerator.generateToken();
			new SharingPermit(token, email, businessID).persist();
			Map<String, Object> templateVars = new HashMap<String, Object>();
			String sharedInvoicesLink = String.format(sharingUrlPattern, businessID, URLEncoder.encode(token, "UTF-8"));
			templateVars.put("shareLink", sharedInvoicesLink);
			templateVars.put("sharingExpiration", sharingExpiration);
			sendMessage(email, messageSource.getMessage("sharing.notification", null, locale), templateVars, EMAIL_TEMPLATE_LOCATION);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Token generation failed");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported encoding UTF-8");
		}
	}

	public boolean isValidRequest(Long businessID, String token){
		for(SharingPermit permit: SharingPermit.findSharingPermits(businessID, token))
			if(3_600_000l * sharingExpiration + permit.getCreatedOn() > System.currentTimeMillis())
				return true;
		return false;
	}
	
}
