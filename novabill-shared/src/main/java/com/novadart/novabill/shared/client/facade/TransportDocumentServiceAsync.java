package com.novadart.novabill.shared.client.facade;

import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public interface TransportDocumentServiceAsync {

	void add(TransportDocumentDTO transportDocDTO, AsyncCallback<Long> callback);

	void get(Long id, AsyncCallback<TransportDocumentDTO> callback);

	void getAll(Long businessID,
			AsyncCallback<List<TransportDocumentDTO>> callback);

	void getAllForClient(Long clientID,
			AsyncCallback<List<TransportDocumentDTO>> callback);

	void getAllForClientInRange(Long clientID, Integer start, Integer length,
			AsyncCallback<PageDTO<TransportDocumentDTO>> callback);

	void getAllInRange(Long businessID, Integer start, Integer length,
			AsyncCallback<PageDTO<TransportDocumentDTO>> callback);

	void getNextTransportDocId(AsyncCallback<Long> callback);

	void remove(Long businessID, Long clientID, Long id,
			AsyncCallback<Void> callback);

	void update(TransportDocumentDTO transportDocDTO,
			AsyncCallback<Void> callback);

}
