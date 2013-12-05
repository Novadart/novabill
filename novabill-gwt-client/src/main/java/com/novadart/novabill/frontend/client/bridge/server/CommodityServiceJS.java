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
import com.novadart.novabill.frontend.client.bridge.server.autobean.Commodity;
import com.novadart.novabill.frontend.client.bridge.server.autobean.CommodityList;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.exception.ValidationException;

public class CommodityServiceJS extends ServiceJS {
	
	
	public static void get(String businessID, String commodityID, final JavaScriptObject callback){
		SERVER_FACADE.getCommodityGwtService().get(Long.parseLong(businessID), Long.parseLong(commodityID), new ManagedAsyncCallback<CommodityDTO>() {

			@Override
			public void onSuccess(CommodityDTO result) {
				AutoBean<Commodity> c = AutoBeanEncoder.encode(result);
				BridgeUtils.invokeJSCallback(c, callback);
			}
		});

	}

	
	public static void getAll(String businessID, final JavaScriptObject callback){
		SERVER_FACADE.getCommodityGwtService().getAll(Long.parseLong(businessID), new ManagedAsyncCallback<List<CommodityDTO>>() {

			@Override
			public void onSuccess(List<CommodityDTO> result) {
				CommodityList il = AutoBeanMaker.INSTANCE.makeCommodityList().as();
				List<Commodity> commodities = new ArrayList<Commodity>(result.size());
				for (CommodityDTO id : result) {
					commodities.add(AutoBeanEncoder.encode(id).as());
				}
				il.setCommodities(commodities);
				
				BridgeUtils.invokeJSCallback(AutoBeanUtils.getAutoBean(il), callback);
				
			}
		});
	}

	public static void add(String commodityJson, final JavaScriptObject callback){
		AutoBean<Commodity> bean = AutoBeanCodex.decode(AutoBeanMaker.INSTANCE, Commodity.class, commodityJson);
		CommodityDTO c = AutoBeanDecoder.decode(bean.as());
		
		SERVER_FACADE.getCommodityGwtService().add(c, new ManagedAsyncCallback<Long>() {

			@Override
			public void onSuccess(Long result) {
				BridgeUtils.invokeJSCallback(result, callback);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				if(caught instanceof ValidationException){
					ValidationException ve = (ValidationException)caught;
					BridgeUtils.invokeJSCallbackOnException(ve.getClass().getName(), ve.getErrors().get(0).getErrorCode().name(), callback);
				} else {
					super.onFailure(caught);
				}
			}
		});
		
	}
	
	public static void update(String commodityJson, final JavaScriptObject callback){
		AutoBean<Commodity> bean = AutoBeanCodex.decode(AutoBeanMaker.INSTANCE, Commodity.class, commodityJson);
		CommodityDTO c = AutoBeanDecoder.decode(bean.as());
		
		SERVER_FACADE.getCommodityGwtService().update(c, new ManagedAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				BridgeUtils.invokeJSCallback(callback);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				if(caught instanceof ValidationException){
					ValidationException ve = (ValidationException)caught;
					BridgeUtils.invokeJSCallbackOnException(ve.getClass().getName(), ve.getErrors().get(0).getErrorCode().name(), callback);
				} else {
					super.onFailure(caught);
				}
			}
		});
		
	}

	public static void remove(String businessID, String id, final JavaScriptObject callback){
		SERVER_FACADE.getCommodityGwtService().remove(Long.parseLong(businessID), Long.parseLong(id), new ManagedAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				BridgeUtils.invokeJSCallback(callback);
			}
		});
		
	}

}
