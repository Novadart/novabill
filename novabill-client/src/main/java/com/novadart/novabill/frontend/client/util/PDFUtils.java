package com.novadart.novabill.frontend.client.util;

import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;

public class PDFUtils {

	private static void generatePdf(final String documentClass, final String documentId) {
		ServerFacade.INSTANCE.getBusinessService().generatePDFToken(new ManagedAsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				FileDownloadUtils.downloadUrl(
						ClientFactory.INSTANCE.getPdfRequest().replace("{document}", documentClass)
							.replace("{id}", documentId)
							.replace("{token}", result)
						);
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
