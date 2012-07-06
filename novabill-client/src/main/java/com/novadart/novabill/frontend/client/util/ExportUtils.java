package com.novadart.novabill.frontend.client.util;

import com.google.gwt.core.client.GWT;

public class ExportUtils {

	private static final String EXPORT_REQUEST = 
			GWT.getHostPageBaseURL()+"private/export?clients={c}&invoices={i}&estimations={e}&token={token}";
	
	
	public static void exportData(boolean clients, boolean invoices, boolean estimations){
		FileDownloadUtils.downloadUrl(
				EXPORT_REQUEST.replace("{c}", String.valueOf(clients))
					.replace("{i}", String.valueOf(invoices))
					.replace("{e}", String.valueOf(estimations))
//					.replace("{token}", result)
				);
	}
	
}
