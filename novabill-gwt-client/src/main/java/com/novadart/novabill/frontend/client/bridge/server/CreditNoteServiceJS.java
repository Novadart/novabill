package com.novadart.novabill.frontend.client.bridge.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.novadart.novabill.frontend.client.bridge.BridgeUtils;
import com.novadart.novabill.frontend.client.bridge.server.autobean.AutoBeanConverter;
import com.novadart.novabill.frontend.client.bridge.server.autobean.AutoBeanMaker;
import com.novadart.novabill.frontend.client.bridge.server.autobean.CreditNote;
import com.novadart.novabill.frontend.client.bridge.server.autobean.CreditNoteList;
import com.novadart.novabill.frontend.client.bridge.server.autobean.Page;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;

public class CreditNoteServiceJS extends ServiceJS {
	
	public static void getAllForClient(String id, String year, final JavaScriptObject callback) {
		SERVER_FACADE.getCreditNoteService().getAllForClient(Long.parseLong(id), Integer.parseInt(year), new ManagedAsyncCallback<List<CreditNoteDTO>>() {

			@Override
			public void onSuccess(List<CreditNoteDTO> result) {
				CreditNoteList il = AutoBeanMaker.INSTANCE.makeCreditNotesList().as();
				List<CreditNote> creditNotes = new ArrayList<CreditNote>(result.size());
				for (CreditNoteDTO id : result) {
					creditNotes.add(AutoBeanConverter.convert(id).as());
				}
				il.setCreditNotes(creditNotes);
				BridgeUtils.invokeJSCallback(AutoBeanUtils.getAutoBean(il), callback);
			}
		});
	}

	
	public static void getAllInRange(String businessID, String year, String start, String length, final JavaScriptObject callback) {
		SERVER_FACADE.getCreditNoteService().getAllInRange(Long.parseLong(businessID), Integer.parseInt(year), Integer.parseInt(start), Integer.parseInt(length), 
				new ManagedAsyncCallback<PageDTO<CreditNoteDTO>>() {

			@Override
			public void onSuccess(PageDTO<CreditNoteDTO> result) {
				AutoBean<Page<CreditNote>> page = AutoBeanConverter.convertCreditNotePage(result);
				BridgeUtils.invokeJSCallback(page, callback);
			}
		});
	}
	
	public static void remove(String businessID, String clientId, String invoiceId, final JavaScriptObject callback) {
		SERVER_FACADE.getCreditNoteService().remove(Long.parseLong(businessID), Long.parseLong(clientId), Long.parseLong(invoiceId), 
				new ManagedAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				BridgeUtils.invokeJSCallback(callback);
			}
		});
	}

}
