package com.novadart.novabill.frontend.client.bridge.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.novadart.novabill.frontend.client.bridge.BridgeUtils;
import com.novadart.novabill.frontend.client.bridge.server.autobean.AutoBeanEncoder;
import com.novadart.novabill.frontend.client.bridge.server.autobean.AutoBeanMaker;
import com.novadart.novabill.frontend.client.bridge.server.autobean.Estimation;
import com.novadart.novabill.frontend.client.bridge.server.autobean.EstimationList;
import com.novadart.novabill.frontend.client.bridge.server.autobean.Page;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;

public class EstimationServiceJS extends ServiceJS {
	
	public static void getAllForClient(String id, String year, final JavaScriptObject callback) {
		SERVER_FACADE.getEstimationService().getAllForClient(Long.parseLong(id), Integer.parseInt(year), new ManagedAsyncCallback<List<EstimationDTO>>() {

			@Override
			public void onSuccess(List<EstimationDTO> result) {
				EstimationList il = AutoBeanMaker.INSTANCE.makeEstimationList().as();
				List<Estimation> estimations = new ArrayList<Estimation>(result.size());
				for (EstimationDTO id : result) {
					estimations.add(AutoBeanEncoder.encode(id).as());
				}
				il.setEstimations(estimations);
				BridgeUtils.invokeJSCallback(AutoBeanUtils.getAutoBean(il), callback);
			}
		});
	}

	
	public static void getAllInRange(String businessID, String year, String start, String length, final JavaScriptObject callback) {
		SERVER_FACADE.getEstimationService().getAllInRange(Long.parseLong(businessID), Integer.parseInt(year), Integer.parseInt(start), Integer.parseInt(length), 
				new ManagedAsyncCallback<PageDTO<EstimationDTO>>() {

			@Override
			public void onSuccess(PageDTO<EstimationDTO> result) {
				AutoBean<Page<Estimation>> page = AutoBeanEncoder.encodeEstimationPage(result);
				BridgeUtils.invokeJSCallback(page, callback);
			}
		});
	}
	
	public static void remove(String businessID, String clientId, String invoiceId, final JavaScriptObject callback) {
		SERVER_FACADE.getEstimationService().remove(Long.parseLong(businessID), Long.parseLong(clientId), Long.parseLong(invoiceId), 
				new ManagedAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				BridgeUtils.invokeJSCallback(callback);
			}
		});
	}

}
