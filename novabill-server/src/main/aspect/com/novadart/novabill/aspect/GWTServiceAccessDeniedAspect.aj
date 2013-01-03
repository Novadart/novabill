package com.novadart.novabill.aspect;

import org.springframework.security.access.AccessDeniedException;
import com.novadart.novabill.annotation.HandleGWTServiceAccessDenied;
import com.novadart.novabill.shared.client.exception.DataAccessException;

public aspect GWTServiceAccessDeniedAspect {
	
	pointcut gwtServiceMethod():
		execution(public * (@HandleGWTServiceAccessDenied com.novadart.novabill.web.gwt.AbstractGwtController+).*(..));
	
	
	after() throwing(AccessDeniedException ex) throws DataAccessException: gwtServiceMethod(){
		throw new DataAccessException();
	}

}
