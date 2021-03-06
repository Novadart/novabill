package com.novadart.novabill.service;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.function.Supplier;

@Service("utilsService")
public class UtilsService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PrincipalDetailsService principalDetailsService;

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
	
	public void setBusinessForPrincipal(Business business) throws NotAuthenticatedException{
		if(!isAuthenticated())
			throw new NotAuthenticatedException();
		getAuthenticatedPrincipalDetails().setBusiness(business);
	}
	
	public boolean isPasswordValid(String encodedPassword, String rawPassword){
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}

	private void setSecurityContext(String username){
		UserDetails userDetails = principalDetailsService.loadUserByUsername(username);
		Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	private void clearSecurityContext(){
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	/*
	 * This method executes 'action' on the behalf of the business identified by 'businessID', taking advantage of the
	 * credentials given to the said business. To be used with care.
	 */
	public <T> T executeActionAsBusiness(Supplier<T> action, Long businessID){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication(); //save authentication
		try{
			Business business = Business.findBusiness(businessID);
			String username = business.getPrincipals().iterator().next().getUsername();
			setSecurityContext(username);
			return action.get();
		} finally {
			clearSecurityContext();
			SecurityContextHolder.getContext().setAuthentication(auth); //restore authentication
		}
	}


}
