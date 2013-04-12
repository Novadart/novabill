package com.novadart.novabill.frontend.client.demo.facade.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.facade.ClientServiceAsync;

public class DemoClientService implements ClientServiceAsync {

	@Override
	public void add(Long businessID, ClientDTO clientDTO,
			AsyncCallback<Long> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void get(Long id, AsyncCallback<ClientDTO> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(Long businessID, Long id, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void searchClients(Long businessID, String query, int start,
			int offset, AsyncCallback<PageDTO<ClientDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Long businessID, ClientDTO clientDTO,
			AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

}
