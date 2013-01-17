package com.novadart.novabill.frontend.client.util;

import com.google.gwt.core.client.GWT;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.view.widget.notification.Notification;

public class ExportUtils {

	private static final String EXPORT_REQUEST = 
			GWT.getHostPageBaseURL()+"private/export?clients={c}&invoices={i}&estimations={e}&creditnotes={cn}&transportdocs={t}&token={token}";
	
	
	public static void exportData(final boolean clients, final boolean invoices, final boolean estimations,
			final boolean creditNotes, final boolean traspDocuments){
		ServerFacade.business.generateExportToken(new WrappedAsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				FileDownloadUtils.downloadUrl(
						EXPORT_REQUEST.replace("{c}", String.valueOf(clients))
							.replace("{i}", String.valueOf(invoices))
							.replace("{e}", String.valueOf(estimations))
							.replace("{cn}", String.valueOf(creditNotes))
							.replace("{t}", String.valueOf(traspDocuments))
							.replace("{token}", result)
						);
			}

			@Override
			public void onException(Throwable caught) {
				Notification.showMessage(I18N.INSTANCE.errorServerCommunication());
			}
		});
	}
	
}
