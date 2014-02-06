package com.novadart.novabill.frontend.client.bridge.server;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.novadart.novabill.frontend.client.bridge.BridgeUtils;
import com.novadart.novabill.frontend.client.bridge.server.autobean.AutoBeanDecoder;
import com.novadart.novabill.frontend.client.bridge.server.autobean.AutoBeanEncoder;
import com.novadart.novabill.frontend.client.bridge.server.autobean.AutoBeanMaker;
import com.novadart.novabill.frontend.client.bridge.server.autobean.Client;
import com.novadart.novabill.frontend.client.bridge.server.autobean.Page;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;


public class ClientServiceJS extends ServiceJS {

	public static void searchClients(String businessID, String query, String start, String offset, final JavaScriptObject callback) {
		SERVER_FACADE.getClientService().searchClients(Long.valueOf(businessID), 
				query, Integer.parseInt(start), Integer.parseInt(offset), new ManagedAsyncCallback<PageDTO<ClientDTO>>() {

			@Override
			public void onSuccess(final PageDTO<ClientDTO> result) {
				AutoBean<Page<Client>> page = AutoBeanEncoder.encodeClientPage(result);
				BridgeUtils.invokeJSCallback(page, callback);
			}
		});
	}

	
	public static void get(String id, final JavaScriptObject callback) {
		SERVER_FACADE.getClientService().get(Long.valueOf(id), new ManagedAsyncCallback<ClientDTO>() {

			@Override
			public void onSuccess(ClientDTO result) {
				BridgeUtils.invokeJSCallback(AutoBeanEncoder.encode(result), callback);
			}
		});
		
	}
	
	
	public static void add(String businessID, String clientJson, final JavaScriptObject callback) {
		AutoBean<Client> bean = AutoBeanCodex.decode(AutoBeanMaker.INSTANCE, Client.class, clientJson);
		ClientDTO clientDTO = AutoBeanDecoder.decode(bean.as());
		
		SERVER_FACADE.getClientService().add(Long.parseLong(businessID), clientDTO, new ManagedAsyncCallback<Long>() {

			@Override
			public void onSuccess(Long result) {
				BridgeUtils.invokeJSCallback(result, callback);
			}
		});		
	}
	
	
	public static void remove(String businessID, String id, final JavaScriptObject callback) {
		SERVER_FACADE.getClientService().remove(Long.valueOf(businessID), Long.valueOf(id), new ManagedAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				BridgeUtils.invokeJSCallback(callback);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				if(caught instanceof DataIntegrityException){
					DataIntegrityException di = (DataIntegrityException)caught;
					BridgeUtils.invokeJSCallbackOnException(di.getClass().getName(), null, callback);
				} else {
					super.onFailure(caught);
				}
			}
		});
		
	}
	
}
