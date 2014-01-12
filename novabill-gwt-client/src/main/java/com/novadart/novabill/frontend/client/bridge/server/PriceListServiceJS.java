package com.novadart.novabill.frontend.client.bridge.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.novadart.novabill.shared.client.dto.PriceDTO;
import com.novadart.novabill.shared.client.dto.PriceListDTO;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.tuple.Pair;

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
	
	
	public static void getPrices(String businessID, String priceListId, final JavaScriptObject callback) {
		SERVER_FACADE.getPriceListGwtService().getPrices(Long.parseLong(businessID), Long.parseLong(priceListId), 
				new ManagedAsyncCallback<Map<String,Pair<String,PriceDTO>>>() {

			@Override
			public void onSuccess(Map<String, Pair<String, PriceDTO>> result) {
				// TODO Auto-generated method stub
				
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
	
	
	public static void update(String priceListJson, final JavaScriptObject callback) {
		AutoBean<PriceList> bean = AutoBeanCodex.decode(AutoBeanMaker.INSTANCE, PriceList.class, priceListJson);
		PriceListDTO c = AutoBeanDecoder.decode(bean.as());
		
		SERVER_FACADE.getPriceListGwtService().update(c, new ManagedAsyncCallback<Void>() {

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
	
	
	public static void remove(String businessId, String priceListId, final JavaScriptObject callback) {
		SERVER_FACADE.getPriceListGwtService().remove(Long.parseLong(businessId), Long.parseLong(priceListId), 
				new ManagedAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				BridgeUtils.invokeJSCallback(callback);
			}
		});
	}
	
	
	public static void clonePriceList(String businessId, String priceListId, String priceListName, final JavaScriptObject callback) {
		SERVER_FACADE.getPriceListGwtService().clonePriceList(Long.parseLong(businessId), Long.parseLong(priceListId), priceListName, 
				new ManagedAsyncCallback<Long>() {

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
	
}
