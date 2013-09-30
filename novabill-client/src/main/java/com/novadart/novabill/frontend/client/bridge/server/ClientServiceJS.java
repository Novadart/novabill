package com.novadart.novabill.frontend.client.bridge.server;

import com.google.gwt.core.client.JavaScriptObject;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.IPageDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;

public class ClientServiceJS extends ServiceJS {

	public static void searchClients(String businessID, String query, String start, String offset, final JavaScriptObject callback) {
		SERVER_FACADE.getClientService().searchClients(Long.valueOf(businessID), 
				query, Integer.parseInt(start), Integer.parseInt(offset), new ManagedAsyncCallback<PageDTO<ClientDTO>>() {

			@Override
			public void onSuccess(final PageDTO<ClientDTO> result) {
				invokeJSCallback(IPageDTO.class, result, callback);
			}
		});
	}

}
