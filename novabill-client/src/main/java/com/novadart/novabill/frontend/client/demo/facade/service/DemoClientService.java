package com.novadart.novabill.frontend.client.demo.facade.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.facade.ClientServiceAsync;

public class DemoClientService implements ClientServiceAsync {

	@Override
	public void add(Long businessID, ClientDTO clientDTO,
			AsyncCallback<Long> callback) {
		Data.save(clientDTO);
		callback.onSuccess(clientDTO.getId());
	}

	@Override
	public void get(Long id, AsyncCallback<ClientDTO> callback) {
		try {
			callback.onSuccess(Data.getClient(id));
		} catch (NoSuchObjectException e) {
			callback.onFailure(e);
		}
	}

	@Override
	public void remove(Long businessID, Long id, AsyncCallback<Void> callback) {
		Data.getClients().remove(id);
		callback.onSuccess(null);
	}

	@Override
	public void searchClients(Long businessID, String query, int start,
			int offset, AsyncCallback<PageDTO<ClientDTO>> callback) {
		PageDTO<ClientDTO> p = new PageDTO<ClientDTO>();
		
		List<ClientDTO> items = Data.getClients();
		p.setItems(items);
		p.setLength(items.size());
		p.setTotal(Long.valueOf(items.size()));
		
		callback.onSuccess(p);
	}

	@Override
	public void update(Long businessID, ClientDTO clientDTO,
			AsyncCallback<Void> callback) {
		Data.save(clientDTO);
		callback.onSuccess(null);
	}

}
