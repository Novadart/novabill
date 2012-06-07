package com.novadart.novabill.frontend.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

public class PDFUtils {

	private static final String PDF_REQUEST = 
			GWT.getHostPageBaseURL()+"private/pdf/landing/{document}/{id}";
	
	private static final String POPUP_PARAMS = "width=380px,height=260px,top=10px,left=10px,status=0,menubar=0,scrollbars=0,location=0";

	
	public static void generateInvoicePdf(final long id){
		Window.open(
				PDF_REQUEST.replace("{document}","invoices").replace("{id}", String.valueOf(id))
				, null, POPUP_PARAMS);
	}
	
	public static void generateEstimationPdf(final long id){
		Window.open(
				PDF_REQUEST.replace("{document}","estimations").replace("{id}", String.valueOf(id))
				, "_blank", POPUP_PARAMS);
	}
	
}
