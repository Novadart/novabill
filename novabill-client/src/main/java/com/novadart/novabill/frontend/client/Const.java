package com.novadart.novabill.frontend.client;

import java.util.Comparator;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;

public class Const {
	
	public static final String URL_LOGO = GWT.getHostPageBaseURL()+"private/businesses/logo";
	public static final String URL_THUMB = GWT.getHostPageBaseURL()+"private/businesses/logo/thumbnail";
	public static final String UPDATE_LOGO = GWT.getHostPageBaseURL()+"private/businesses/logo?token=";
	public static final String DELETE_LOGO = GWT.getHostPageBaseURL()+"private/businesses/logo?token=";
	public static final String CHANGE_PASSWORD_URL = GWT.getHostPageBaseURL()+"private/change-password";
	public static final String XSRF_URL = GWT.getHostPageBaseURL() + "gwt/xsrf";
	public static final String POST_FEEDBACK_URL = GWT.getHostPageBaseURL()+"private/feedback";
	
	private static String logoUrl = URL_THUMB + "?v=" + new Date().getTime();
	
	public static String getLogoUrl(){
		return logoUrl;
	}
	
	public static void refeshLogoUrl(){
		logoUrl = URL_THUMB + "?v=" + new Date().getTime();
	}
	
	public static final Comparator<ClientDTO> CLIENT_COMPARATOR = new Comparator<ClientDTO>() {
		
		@Override
		public int compare(ClientDTO o1, ClientDTO o2) {
			
			return o1.getName().compareToIgnoreCase(o2.getName());
		}
	};
	
	public static final Comparator<PaymentTypeDTO> PAYMENT_COMPARATOR = new Comparator<PaymentTypeDTO>() {
		
		@Override
		public int compare(PaymentTypeDTO o1, PaymentTypeDTO o2) {
			return o1.getName().compareTo(o2.getName());
		}
	};
	
	//comparator returns inverse ordering
	public static final Comparator<AccountingDocumentDTO> DOCUMENT_COMPARATOR = new Comparator<AccountingDocumentDTO>() {
		
		@Override
		public int compare(AccountingDocumentDTO o1, AccountingDocumentDTO o2) {
			Integer year1 = Integer.parseInt(DateTimeFormat.getFormat("yyyy").format(o1.getAccountingDocumentDate()));
			Integer year2 = Integer.parseInt(DateTimeFormat.getFormat("yyyy").format(o2.getAccountingDocumentDate()));
			
			if(year1 != year2) {
				return -year1.compareTo(year2);
			} else {
				return -o1.getDocumentID().compareTo(o2.getDocumentID());
			}
		}
	};
	
}
