package com.novadart.novabill.shared.client.facade;

import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;

public interface EstimationServiceAsync {

	void add(EstimationDTO estimationDTO, AsyncCallback<Long> callback);

	void get(Long id, AsyncCallback<EstimationDTO> callback);

	void getAll(Long businessID, AsyncCallback<List<EstimationDTO>> callback);

	void getAllForClient(Long clientID,
			AsyncCallback<List<EstimationDTO>> callback);

	void getAllForClientInRange(Long clientID, int start, int length,
			AsyncCallback<PageDTO<EstimationDTO>> callback);

	void getAllInRange(Long businessID, int start, int length,
			AsyncCallback<PageDTO<EstimationDTO>> callback);

	void getNextEstimationId(AsyncCallback<Long> callback);

	void remove(Long businessID, Long clientID, Long id,
			AsyncCallback<Void> callback);

	void update(EstimationDTO estimationDTO, AsyncCallback<Void> callback);

}
