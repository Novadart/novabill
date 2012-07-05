package com.novadart.novabill.frontend.client.util;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

public class ExportUtils {

	private static final String EXPORT_REQUEST = 
			GWT.getHostPageBaseURL()+"private/export/landing?clients={c}&invoices={i}&estimations={e}";
	
	private static final String POPUP_PARAMS = "width=640px,height=480px,top=10px,left=10px,status=0,menubar=0,scrollbars=0,location=0";

	
	public static void exportData(boolean clients, boolean invoices, boolean estimations){
		Window.open(
				EXPORT_REQUEST.replace("{c}", String.valueOf(clients))
					.replace("{i}", String.valueOf(invoices))
					.replace("{e}", String.valueOf(estimations))
				, null, POPUP_PARAMS);
	}
	
}
