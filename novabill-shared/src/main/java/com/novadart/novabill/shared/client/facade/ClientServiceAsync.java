package com.novadart.novabill.shared.client.facade;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;

public interface ClientServiceAsync {

	void getAll(AsyncCallback<List<ClientDTO>> callback);

	void remove(Long id, AsyncCallback<Void> callback);

	void add(ClientDTO clientDTO, AsyncCallback<Long> callback);

	void update(ClientDTO clientDTO, AsyncCallback<Void> callback);

	void get(Long id, AsyncCallback<ClientDTO> callback);

	void getFromInvoiceId(Long id, AsyncCallback<ClientDTO> callback);

	void searchClients(String query, int start, int offset,
			AsyncCallback<PageDTO<ClientDTO>> callback);

	void getFromEstimationId(Long id, AsyncCallback<ClientDTO> callback);

}
