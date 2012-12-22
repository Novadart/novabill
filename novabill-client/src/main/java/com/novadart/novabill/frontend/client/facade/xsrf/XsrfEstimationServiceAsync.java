package com.novadart.novabill.frontend.client.facade.xsrf;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.facade.EstimationService;
import com.novadart.novabill.shared.client.facade.EstimationServiceAsync;

public class XsrfEstimationServiceAsync extends XsrfProtectedService<EstimationServiceAsync> implements
EstimationServiceAsync {

	public XsrfEstimationServiceAsync() {
		super((EstimationServiceAsync)GWT.create(EstimationService.class));
	}
	
	@Override
	public void add(final EstimationDTO estimationDTO, final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<EstimationServiceAsync>(callback) {

			@Override
			protected void performCall(EstimationServiceAsync service) {
				service.add(estimationDTO, callback);
			}

		});
	}

	@Override
	public void update(final EstimationDTO estimationDTO, final AsyncCallback<Void> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<EstimationServiceAsync>(callback) {

			@Override
			protected void performCall(EstimationServiceAsync service) {
				service.update(estimationDTO, callback);
			}

		});
	}

	@Override
	public void getNextEstimationId(final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<EstimationServiceAsync>(callback) {

			@Override
			protected void performCall(EstimationServiceAsync service) {
				service.getNextEstimationId(callback);
			}

		});
	}
	
	@Override
	public void get(final Long id, final AsyncCallback<EstimationDTO> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<EstimationServiceAsync>(callback) {

			@Override
			protected void performCall(EstimationServiceAsync service) {
				service.get(id, callback);
			}

		});
	}
	
	@Override
	public void getAllForClient(final Long clientID,
			final AsyncCallback<List<EstimationDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<EstimationServiceAsync>(callback) {

			@Override
			protected void performCall(EstimationServiceAsync service) {
				service.getAllForClient(clientID, callback);
			}

		});
	}
	
	@Override
	public void getAllForClientInRange(final Long clientID, final int start, final int length,
			final AsyncCallback<PageDTO<EstimationDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<EstimationServiceAsync>(callback) {

			@Override
			protected void performCall(EstimationServiceAsync service) {
				service.getAllForClientInRange(clientID, start, length, callback);
			}

		});
	}
	
	@Override
	public void getAllInRange(final Long businessID, final int start, final int length,
			final AsyncCallback<PageDTO<EstimationDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<EstimationServiceAsync>(callback) {

			@Override
			protected void performCall(EstimationServiceAsync service) {
				service.getAllInRange(businessID, start, length, callback);
			}

		});
	}
	
	@Override
	public void remove(final Long businessID, final Long clientID, final Long id,
			final AsyncCallback<Void> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<EstimationServiceAsync>(callback) {

			@Override
			protected void performCall(EstimationServiceAsync service) {
				service.remove(businessID, clientID, id, callback);
			}

		});
	}
	
}
