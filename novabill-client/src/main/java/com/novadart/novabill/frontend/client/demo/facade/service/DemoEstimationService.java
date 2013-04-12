package com.novadart.novabill.frontend.client.demo.facade.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.facade.EstimationServiceAsync;

public class DemoEstimationService implements EstimationServiceAsync {

	@Override
	public void add(EstimationDTO estimationDTO, AsyncCallback<Long> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void get(Long id, AsyncCallback<EstimationDTO> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAllForClient(Long clientID,
			AsyncCallback<List<EstimationDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAllForClientInRange(Long clientID, int start, int length,
			AsyncCallback<PageDTO<EstimationDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAllInRange(Long businessID, int start, int length,
			AsyncCallback<PageDTO<EstimationDTO>> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getNextEstimationId(AsyncCallback<Long> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(Long businessID, Long clientID, Long id,
			AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(EstimationDTO estimationDTO, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

}
