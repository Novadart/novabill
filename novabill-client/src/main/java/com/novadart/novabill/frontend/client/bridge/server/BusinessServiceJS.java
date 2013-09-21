package com.novadart.novabill.frontend.client.bridge.server;

import com.google.gwt.core.client.JavaScriptObject;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.IBusinessDTO;

public class BusinessServiceJS extends ServiceJS {
	

	public static void get(String businessID, final JavaScriptObject callback) {
		
		SERVER_FACADE.getBusinessService().get(Long.parseLong(businessID), new ManagedAsyncCallback<BusinessDTO>() {

			@Override
			public void onSuccess(BusinessDTO result) {
				invokeJSCallback(IBusinessDTO.class, result, callback);				
			}
		});
	}

}
