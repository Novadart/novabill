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
		generateInvoicePdf(String.valueOf(id));
	}

	public static void generateEstimationPdf(long id){
		generateEstimationPdf(String.valueOf(id));
	}

	public static void generateCreditNotePdf(long id) {
		generateCreditNotePdf(String.valueOf(id));
	}
	
	public static void generateTransportDocumentPdf(long id) {
		generateTransportDocumentPdf(String.valueOf(id));
	}
	
	
	public static void generateInvoicePdf(String id){
		generatePdf("invoices", id);
	}

	public static void generateEstimationPdf(String id){
		generatePdf("estimations", id);
	}

	public static void generateCreditNotePdf(String id) {
		generatePdf("creditnotes", id);
	}
	
	public static void generateTransportDocumentPdf(String id) {
		generatePdf("transportdocs", id);
	}

}
