package com.novadart.novabill.frontend.client;

import java.util.Date;

import com.google.gwt.core.client.GWT;

public class Const {
	
	public static final String URL_LOGO = GWT.getHostPageBaseURL()+"private/businesses/logo";
	public static final String XSRF_URL = GWT.getHostPageBaseURL() + "gwt/xsrf";

	public static String genLogoUrl(){
		return URL_LOGO + "?v=" + new Date().getTime();
	}
}
