package com.novadart.novabill.frontend.client.facade.xsrf;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.HasRpcToken;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.XsrfToken;
import com.google.gwt.user.client.rpc.XsrfTokenService;
import com.google.gwt.user.client.rpc.XsrfTokenServiceAsync;
import com.novadart.novabill.frontend.client.Const;

abstract class XsrfProtectedService<Service> {
	
	static abstract class XsrfServerCallDelegate<Service> {
		
		private final AsyncCallback<?> callback;
		
		XsrfServerCallDelegate(AsyncCallback<?> callback) {
			this.callback = callback;
		}
		
		protected abstract void performCall(Service service);
		
		private void manageException(Throwable caught) {
			callback.onFailure(caught);
		}
	}


	private static final XsrfTokenServiceAsync xsrf = (XsrfTokenServiceAsync)GWT.create(XsrfTokenService.class);
	
	static {
		((ServiceDefTarget)xsrf).setServiceEntryPoint(Const.XSRF_URL);
	}
	
	private final Service service;
	
	XsrfProtectedService(Service service) {
		this.service = service;
	}
	
	protected Service getService() {
		return service;
	}
	
	protected final void performXsrfProtectedCall(final XsrfServerCallDelegate<Service> serverCall) {
		xsrf.getNewXsrfToken(new AsyncCallback<XsrfToken>() {
			
			@Override
			public void onSuccess(XsrfToken result) {
				((HasRpcToken) service).setRpcToken(result);
				serverCall.performCall(service);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				serverCall.manageException(caught);
			}
		});
	}
		
}
