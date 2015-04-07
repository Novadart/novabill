package com.novadart.novabill.web.gwt;

import com.google.gwt.user.server.rpc.XsrfProtectedServiceServlet;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractGwtController	extends XsrfProtectedServiceServlet implements Controller, ServletContextAware {

	private static final long serialVersionUID = 1L;

	private ServletContext servletContext;

	public AbstractGwtController() {
		super("JSESSIONID");
	}
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response){
		super.doPost(request, response);
		return null;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public ServletContext getServletContext() {
		return servletContext;
	}
}
