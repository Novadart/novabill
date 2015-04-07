package com.novadart.novabill.springsecurity;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	public AuthenticationFailureHandler() {
		super();
	}

	public AuthenticationFailureHandler(String defaultFailureUrl) {
		super(defaultFailureUrl);
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		boolean isGWTRequest = request.getParameter("gwt") != null;
		if(isGWTRequest)
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		else
			super.onAuthenticationFailure(request, response, exception);
	}
	
	

}
