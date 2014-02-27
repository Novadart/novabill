package com.novadart.novabill.frontend.client.util;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;

public class PDFUtils {
	
	public static DateTimeFormat ISO_DATE_FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd");

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
	
	public static void generatePaymentsProspectPdf(long startDate, long endDate) {
		generatePaymentsProspectPdf(new Date(startDate), new Date(endDate));
	}
	
	public static void generatePaymentsProspectPdf(final Date startDate, final Date endDate) {
		ServerFacade.INSTANCE.getBusinessService().generatePDFToken(new ManagedAsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				FileDownloadUtils.downloadUrl(
						ClientFactory.INSTANCE.getPdfProspectRequest()
							.replace("{startDate}", ISO_DATE_FORMAT.format(startDate))
							.replace("{endDate}", ISO_DATE_FORMAT.format(endDate))
							.replace("{token}", result)
						);
			}

		});
	}

}
