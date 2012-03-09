package com.novadart.novabill.shared.client.facade;

import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.EstimationDTO;

public interface EstimationServiceAsync {

	void get(long id, AsyncCallback<EstimationDTO> callback);

	void add(EstimationDTO estimationDTO, AsyncCallback<Long> callback);

	void remove(Long id, AsyncCallback<Void> callback);

	void getAllForClient(long clientId,
			AsyncCallback<List<EstimationDTO>> callback);

	void update(EstimationDTO estimationDTO, AsyncCallback<Void> callback);

}
