package com.novadart.novabill.springsecurity;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.web.mvc.Urls;

@Service("csrfSecurityRequestMatcher")
public class CsrfSecurityRequestMatcher implements RequestMatcher {

	private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
	
	@Autowired
	private UtilsService utilsService;
	
	@Override
	public boolean matches(HttpServletRequest request) {
		if(allowedMethods.matcher(request.getMethod()).matches())
			return false;
		if(utilsService.isGWTRPCCall(request))
			return false;
		if(request.getServletPath().equals(Urls.PUBLIC_PAYPAL_IPN_LISTENER))
			return false;
		return true;
	}

}
