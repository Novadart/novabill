package com.novadart.novabill.springsecurity;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.service.UtilsService;

public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	@Autowired
	private UtilsService utilsService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationSuccessHandler.class);

	public AuthenticationSuccessHandler() {
		super();
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
		boolean isGWTRequest = request.getParameter("gwt") != null;
		Business business = utilsService.getAuthenticatedPrincipalDetails().getPrincipal();
		business.setLastLogin(System.currentTimeMillis());
		business.merge();
		LOGGER.info("[{}, login, {}]", new Object[]{business.getEmail(), new Date(business.getLastLogin())});
		if(isGWTRequest)
			response.sendError(HttpServletResponse.SC_OK);
		else
			super.onAuthenticationSuccess(request, response, authentication);
	}

}
