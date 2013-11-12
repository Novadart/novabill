package com.novadart.novabill.frontend.client.demo.facade.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.facade.EstimationGwtServiceAsync;

public class DemoEstimationService implements EstimationGwtServiceAsync {

	@Override
	public void add(EstimationDTO estimationDTO, AsyncCallback<Long> callback) {
		Data.save(estimationDTO, EstimationDTO.class);
		callback.onSuccess(estimationDTO.getId());
	}

	@Override
	public void get(Long id, AsyncCallback<EstimationDTO> callback) {
		try {
			callback.onSuccess(Data.getDoc(id, EstimationDTO.class));
		} catch (NoSuchObjectException e) {
			callback.onFailure(e);
		}
	}

	@Override
	public void getAllForClient(Long clientID,
			AsyncCallback<List<EstimationDTO>> callback) {
		callback.onSuccess(Data.getDocsList(clientID, EstimationDTO.class));
	}

	@Override
	public void getAllForClientInRange(Long clientID, int start, int length,
			AsyncCallback<PageDTO<EstimationDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAllInRange(Long businessID, int start, int length,
			AsyncCallback<PageDTO<EstimationDTO>> callback) {
		PageDTO<EstimationDTO> page = new PageDTO<EstimationDTO>();
		List<EstimationDTO> docs = Data.getDocsList(EstimationDTO.class);
		page.setItems(docs);
		page.setLength(docs.size());
		page.setOffset(0);
		page.setTotal(Long.valueOf(docs.size()));
		callback.onSuccess(page);
	}

	@Override
	public void getNextEstimationId(AsyncCallback<Long> callback) {
		callback.onSuccess(Data.nextDocID(EstimationDTO.class));
	}

	@Override
	public void remove(Long businessID, Long clientID, Long id,
			AsyncCallback<Void> callback) {
		Data.remove(clientID, id, EstimationDTO.class);
		callback.onSuccess(null);
	}

	@Override
	public void update(EstimationDTO estimationDTO, AsyncCallback<Void> callback) {
		Data.save(estimationDTO, EstimationDTO.class);
		callback.onSuccess(null);
	}

}
