package com.novadart.novabill.frontend.client.demo;

import com.google.gwt.core.client.GWT;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.ClientFactoryImpl;

public class DemoClientFactoryImpl extends ClientFactoryImpl implements ClientFactory {

	private static final String URL_LOGO = "demo/logo";
	private static final String URL_THUMB = "demo/logo";
	private static final String UPDATE_LOGO = "demo";
	private static final String DELETE_LOGO = "demo";
	private static final String CHANGE_PASSWORD_URL = "demo";
	private static final String XSRF_URL = "demo";
	private static final String POST_FEEDBACK_URL = "demo";
	private static final String LOGOUT_URL = GWT.getHostPageBaseURL();
	private static final String DELETE_ACCOUNT_URL = "demo";
	private static final String EXPORT_REQUEST = "";
	private static final String PDF_REQUEST = "";
	private static final String REGISTER_ACCOUNT_URL = GWT.getHostPageBaseURL() + "register";
	
	private static String logoUrl = URL_THUMB;
	
	@Override
	public String getPdfRequest() {
		return PDF_REQUEST;
	}
	
	@Override
	public String getExportRequest() {
		return EXPORT_REQUEST;
	}
	
	@Override
	public String getLogoUrl(){
		return logoUrl;
	}
	
	@Override
	public void refeshLogoUrl(){
	}
	
	@Override
	public String getUrlLogo() {
		return URL_LOGO;
	}

	@Override
	public String getUrlThumb() {
		return URL_THUMB;
	}

	@Override
	public String getUpdateLogo() {
		return UPDATE_LOGO;
	}

	@Override
	public String getDeleteLogo() {
		return DELETE_LOGO;
	}

	@Override
	public String getChangePasswordUrl() {
		return CHANGE_PASSWORD_URL;
	}

	@Override
	public String getXsrfUrl() {
		return XSRF_URL;
	}

	@Override
	public String getPostFeedbackUrl() {
		return POST_FEEDBACK_URL;
	}
	
	@Override
	public String getLogoutUrl() {
		return LOGOUT_URL;
	}
	
	@Override
	public String getDeleteAccountUrl() {
		return DELETE_ACCOUNT_URL;
	}

	@Override
	public String getRegisterAccountUrl() {
		return REGISTER_ACCOUNT_URL;
	}
}
