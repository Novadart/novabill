package com.novadart.novabill.shared.client.facade;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;

public interface ClientServiceAsync {

	void add(Long businessID, ClientDTO clientDTO, AsyncCallback<Long> callback);

	void get(Long id, AsyncCallback<ClientDTO> callback);

	void remove(Long businessID, Long id, AsyncCallback<Void> callback);

	void searchClients(Long businessID, String query, int start, int offset,
			AsyncCallback<PageDTO<ClientDTO>> callback);

	void update(Long businessID, ClientDTO clientDTO,
			AsyncCallback<Void> callback);

}
