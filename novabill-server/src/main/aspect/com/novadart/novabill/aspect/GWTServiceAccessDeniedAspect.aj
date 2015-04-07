package com.novadart.novabill.aspect;

import com.novadart.novabill.shared.client.exception.DataAccessException;
import org.springframework.security.access.AccessDeniedException;

public aspect GWTServiceAccessDeniedAspect {
	
	pointcut gwtServiceMethod():
		execution(public * (@HandleGWTServiceAccessDenied com.novadart.novabill.web.gwt.AbstractGwtController+).*(..));
	
	
	after() throwing(AccessDeniedException ex) throws DataAccessException: gwtServiceMethod(){
		throw new DataAccessException();
	}

}
