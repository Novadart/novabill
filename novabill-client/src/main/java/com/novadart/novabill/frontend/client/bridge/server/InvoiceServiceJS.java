package com.novadart.novabill.frontend.client.bridge.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.novadart.novabill.frontend.client.bridge.server.data.Data;
import com.novadart.novabill.frontend.client.bridge.server.data.IAutoBeanInvoiceDTO;
import com.novadart.novabill.frontend.client.bridge.server.data.IInvoicesData;
import com.novadart.novabill.frontend.client.bridge.server.data.InvoicesData;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.shared.client.dto.IAccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class InvoiceServiceJS extends ServiceJS {

	
	public static void getAllForClient(String id, final JavaScriptObject callback) {
		SERVER_FACADE.getInvoiceService().getAllForClient(Long.parseLong(id), new ManagedAsyncCallback<List<InvoiceDTO>>() {

			@Override
			public void onSuccess(List<InvoiceDTO> result) {
				List<IAutoBeanInvoiceDTO> abi = new ArrayList<IAutoBeanInvoiceDTO>();
				for (InvoiceDTO i : result) {
					abi.add(Data.convert(i, convertList(IAccountingDocumentItemDTO.class, i.getItems())));
				}

				InvoicesData id = new InvoicesData();
				id.setInvoices(convertList(IAutoBeanInvoiceDTO.class, abi));
				invokeJSCallback(IInvoicesData.class, id, callback);
			}
		});
		
	}
	
	
}
