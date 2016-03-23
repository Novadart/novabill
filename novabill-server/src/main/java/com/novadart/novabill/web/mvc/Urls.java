package com.novadart.novabill.web.mvc;

public interface Urls {

	//	Public Urls
	String PUBLIC_HOME = "/";
    String PUBLIC_REGISTER = "/register";
    String PUBLIC_FORGOT_PASSWORD = "/forgot-password";
    String PUBLIC_FORGOT_PASSWORD_OK = "/forgot-password-ok";
    String PUBLIC_PASSWORD_RECOVERY = "/password-recovery";
    String PUBLIC_PASSWORD_RECOVERY_OK = "/password-recovery-ok";
    String PUBLIC_LOGIN_CHECK = "/resources/login_check";

	// to fix
	String PUBLIC_ABOUT = "/about";
	String PUBLIC_PAGE_NOT_FOUND = "/page-not-found";
	String PUBLIC_EXCEPTION = "/oops";
	String PUBLIC_TOS = "/tos";
	String PUBLIC_TOS_MINIMAL = "/tos-minimal";
	String PUBLIC_PAYPAL_RETURN_PAGE = "/paypal-return-page";
	String PUBLIC_SHARE_REQUEST = "/share-ask";
	String PUBLIC_SHARE_SHARE = "/share";
	String PUBLIC_SHARE_THANKS = "/share-thanks";
	String PUBLIC_EMAIL_INVOICES = "/email/invoices";
	String PUBLIC_PAYPAL_IPN_LISTENER = "/paypal-ipn-listener";
	String PUBLIC_COOKIES_POLICY = "/cookies-policy" ;

	// Private Urls	
	String PRIVATE_HOME = "/private/";
	String PRIVATE_HELLO = "/private/hello";
	String PRIVATE_CHANGE_PASSWORD = "/private/change-password";
	String PRIVATE_CLIENTS = "/private/clients";
	String PRIVATE_DOCS_INVOICES = "/private/invoices";
	String PRIVATE_DOCS_ESTIMATIONS = "/private/estimations";
	String PRIVATE_DOCS_TRANSPORT_DOCUMENTS = "/private/transport-documents";
	String PRIVATE_DOCS_CREDIT_NOTES = "/private/credit-notes";
	String PRIVATE_COMMODITIES = "/private/commodities";
	String PRIVATE_PRICE_LISTS = "/private/price-lists";
	String PRIVATE_PAYMENTS = "/private/payments";
	String PRIVATE_PREMIUM = "/private/premium";
	String PRIVATE_SETTINGS = "/private/settings";
	String PRIVATE_SHARE = "/private/share";
	String PRIVATE_STATISTICS_GENERAL = "/private/stats";
	String PRIVATE_STATISTICS_CLIENTS = "/private/stats/clients";
	String PRIVATE_STATISTICS_COMMODITIES = "/private/stats/commodities";
	String PRIVATE_PRINT_PDF = "/private/print-pdf";
	String PRIVATE_DELETE_ACCOUNT = "/private/delete-account";
	String PRIVATE_LOGOUT = "/resources/logout";
	String PRIVATE_EXPORT = "/private/export";

}
