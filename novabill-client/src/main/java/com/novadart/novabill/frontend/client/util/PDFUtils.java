package com.novadart.novabill.frontend.client.util;

import com.google.gwt.core.client.GWT;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.ui.widget.notification.Notification;

public class PDFUtils {

	private static final String EXPORT_REQUEST = 
			GWT.getHostPageBaseURL()+"private/pdf/{document}/{id}?token={token}";


	public static void generateInvoicePdf(final long id){
		ServerFacade.invoice.generatePDFToken(new WrappedAsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				FileDownloadUtils.downloadUrl(
						EXPORT_REQUEST.replace("{document}","invoices").replace("{id}", String.valueOf(id))
							.replace("{token}", result)
						);
			}

			@Override
			public void onException(Throwable caught) {
				Notification.showMessage(I18N.INSTANCE.errorServerCommunication());
			}
		});
	}

	public static void generateEstimationPdf(final long id){
		ServerFacade.invoice.generatePDFToken(new WrappedAsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				FileDownloadUtils.downloadUrl(
						EXPORT_REQUEST.replace("{document}","estimations").replace("{id}", String.valueOf(id))
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
