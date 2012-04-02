package com.novadart.novabill.frontend.client;

import java.util.Date;

import com.google.gwt.core.client.GWT;

public class Const {
	
	public static final String URL_LOGO = GWT.getHostPageBaseURL()+"businesses/logo";
	

	public static String genLogoUrl(){
		return URL_LOGO + "?v=" + new Date().getTime();
	}
}
