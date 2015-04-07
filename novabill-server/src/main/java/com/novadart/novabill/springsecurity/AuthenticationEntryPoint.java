package com.novadart.novabill.springsecurity;

import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
	
	@Autowired
	UtilsService utilsService;

	public AuthenticationEntryPoint(String loginFormUrl) {
		super(loginFormUrl);
	}


	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		if(utilsService.isGWTRPCCall(request))
			utilsService.sendException(request, response, new NotAuthenticatedException());
		else
			super.commence(request, response, authException);
	}

}
