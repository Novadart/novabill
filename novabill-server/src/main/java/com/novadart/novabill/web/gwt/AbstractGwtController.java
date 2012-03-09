package com.novadart.novabill.web.gwt;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.RpcTokenException;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public abstract class AbstractGwtController<RemoteService, RemoteServiceImpl extends RemoteService> 
							extends RemoteServiceServlet implements Controller, ServletContextAware {

	private static final long serialVersionUID = 1L;

	private ServletContext servletContext;
	private final Class<RemoteService> remoteServiceClass;
	private RemoteServiceImpl remoteServiceImpl;

	public AbstractGwtController(Class<RemoteService> remoteServiceClass) {
		this(remoteServiceClass, null);
	}

	public AbstractGwtController(Class<RemoteService> remoteServiceClass, RemoteServiceImpl remoteServiceImpl) {
		this.remoteServiceClass = remoteServiceClass;
		this.remoteServiceImpl = remoteServiceImpl;
	}

	public void setRemoteServiceImpl(RemoteServiceImpl remoteServiceImpl) {
		this.remoteServiceImpl = remoteServiceImpl;
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response){
		super.doPost(request, response);
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String processCall(String payload) throws SerializationException {
		try {
			RPCRequest rpcRequest = RPC.decodeRequest(payload, remoteServiceClass, this);

			onAfterRequestDeserialized(rpcRequest);

			return RPC.invokeAndEncodeResponse(remoteServiceImpl==null ? ((RemoteServiceImpl) this) : remoteServiceImpl, 
					rpcRequest.getMethod(), 
					rpcRequest.getParameters(), rpcRequest.getSerializationPolicy(),
					rpcRequest.getFlags());
		}
		catch (IncompatibleRemoteServiceException ex) {

			return RPC.encodeResponseForFailure(null, ex);
		}
		catch (RpcTokenException tokenException) {

			return RPC.encodeResponseForFailure(null, tokenException);

		}
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public ServletContext getServletContext() {
		return servletContext;
	}
}
