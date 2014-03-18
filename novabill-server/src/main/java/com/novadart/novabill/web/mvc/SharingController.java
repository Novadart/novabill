package com.novadart.novabill.web.mvc;

import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novadart.novabill.annotation.Xsrf;
import com.novadart.novabill.service.SharingService;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.XsrfTokenService;

@Controller
public class SharingController {

	public static final String TOKEN_REQUEST_PARAM = "token";
	
	@Autowired
	private XsrfTokenService xsrfTokenService;
	
	@Autowired
	private SharingService sharingService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private UtilsService utilsService;
	
	@ResponseBody
	@RequestMapping(value = "/private/share/token", method = RequestMethod.GET)
	public String getToken(HttpSession session) throws NoSuchAlgorithmException{
		return xsrfTokenService.generateToken(session, XsrfTokenSessionFieldNames.SHARING_TOKENS_SESSION_FIELD);
	}
	
	@ResponseBody
	@RequestMapping(value = "/private/share/{email}", method = RequestMethod.POST)
	@Xsrf(tokenRequestParam = TOKEN_REQUEST_PARAM, tokensSessionField = XsrfTokenSessionFieldNames.SHARING_TOKENS_SESSION_FIELD)
	public void issueSharingPermitTemporarily(@PathVariable String email, Locale locale, HttpSession session){
		sharingService.issueSharingPermitTemporarilyAndNotifyParticipant(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(), email, messageSource, locale);
	}
	
	@RequestMapping(value = "/share/{businessID}/{token}")
	public String share(@PathVariable Long businessID, @PathVariable String token){
		if(sharingService.isValidRequest(businessID, token)){
			return "share";
		}else
			return "invalidSharingRequest";
	}
	
}
