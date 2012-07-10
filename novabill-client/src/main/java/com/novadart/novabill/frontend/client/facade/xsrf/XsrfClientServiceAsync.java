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
	public void getAll(final AsyncCallback<List<ClientDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<ClientServiceAsync>(callback) {

			@Override
			protected void performCall(ClientServiceAsync service) {
				service.getAll(callback);
			}

		});
	}

	@Override
	public void remove(final Long id, final AsyncCallback<Void> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<ClientServiceAsync>(callback) {

			@Override
			protected void performCall(ClientServiceAsync service) {
				service.remove(id, callback);
			}

		});
	}

	@Override
	public void add(final ClientDTO clientDTO, final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<ClientServiceAsync>(callback) {

			@Override
			protected void performCall(ClientServiceAsync service) {
				service.add(clientDTO, callback);
			}

		});
	}

	@Override
	public void update(final ClientDTO clientDTO, final AsyncCallback<Void> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<ClientServiceAsync>(callback) {

			@Override
			protected void performCall(ClientServiceAsync service) {
				service.update(clientDTO, callback);
			}

		});
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
	public void getFromInvoiceId(final Long id, final AsyncCallback<ClientDTO> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<ClientServiceAsync>(callback) {

			@Override
			protected void performCall(ClientServiceAsync service) {
				service.getFromInvoiceId(id, callback);
			}

		});
	}

	@Override
	public void searchClients(final String query, final int start, final int offset,
			final AsyncCallback<PageDTO<ClientDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<ClientServiceAsync>(callback) {

			@Override
			protected void performCall(ClientServiceAsync service) {
				service.searchClients(query, start, offset, callback);
			}

		});
	}

	@Override
	public void getFromEstimationId(final Long id, final AsyncCallback<ClientDTO> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<ClientServiceAsync>(callback) {

			@Override
			protected void performCall(ClientServiceAsync service) {
				service.getFromEstimationId(id, callback);
			}

		});
	}

}
