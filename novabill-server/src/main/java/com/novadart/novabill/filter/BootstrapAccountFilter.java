package com.novadart.novabill.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;

import com.novadart.novabill.domain.security.Principal;

public class BootstrapAccountFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		
		Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String requestURI = req.getRequestURI();
		
		
		//check if business object is present
		if(principal.getBusiness() == null){
			
			// check if the url we are loading is already the one for first run
			if (requestURI.contains("/private/firstrun/")) {
				
				chain.doFilter(request, response);
				
				//otherwise redirect to the first run url
			} else {
				
				String newURI = "/private/firstrun/";
				req.getRequestDispatcher(newURI).forward(request, response);
				
			}
			
		} else {
			
			if (requestURI.contains("/private/firstrun/")) {
				
				String newURI = "/private/";
				req.getRequestDispatcher(newURI).forward(request, response);
				
			} else {
				
				chain.doFilter(request, response);
				
			}
			
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		

		


	}

	@Override
	public void destroy() {}

}
