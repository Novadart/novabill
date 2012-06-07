package com.novadart.novabill.frontend.client.facade.xsrf;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.XsrfToken;
import com.google.gwt.user.client.rpc.XsrfTokenService;
import com.google.gwt.user.client.rpc.XsrfTokenServiceAsync;
import com.novadart.novabill.frontend.client.Const;

abstract class XsrfProtectedService {
	
	static interface XsrfServerCallDelegate {
		public void performCall();
		public void manageException(Throwable caught);
	}


	private static final XsrfTokenServiceAsync xsrf = (XsrfTokenServiceAsync)GWT.create(XsrfTokenService.class);
	
	static {
		((ServiceDefTarget)xsrf).setServiceEntryPoint(Const.XSRF_URL);
	}
	
	
	protected final void performXsrfProtectedCall(final XsrfServerCallDelegate serverCall) {
		xsrf.getNewXsrfToken(new AsyncCallback<XsrfToken>() {
			
			@Override
			public void onSuccess(XsrfToken result) {
				setXsrfToken(result);
				serverCall.performCall();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				serverCall.manageException(caught);
			}
		});
	}
	
	protected abstract void setXsrfToken(XsrfToken token);
		
}
