package com.novadart.novabill.frontend.client.facade;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.StatusCodeException;

public class CallbackUtils {

	
	public static boolean isServerCommunicationException(Throwable e){
		return e instanceof StatusCodeException
				|| e instanceof IncompatibleRemoteServiceException
				|| e instanceof InvocationException;
	}
}
