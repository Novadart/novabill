package com.novadart.novabill.frontend.client.bridge.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.novadart.novabill.frontend.client.bridge.BridgeUtils;
import com.novadart.novabill.frontend.client.bridge.server.autobean.AutoBeanConverter;
import com.novadart.novabill.frontend.client.bridge.server.autobean.AutoBeanMaker;
import com.novadart.novabill.frontend.client.bridge.server.autobean.Invoice;
import com.novadart.novabill.frontend.client.bridge.server.autobean.InvoicesList;
import com.novadart.novabill.frontend.client.bridge.server.autobean.Page;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;


public class InvoiceServiceJS extends ServiceJS {

	
	public static void getAllForClient(String id, final JavaScriptObject callback) {
		SERVER_FACADE.getInvoiceService().getAllForClient(Long.parseLong(id), new ManagedAsyncCallback<List<InvoiceDTO>>() {

			@Override
			public void onSuccess(List<InvoiceDTO> result) {
				InvoicesList il = AutoBeanMaker.INSTANCE.makeInvoicesList().as();
				List<Invoice> invoices = new ArrayList<Invoice>(result.size());
				for (InvoiceDTO id : result) {
					invoices.add(AutoBeanConverter.convert(id).as());
				}
				il.setInvoices(invoices);
				BridgeUtils.invokeJSCallback(AutoBeanUtils.getAutoBean(il), callback);
			}
		});
	}

	
	public static void getAllInRange(String businessID, String start, String length, final JavaScriptObject callback) {
		SERVER_FACADE.getInvoiceService().getAllInRange(Long.parseLong(businessID), Integer.parseInt(start), Integer.parseInt(length), 
				new ManagedAsyncCallback<PageDTO<InvoiceDTO>>() {

			@Override
			public void onSuccess(PageDTO<InvoiceDTO> result) {
				AutoBean<Page<Invoice>> page = AutoBeanConverter.convertInvoicePage(result);
				BridgeUtils.invokeJSCallback(page, callback);
			}
		});
	}
	
}
