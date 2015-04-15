package com.novadart.novabill.frontend.client.bridge.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.novadart.novabill.frontend.client.bridge.BridgeUtils;
import com.novadart.novabill.frontend.client.bridge.server.autobean.AutoBeanEncoder;
import com.novadart.novabill.frontend.client.bridge.server.autobean.AutoBeanMaker;
import com.novadart.novabill.frontend.client.bridge.server.autobean.Invoice;
import com.novadart.novabill.frontend.client.bridge.server.autobean.InvoicesList;
import com.novadart.novabill.frontend.client.bridge.server.autobean.Page;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.shared.client.data.FilteringDateType;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;


public class InvoiceServiceJS extends ServiceJS {

	
	public static void getAllForClient(String id, String year, final JavaScriptObject callback) {
		SERVER_FACADE.getInvoiceService().getAllForClient(Long.parseLong(id), Integer.parseInt(year), new ManagedAsyncCallback<List<InvoiceDTO>>() {

			@Override
			public void onSuccess(List<InvoiceDTO> result) {
				InvoicesList il = AutoBeanMaker.INSTANCE.makeInvoicesList().as();
				List<Invoice> invoices = new ArrayList<Invoice>(result.size());
				for (InvoiceDTO id : result) {
					invoices.add(AutoBeanEncoder.encode(id).as());
				}
				il.setInvoices(invoices);
				BridgeUtils.invokeJSCallback(AutoBeanUtils.getAutoBean(il), callback);
			}
		});
	}

	
	public static void getAllInRange(String businessID, String year, String suffix, String start, String length, final JavaScriptObject callback) {
		SERVER_FACADE.getInvoiceService().getAllInRange(Long.parseLong(businessID), Integer.parseInt(year), suffix, Integer.parseInt(start), Integer.parseInt(length),
				new ManagedAsyncCallback<PageDTO<InvoiceDTO>>() {

			@Override
			public void onSuccess(PageDTO<InvoiceDTO> result) {
				AutoBean<Page<Invoice>> page = AutoBeanEncoder.encodeInvoicePage(result);
				BridgeUtils.invokeJSCallback(page, callback);
			}
		});
	}
	
	
	public static void setPayed(String businessID, String clientId, String id, boolean value, final JavaScriptObject callback) {
		SERVER_FACADE.getInvoiceService().setPayed(Long.parseLong(businessID), Long.parseLong(clientId), Long.parseLong(id), value, new ManagedAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				BridgeUtils.invokeJSCallback(callback);
			}
		});	
	}
	
	public static void remove(String businessID, String clientId, String invoiceId, final JavaScriptObject callback) {
		SERVER_FACADE.getInvoiceService().remove(Long.parseLong(businessID), Long.parseLong(clientId), Long.parseLong(invoiceId), 
				new ManagedAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				BridgeUtils.invokeJSCallback(callback);
			}
		});
	}
	
	public static void getAllUnpaidInDateRange(String filteringDateType, String startDate, String endDate, final JavaScriptObject callback) {
		Date sDate = startDate != null ? new Date(Long.parseLong(startDate)) : null;
		Date eDate = endDate != null ? new Date(Long.parseLong(endDate)) : null;
		
		SERVER_FACADE.getInvoiceService().getAllUnpaidInDateRange(FilteringDateType.valueOf(filteringDateType), sDate, eDate, 
				new ManagedAsyncCallback<List<InvoiceDTO>>() {

			@Override
			public void onSuccess(List<InvoiceDTO> result) {
				InvoicesList il = AutoBeanMaker.INSTANCE.makeInvoicesList().as();
				List<Invoice> invoices = new ArrayList<Invoice>(result.size());
				for (InvoiceDTO i : result) {
					invoices.add(AutoBeanEncoder.encode(i).as());
				}
				il.setInvoices(invoices);
				BridgeUtils.invokeJSCallback(AutoBeanUtils.getAutoBean(il), callback);
			}
		});	
		
	}
	
}
