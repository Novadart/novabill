package com.novadart.novabill.web.mvc;

public interface Urls {

	//	Public Urls
	public static final String PUBLIC_HOME = "/";
	public static final String PUBLIC_ABOUT = "/about";
	public static final String PUBLIC_PRICES = "/prices";
	public static final String PUBLIC_PAGE_NOT_FOUND = "/page-not-found";
	public static final String PUBLIC_EXCEPTION = "/oops";
	public static final String PUBLIC_ACTIVATE = "/activate";
	public static final String PUBLIC_LOGIN = "/login";
	public static final String PUBLIC_FORGOT_PASSWORD = "/forgot-password";
	public static final String PUBLIC_FORGOT_PASSWORD_OK = "/forgot-password-ok";
	public static final String PUBLIC_PASSWORD_RECOVERY = "/password-recovery";
	public static final String PUBLIC_REGISTER = "/register";
	public static final String PUBLIC_REGISTRATION_COMPLETE = "/registration-complete";
	public static final String PUBLIC_TOS = "/tos";
	public static final String PUBLIC_TOS_MINIMAL = "/tos-minimal";
	public static final String PUBLIC_PAYPAL_RETURN_PAGE = "/paypal-return-page";
	
	public static final String PUBLIC_SHARE_REQUEST = "/share-ask";
	public static final String PUBLIC_SHARE_SHARE = "/share";
	public static final String PUBLIC_SHARE_THANKS = "/share-thanks";
	
	public static final String PUBLIC_EMAIL_INVOICES = "/email/invoices";
	
	public static final String PUBLIC_PAYPAL_IPN_LISTENER = "/paypal-ipn-listener";
	
	// Private Urls	
	public static final String PRIVATE_HOME = "/private/";
	public static final String PRIVATE_HELLO = "/private/hello";
	public static final String PRIVATE_CHANGE_PASSWORD = "/private/change-password";
	public static final String PRIVATE_CLIENTS = "/private/clients";
	public static final String PRIVATE_DOCS_INVOICES = "/private/invoices";
	public static final String PRIVATE_DOCS_ESTIMATIONS = "/private/estimations";
	public static final String PRIVATE_DOCS_TRANSPORT_DOCUMENTS = "/private/transport-documents";
	public static final String PRIVATE_DOCS_CREDIT_NOTES = "/private/credit-notes";
	public static final String PRIVATE_COMMODITIES = "/private/commodities";
	public static final String PRIVATE_PRICE_LISTS = "/private/price-lists";
	public static final String PRIVATE_PAYMENTS = "/private/payments";
	public static final String PRIVATE_PREMIUM = "/private/premium";
	public static final String PRIVATE_SETTINGS = "/private/settings";
	public static final String PRIVATE_SHARE = "/private/share";
	public static final String PRIVATE_PRINT_PDF = "/private/print-pdf";
	public static final String PRIVATE_DELETE_ACCOUNT = "/private/delete-account";
	public static final String PRIVATE_LOGOUT = "/resources/logout";
	public static final String PRIVATE_EXPORT = "/private/export";
}
