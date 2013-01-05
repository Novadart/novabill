package com.novadart.novabill.frontend.client;

import java.util.Comparator;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class Const {
	
	public static final String URL_LOGO = GWT.getHostPageBaseURL()+"private/businesses/logo";
	public static final String DELETE_LOGO = GWT.getHostPageBaseURL()+"private/businesses/logo";
	public static final String CHANGE_PASSWORD_URL = GWT.getHostPageBaseURL()+"private/change-password";
	public static final String XSRF_URL = GWT.getHostPageBaseURL() + "gwt/xsrf";
	public static final String POST_FEEDBACK_URL = GWT.getHostPageBaseURL()+"private/feedback";
	
	public static String genLogoUrl(){
		return URL_LOGO + "?v=" + new Date().getTime();
	}
	
	public static final Comparator<ClientDTO> CLIENT_COMPARATOR = new Comparator<ClientDTO>() {
		
		@Override
		public int compare(ClientDTO o1, ClientDTO o2) {
			
			return o1.getName().compareToIgnoreCase(o2.getName());
		}
	};
	
}
