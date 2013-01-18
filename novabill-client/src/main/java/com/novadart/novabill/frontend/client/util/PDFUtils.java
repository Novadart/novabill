package com.novadart.novabill.frontend.client.util;

import com.google.gwt.core.client.GWT;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.widget.notification.Notification;

public class PDFUtils {

	private static final String EXPORT_REQUEST = 
			GWT.getHostPageBaseURL()+"private/pdf/{document}/{id}?token={token}";


	private static void generatePdf(final String documentClass, final String documentId) {
		ServerFacade.business.generatePDFToken(new WrappedAsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				FileDownloadUtils.downloadUrl(
						EXPORT_REQUEST.replace("{document}", documentClass)
							.replace("{id}", documentId)
							.replace("{token}", result)
						);
			}

			@Override
			public void onException(Throwable caught) {
				Notification.showMessage(I18N.INSTANCE.errorServerCommunication());
			}
		});
	}
	
	public static void generateInvoicePdf(long id){
		generatePdf("invoices", String.valueOf(id));
	}

	public static void generateEstimationPdf(long id){
		generatePdf("estimations", String.valueOf(id));
	}

	public static void generateCreditNotePdf(long id) {
		generatePdf("creditnotes", String.valueOf(id));
	}
	
	public static void generateTransportDocumentPdf(long id) {
		generatePdf("transportdocs", String.valueOf(id));
	}

}
