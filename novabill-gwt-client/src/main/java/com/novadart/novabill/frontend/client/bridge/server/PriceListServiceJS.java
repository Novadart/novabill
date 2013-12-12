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
import com.novadart.novabill.frontend.client.bridge.server.autobean.PriceList;
import com.novadart.novabill.frontend.client.bridge.server.autobean.PriceListList;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.shared.client.dto.PriceListDTO;

public class PriceListServiceJS extends ServiceJS {
	
	public static void getAll(String businessID, final JavaScriptObject callback) {
		SERVER_FACADE.getPriceListGwtService().getAll(Long.parseLong(businessID), new ManagedAsyncCallback<List<PriceListDTO>>() {

			@Override
			public void onSuccess(List<PriceListDTO> result) {
				PriceListList list = AutoBeanMaker.INSTANCE.makePriceListList().as();
				List<PriceList> priceLists = new ArrayList<PriceList>();
				for (PriceListDTO p : result) {
					priceLists.add(AutoBeanEncoder.encode(p).as());
				}
				list.setList(priceLists);
				
				BridgeUtils.invokeJSCallback(AutoBeanUtils.getAutoBean(list), callback);
			}
		});
	}
	
	
	public static void get(String id, final JavaScriptObject callback) {
		SERVER_FACADE.getPriceListGwtService().get(Long.parseLong(id), new ManagedAsyncCallback<PriceListDTO>() {

			@Override
			public void onSuccess(PriceListDTO result) {
				BridgeUtils.invokeJSCallback(AutoBeanEncoder.encode(result), callback);
			}
		});
	}
	
	
	public static void add(String priceListJson, final JavaScriptObject callback) {
		AutoBean<PriceList> bean = AutoBeanCodex.decode(AutoBeanMaker.INSTANCE, PriceList.class, priceListJson);
		PriceListDTO c = AutoBeanDecoder.decode(bean.as());
		
		SERVER_FACADE.getPriceListGwtService().add(c, new ManagedAsyncCallback<Long>() {

			@Override
			public void onSuccess(Long result) {
				BridgeUtils.invokeJSCallback(result, callback);
			}
		});
	}
	
	
	public static void update(String priceListJson, final JavaScriptObject callback) {
		AutoBean<PriceList> bean = AutoBeanCodex.decode(AutoBeanMaker.INSTANCE, PriceList.class, priceListJson);
		PriceListDTO c = AutoBeanDecoder.decode(bean.as());
		
		SERVER_FACADE.getPriceListGwtService().update(c, new ManagedAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				BridgeUtils.invokeJSCallback(callback);
			}
		});
	}
	
	
	public static void remove(String businessId, String priceListId, final JavaScriptObject callback) {
		SERVER_FACADE.getPriceListGwtService().remove(Long.parseLong(businessId), Long.parseLong(priceListId), 
				new ManagedAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				BridgeUtils.invokeJSCallback(callback);
			}
		});
	}
	
	
}
