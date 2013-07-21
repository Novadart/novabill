package com.novadart.novabill.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;

@Service("utilsService")
public class UtilsService {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private Authentication getAuthentication(){
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	public boolean isAuthenticated(){
		Authentication authentication = getAuthentication();
		return authentication != null && !(authentication instanceof AnonymousAuthenticationToken); 
	}
	
	public Principal getAuthenticatedPrincipalDetails(){
		return (Principal)getAuthentication().getPrincipal();
	}
	
	private boolean isGWTContentType(HttpServletRequest request){
		String contentType = request.getContentType();
		return contentType != null && contentType.trim().toLowerCase().startsWith("text/x-gwt-rpc");
	}
	
	private boolean containsGWTHeaders(HttpServletRequest request){
		return (request.getHeader("x-gwt-module-base") != null) || (request.getHeader("x-gwt-permutation") != null);
	}
	
	public boolean isGWTRPCCall(HttpServletRequest request){
		return isGWTContentType(request) || containsGWTHeaders(request); 
	}
	
	
	public void sendException(HttpServletRequest request, HttpServletResponse response, Exception ex) throws IOException{
		try {
			String payload = RPC.encodeResponseForFailure(null, ex);
			IOUtils.copy(new ByteArrayInputStream(payload.getBytes()), response.getOutputStream());
		} catch (SerializationException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String hash(String value, Object salt){
		return passwordEncoder.encodePassword(value, salt);
	}
	
	public void setBusinessForPrincipal(Business business) throws NotAuthenticatedException{
		if(!isAuthenticated())
			throw new NotAuthenticatedException();
		getAuthenticatedPrincipalDetails().setBusiness(business);
	}
	
}
