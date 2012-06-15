package com.novadart.novabill.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.novadart.novabill.domain.security.PrincipalDetails;

@Service("utilsService")
public class UtilsService {

	private Authentication getAuthentication(){
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	public boolean isAuthenticated(){
		Authentication authentication = getAuthentication();
		return !(authentication instanceof AnonymousAuthenticationToken); 
	}
	
	public PrincipalDetails getAuthenticatedPrincipalDetails(){
		return (PrincipalDetails)getAuthentication().getPrincipal();
	}
	
	public boolean isGWTRPCCall(HttpServletRequest request){
		String contentType = request.getContentType();
		return contentType != null && contentType.trim().toLowerCase().startsWith("text/x-gwt-rpc");
	}
	
	
	public void sendException(HttpServletRequest request, HttpServletResponse response, Exception ex) throws IOException{
		try {
			String payload = RPC.encodeResponseForFailure(null, ex);
			IOUtils.copy(new ByteArrayInputStream(payload.getBytes()), response.getOutputStream());
		} catch (SerializationException e) {
			throw new RuntimeException(e);
		}
	}
	
}
