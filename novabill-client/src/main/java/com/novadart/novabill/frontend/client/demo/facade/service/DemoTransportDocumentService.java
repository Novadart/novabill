package com.novadart.novabill.frontend.client.demo.facade.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.facade.TransportDocumentServiceAsync;

public class DemoTransportDocumentService implements TransportDocumentServiceAsync {

	@Override
	public void add(TransportDocumentDTO transportDocDTO, AsyncCallback<Long> callback) {
		Data.save(transportDocDTO, TransportDocumentDTO.class);
		callback.onSuccess(transportDocDTO.getId());
	}

	@Override
	public void get(Long id, AsyncCallback<TransportDocumentDTO> callback) {
		try {
			callback.onSuccess(Data.getDoc(id, TransportDocumentDTO.class));
		} catch (NoSuchObjectException e) {
			callback.onFailure(e);
		}
	}

	@Override
	public void getAllForClient(Long clientID,
			AsyncCallback<List<TransportDocumentDTO>> callback) {
		callback.onSuccess(Data.getDocsList(clientID, TransportDocumentDTO.class));
	}

	@Override
	public void getAllForClientInRange(Long clientID, Integer start,
			Integer length,
			AsyncCallback<PageDTO<TransportDocumentDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAllInRange(Long businessID, Integer start, Integer length,
			AsyncCallback<PageDTO<TransportDocumentDTO>> callback) {
		PageDTO<TransportDocumentDTO> page = new PageDTO<TransportDocumentDTO>();
		List<TransportDocumentDTO> docs = Data.getDocsList(TransportDocumentDTO.class);
		page.setItems(docs);
		page.setLength(docs.size());
		page.setOffset(0);
		page.setTotal(Long.valueOf(docs.size()));
		callback.onSuccess(page);
	}

	@Override
	public void getNextTransportDocId(AsyncCallback<Long> callback) {
		callback.onSuccess(Data.nextDocID(TransportDocumentDTO.class));
	}

	@Override
	public void remove(Long businessID, Long clientID, Long id,
			AsyncCallback<Void> callback) {
		Data.remove(clientID, id, TransportDocumentDTO.class);
		callback.onSuccess(null);
	}

	@Override
	public void update(TransportDocumentDTO transportDocDTO,
			AsyncCallback<Void> callback) {
		Data.save(transportDocDTO, TransportDocumentDTO.class);
		callback.onSuccess(null);
	}

}
