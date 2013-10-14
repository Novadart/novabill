package com.novadart.novabill.frontend.client.bridge.server;

import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.novadart.novabill.frontend.client.bridge.server.data.ClientsData;
import com.novadart.novabill.frontend.client.bridge.server.data.IClientsData;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.IBusinessDTO;
import com.novadart.novabill.shared.client.dto.IClientDTO;

public class BusinessServiceJS extends ServiceJS {
	
	public static void get(String businessID, final JavaScriptObject callback) {
		SERVER_FACADE.getBusinessService().get(Long.parseLong(businessID), new ManagedAsyncCallback<BusinessDTO>() {
			@Override
			public void onSuccess(BusinessDTO result) {
				invokeJSCallback(IBusinessDTO.class, result, callback);				
			}
		});
	}

	
	public static void getClients(String businessID, final JavaScriptObject callback){
		SERVER_FACADE.getBusinessService().getClients(Long.valueOf(businessID), new ManagedAsyncCallback<List<ClientDTO>>() {
			@Override
			public void onSuccess(List<ClientDTO> result) {
				ClientsData cd = new ClientsData();
				cd.setClients(convertList(IClientDTO.class, result));
				invokeJSCallback(IClientsData.class, cd, callback);
			}
		});
	}
}
