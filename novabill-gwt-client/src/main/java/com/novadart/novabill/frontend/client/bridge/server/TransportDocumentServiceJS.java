package com.novadart.novabill.frontend.client.bridge.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.novadart.novabill.frontend.client.bridge.BridgeUtils;
import com.novadart.novabill.frontend.client.bridge.server.autobean.AutoBeanEncoder;
import com.novadart.novabill.frontend.client.bridge.server.autobean.AutoBeanMaker;
import com.novadart.novabill.frontend.client.bridge.server.autobean.Page;
import com.novadart.novabill.frontend.client.bridge.server.autobean.TransportDocument;
import com.novadart.novabill.frontend.client.bridge.server.autobean.TransportDocumentList;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public class TransportDocumentServiceJS extends ServiceJS {
	
	public static void getAllForClient(String id, String year, final JavaScriptObject callback) {
		SERVER_FACADE.getTransportdocumentService().getAllForClient(Long.parseLong(id), Integer.parseInt(year), new ManagedAsyncCallback<List<TransportDocumentDTO>>() {

			@Override
			public void onSuccess(List<TransportDocumentDTO> result) {
				TransportDocumentList il = AutoBeanMaker.INSTANCE.makeTransportDocumentList().as();
				List<TransportDocument> transportDocuments = new ArrayList<TransportDocument>(result.size());
				for (TransportDocumentDTO id : result) {
					transportDocuments.add(AutoBeanEncoder.encode(id).as());
				}
				il.setTransportDocuments(transportDocuments);
				BridgeUtils.invokeJSCallback(AutoBeanUtils.getAutoBean(il), callback);
			}
		});
	}

	
	public static void getAllInRange(String businessID, String year, String start, String length, final JavaScriptObject callback) {
		SERVER_FACADE.getTransportdocumentService().getAllInRange(Long.parseLong(businessID), Integer.parseInt(year), Integer.parseInt(start), Integer.parseInt(length), 
				new ManagedAsyncCallback<PageDTO<TransportDocumentDTO>>() {

			@Override
			public void onSuccess(PageDTO<TransportDocumentDTO> result) {
				AutoBean<Page<TransportDocument>> page = AutoBeanEncoder.encodeTransportDocumentPage(result);
				BridgeUtils.invokeJSCallback(page, callback);
			}
		});
	}
	
	public static void remove(String businessID, String clientId, String invoiceId, final JavaScriptObject callback) {
		SERVER_FACADE.getTransportdocumentService().remove(Long.parseLong(businessID), Long.parseLong(clientId), Long.parseLong(invoiceId), 
				new ManagedAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				BridgeUtils.invokeJSCallback(callback);
			}
		});
	}

}
