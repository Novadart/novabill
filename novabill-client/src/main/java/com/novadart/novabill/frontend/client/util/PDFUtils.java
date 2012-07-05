package com.novadart.novabill.frontend.client.util;

import com.google.gwt.core.client.GWT;
public class PDFUtils {

	private static final String EXPORT_REQUEST = 
			GWT.getHostPageBaseURL()+"private/pdf/landing/{document}/{id}";


	public static void generateInvoicePdf(final long id){
		FileDownloadUtils.downloadUrl(
				EXPORT_REQUEST.replace("{document}","invoices").replace("{id}", String.valueOf(id))
				);
	}

	public static void generateEstimationPdf(final long id){
		FileDownloadUtils.downloadUrl(
				EXPORT_REQUEST.replace("{document}","estimations").replace("{id}", String.valueOf(id))
				);
				
	}

}
