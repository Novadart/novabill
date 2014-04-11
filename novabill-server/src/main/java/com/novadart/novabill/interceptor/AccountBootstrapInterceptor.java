package com.novadart.novabill.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.novadart.novabill.domain.security.Principal;

public class AccountBootstrapInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String requestURI = request.getRequestURI();


		//check if business object is present
		if(principal.getBusiness() == null){

			// check if the url we are loading is already the one for first run
			if ( !requestURI.contains("/private/settings/")) {

				String newURI = "/private/settings/";
				request.getRequestDispatcher(newURI).forward(request, response);

			}

		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
					throws Exception {
		// TODO Auto-generated method stub

	}


}
