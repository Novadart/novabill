package com.novadart.novabill.shared.client.facade;

import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public interface TransportDocumentServiceAsync {

	void get(long id, AsyncCallback<TransportDocumentDTO> callback);

	void getAllForClient(long id, AsyncCallback<List<TransportDocumentDTO>> callback);

	void add(TransportDocumentDTO transportDocDTO, AsyncCallback<Long> callback);

	void remove(Long id, AsyncCallback<Void> callback);
	
	void update(TransportDocumentDTO transportDocDTO, AsyncCallback<Void> callback);

	void getNextTransportDocId(AsyncCallback<Long> callback);

	void getAllForClientInRange(long id, int start, int length, AsyncCallback<PageDTO<TransportDocumentDTO>> callback);

	void getAllInRange(int start, int length, AsyncCallback<PageDTO<TransportDocumentDTO>> callback);

}
