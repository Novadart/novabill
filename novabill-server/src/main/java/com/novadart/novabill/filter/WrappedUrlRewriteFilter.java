package com.novadart.novabill.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;

public class WrappedUrlRewriteFilter extends UrlRewriteFilter {
	
	public static final String CONFIG_ENABLED = "enabled";
	
	private boolean isEnabled;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
		
		String enabledValue = filterConfig.getInitParameter(CONFIG_ENABLED);
		isEnabled = enabledValue.equals("true");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		if(isEnabled){
			super.doFilter(request, response, chain);
		} else {
			chain.doFilter(request, response);
		}
	}
	
}
