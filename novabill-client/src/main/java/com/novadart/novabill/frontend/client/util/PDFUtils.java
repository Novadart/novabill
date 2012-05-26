package com.novadart.novabill.frontend.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

public class PDFUtils {

	private static final String PDF_REQUEST = 
			GWT.getHostPageBaseURL()+"private/pdf/landing/{document}/{id}";

	
	public static void generateInvoicePdf(final long id){
		Window.open(
				PDF_REQUEST.replace("{document}","invoices").replace("{id}", String.valueOf(id))
				, null, "width=300px,height=200px;");
	}
	
	public static void generateEstimationPdf(final long id){
		Window.open(
				PDF_REQUEST.replace("{document}","estimations").replace("{id}", String.valueOf(id))
				, null, "width=300px,height=200px;");
	}
	
}
