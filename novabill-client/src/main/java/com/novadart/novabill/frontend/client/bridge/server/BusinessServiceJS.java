package com.novadart.novabill.frontend.client.bridge.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.novadart.novabill.frontend.client.bridge.server.autobean.AutoBeanConverter;
import com.novadart.novabill.frontend.client.bridge.server.autobean.AutoBeanMaker;
import com.novadart.novabill.frontend.client.bridge.server.autobean.Client;
import com.novadart.novabill.frontend.client.bridge.server.autobean.ClientsList;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class BusinessServiceJS extends ServiceJS {
	
	public static void get(String businessID, final JavaScriptObject callback) {
		SERVER_FACADE.getBusinessService().get(Long.parseLong(businessID), new ManagedAsyncCallback<BusinessDTO>() {
			@Override
			public void onSuccess(BusinessDTO result) {
				invokeJSCallback(AutoBeanConverter.convert(result), callback);		
			}
		});
	}

	
	public static void getClients(String businessID, final JavaScriptObject callback){
		SERVER_FACADE.getBusinessService().getClients(Long.valueOf(businessID), new ManagedAsyncCallback<List<ClientDTO>>() {
			@Override
			public void onSuccess(List<ClientDTO> result) {
				List<Client> clients = new ArrayList<Client>();
				for (ClientDTO c : result) {
					clients.add(AutoBeanConverter.convert(c).as());
				}
				
				ClientsList list = AutoBeanMaker.INSTANCE.makeClientsList().as();
				list.setClients(clients);
				
				invokeJSCallback(AutoBeanUtils.getAutoBean(list), callback);
			}
		});
	}
	
	
}
