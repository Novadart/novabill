package com.novadart.novabill.frontend.client.demo.facade.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.facade.TransportDocumentServiceAsync;

public class DemoTransportDocumentService implements
		TransportDocumentServiceAsync {

	@Override
	public void add(TransportDocumentDTO transportDocDTO,
			AsyncCallback<Long> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void get(Long id, AsyncCallback<TransportDocumentDTO> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAllForClient(Long clientID,
			AsyncCallback<List<TransportDocumentDTO>> callback) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	@Override
	public void getNextTransportDocId(AsyncCallback<Long> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(Long businessID, Long clientID, Long id,
			AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(TransportDocumentDTO transportDocDTO,
			AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

}
