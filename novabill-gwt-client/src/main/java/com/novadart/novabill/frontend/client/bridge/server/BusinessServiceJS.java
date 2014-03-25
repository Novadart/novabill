package com.novadart.novabill.frontend.client.bridge.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.novadart.novabill.frontend.client.bridge.BridgeUtils;
import com.novadart.novabill.frontend.client.bridge.server.autobean.AutoBeanDecoder;
import com.novadart.novabill.frontend.client.bridge.server.autobean.AutoBeanEncoder;
import com.novadart.novabill.frontend.client.bridge.server.autobean.AutoBeanMaker;
import com.novadart.novabill.frontend.client.bridge.server.autobean.Business;
import com.novadart.novabill.frontend.client.bridge.server.autobean.BusinessStats;
import com.novadart.novabill.frontend.client.bridge.server.autobean.Client;
import com.novadart.novabill.frontend.client.bridge.server.autobean.ClientsList;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class BusinessServiceJS extends ServiceJS {
	
	public static void get(String businessID, final JavaScriptObject callback) {
		SERVER_FACADE.getBusinessService().get(Long.parseLong(businessID), new ManagedAsyncCallback<BusinessDTO>() {
			@Override
			public void onSuccess(BusinessDTO result) {
				BridgeUtils.invokeJSCallback(AutoBeanEncoder.encode(result), callback);		
			}
		});
	}

	
	public static void generatePDFToken(final JavaScriptObject callback){
		SERVER_FACADE.getBusinessService().generatePDFToken(new ManagedAsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				BridgeUtils.invokeJSCallback(result, callback);
			}
		});
	}
	
	
	public static void getClients(String businessID, final JavaScriptObject callback){
		SERVER_FACADE.getBusinessService().getClients(Long.valueOf(businessID), new ManagedAsyncCallback<List<ClientDTO>>() {
			@Override
			public void onSuccess(List<ClientDTO> result) {
				List<Client> clients = new ArrayList<Client>();
				for (ClientDTO c : result) {
					clients.add(AutoBeanEncoder.encode(c).as());
				}
				
				ClientsList list = AutoBeanMaker.INSTANCE.makeClientsList().as();
				list.setClients(clients);
				
				BridgeUtils.invokeJSCallback(AutoBeanUtils.getAutoBean(list), callback);
			}
		});
	}
	
	
	public static void getStats(String businessID, final JavaScriptObject callback) {
		SERVER_FACADE.getBusinessService().getStats(Long.valueOf(businessID), new ManagedAsyncCallback<BusinessStatsDTO>() {

			@Override
			public void onSuccess(BusinessStatsDTO result) {
				AutoBean<BusinessStats> b = AutoBeanEncoder.encode(result);
				BridgeUtils.invokeJSCallback(b, callback);
			}
		});
	}
	
	
	public static void update(String businessJson, final JavaScriptObject callback) {
		AutoBean<Business> bean = AutoBeanCodex.decode(AutoBeanMaker.INSTANCE, Business.class, businessJson);
		BusinessDTO b = AutoBeanDecoder.decode(bean.as());
		
		SERVER_FACADE.getBusinessService().update(b, new ManagedAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				BridgeUtils.invokeJSCallback(callback);
			}
		});
	}
	
}
