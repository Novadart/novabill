package com.novadart.novabill.frontend.client.facade.xsrf;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.HasRpcToken;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.XsrfToken;
import com.google.gwt.user.client.rpc.XsrfTokenService;
import com.google.gwt.user.client.rpc.XsrfTokenServiceAsync;
import com.novadart.novabill.frontend.client.Const;

abstract class XsrfProtectedService {
	
	public static abstract class XsrfServerCallDelegate {
		
		private final AsyncCallback<?> callback;
		
		public XsrfServerCallDelegate(AsyncCallback<?> callback) {
			this.callback = callback;
		}
		
		public abstract void performCall();
		
		private void manageException(Throwable caught) {
			callback.onFailure(caught);
		}
	}


	private static final XsrfTokenServiceAsync xsrf = (XsrfTokenServiceAsync)GWT.create(XsrfTokenService.class);
	
	static {
		((ServiceDefTarget)xsrf).setServiceEntryPoint(Const.XSRF_URL);
	}
	
	private final HasRpcToken service;
	
	public XsrfProtectedService(HasRpcToken service) {
		this.service = service;
	}
	
	protected final void performXsrfProtectedCall(final XsrfServerCallDelegate serverCall) {
		xsrf.getNewXsrfToken(new AsyncCallback<XsrfToken>() {
			
			@Override
			public void onSuccess(XsrfToken result) {
				service.setRpcToken(result);
				serverCall.performCall();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				serverCall.manageException(caught);
			}
		});
	}
		
}
