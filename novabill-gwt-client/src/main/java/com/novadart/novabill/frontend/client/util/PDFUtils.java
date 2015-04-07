package com.novadart.novabill.frontend.client.util;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.shared.client.data.FilteringDateType;

import java.util.Date;

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

	public static void generatePaymentsProspectPdf(FilteringDateType filteringDateType, Long startDate, Long endDate) {
		generatePaymentsProspectPdf(
				filteringDateType,
				startDate != null ? new Date(startDate) : null, 
				endDate != null ? new Date(endDate) : null
				);
	}

	public static void generatePaymentsProspectPdf(final FilteringDateType filteringDateType, final Date startDate, final Date endDate) {
		ServerFacade.INSTANCE.getBusinessService().generatePDFToken(new ManagedAsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				StringBuilder sb = new StringBuilder();
				sb.append(ClientFactory.INSTANCE.getPdfProspectRequest());
				sb.append("?token=");
				sb.append(result);
				sb.append("&filteringDateType=");
				sb.append(filteringDateType.name());
				if(startDate != null){
					sb.append("&startDate=");
					sb.append(ISO_DATE_FORMAT.format(startDate));
				}
				if(endDate != null){
					sb.append("&endDate=");
					sb.append(ISO_DATE_FORMAT.format(endDate));
				}

				FileDownloadUtils.downloadUrl( sb.toString() );
			}

		});
	}

}
