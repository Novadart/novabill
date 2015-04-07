package com.novadart.novabill.springsecurity;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	public AuthenticationSuccessHandler() {
		super();
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
		boolean isGWTRequest = request.getParameter("gwt") != null;
		if(isGWTRequest)
			response.sendError(HttpServletResponse.SC_OK);
		else
			super.onAuthenticationSuccess(request, response, authentication);
	}

}
