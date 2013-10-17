package com.novadart.novabill.frontend.client.bridge.server;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.novadart.novabill.frontend.client.bridge.server.autobean.AutoBeanConverter;
import com.novadart.novabill.frontend.client.bridge.server.autobean.Client;
import com.novadart.novabill.frontend.client.bridge.server.autobean.Page;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;


public class ClientServiceJS extends ServiceJS {

	public static void searchClients(String businessID, String query, String start, String offset, final JavaScriptObject callback) {
		SERVER_FACADE.getClientService().searchClients(Long.valueOf(businessID), 
				query, Integer.parseInt(start), Integer.parseInt(offset), new ManagedAsyncCallback<PageDTO<ClientDTO>>() {

			@Override
			public void onSuccess(final PageDTO<ClientDTO> result) {
				AutoBean<Page<Client>> page = AutoBeanConverter.convert(result);
				invokeJSCallback(page, callback);
			}
		});
	}

	
	public static void get(String id, final JavaScriptObject callback) {
		SERVER_FACADE.getClientService().get(Long.valueOf(id), new ManagedAsyncCallback<ClientDTO>() {

			@Override
			public void onSuccess(ClientDTO result) {
				invokeJSCallback(AutoBeanConverter.convert(result), callback);
			}
		});
		
	}
	
	
	public static void remove(String businessID, String id, final JavaScriptObject callback) {
		SERVER_FACADE.getClientService().remove(Long.valueOf(businessID), Long.valueOf(id), new ManagedAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				invokeJSCallback(callback);
			}
		});
		
	}
	
}
