package com.novadart.novabill.frontend.client.facade.xsrf;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.facade.ClientService;
import com.novadart.novabill.shared.client.facade.ClientServiceAsync;

public class XsrfClientServiceAsync extends XsrfProtectedService<ClientServiceAsync> implements
ClientServiceAsync {

	
	public XsrfClientServiceAsync() {
		super((ClientServiceAsync)GWT.create(ClientService.class));
	}

	@Override
	public void get(final Long id, final AsyncCallback<ClientDTO> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<ClientServiceAsync>(callback) {

			@Override
			protected void performCall(ClientServiceAsync service) {
				service.get(id, callback);
			}

		});
	}
	
	@Override
	public void add(final Long businessID, final ClientDTO clientDTO,
			final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<ClientServiceAsync>(callback) {

			@Override
			protected void performCall(ClientServiceAsync service) {
				service.add(businessID, clientDTO, callback);
			}

		});
	}
	
	@Override
	public void getAll(final Long businessID, final AsyncCallback<List<ClientDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<ClientServiceAsync>(callback) {

			@Override
			protected void performCall(ClientServiceAsync service) {
				service.getAll(businessID, callback);
			}

		});
	}
	
	@Override
	public void remove(final Long businessID, final Long id, final AsyncCallback<Void> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<ClientServiceAsync>(callback) {

			@Override
			protected void performCall(ClientServiceAsync service) {
				service.remove(businessID, id, callback);
			}

		});
	}
	
	@Override
	public void searchClients(final Long businessID, final String query, final int start,
			final int offset, final AsyncCallback<PageDTO<ClientDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<ClientServiceAsync>(callback) {

			@Override
			protected void performCall(ClientServiceAsync service) {
				service.searchClients(businessID, query, start, offset, callback);
			}

		});
	}
	
	@Override
	public void update(final Long businessID, final ClientDTO clientDTO,
			final AsyncCallback<Void> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<ClientServiceAsync>(callback) {

			@Override
			protected void performCall(ClientServiceAsync service) {
				service.update(businessID, clientDTO, callback);
			}

		});
	}

}
