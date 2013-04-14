package com.novadart.novabill.frontend.client.util;

import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;

public class ExportUtils {

	public static void exportData(final boolean clients, final boolean invoices, final boolean estimations,
			final boolean creditNotes, final boolean traspDocuments){
		ServerFacade.INSTANCE.getBusinessService().generateExportToken(new ManagedAsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				FileDownloadUtils.downloadUrl(
						ClientFactory.INSTANCE.getExportRequest()
							.replace("{c}", String.valueOf(clients))
							.replace("{i}", String.valueOf(invoices))
							.replace("{e}", String.valueOf(estimations))
							.replace("{cn}", String.valueOf(creditNotes))
							.replace("{t}", String.valueOf(traspDocuments))
							.replace("{token}", result)
						);
			}

		});
	}
	
}
