package com.novadart.novabill.aspect.logging;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.service.UtilsService;

privileged aspect LoginLogoutAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginLogoutAspect.class);
	
	private UtilsService utilsService;

	public void setUtilsService(UtilsService utilsService) {
		this.utilsService = utilsService;
	}
	
	pointcut login(): execution(public void com.novadart.novabill.springsecurity.AuthenticationSuccessHandler.onAuthenticationSuccess(..));
	
	pointcut logout(): execution(public void org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler.onLogoutSuccess(..));
	
	after() returning: login(){
		Business business = utilsService.getAuthenticatedPrincipalDetails().getPrincipal();
		business.setLastLogin(System.currentTimeMillis());
		business.merge();
		LOGGER.info("[{}, login, {}]", new Object[]{business.getEmail(), new Date(business.getLastLogin())});
	}
	
	after() returning: logout(){
		LOGGER.info("[{}, logout, {}]", new Object[]{utilsService.getAuthenticatedPrincipalDetails().getPrincipal().getEmail(), new Date(System.currentTimeMillis())});
	}

}