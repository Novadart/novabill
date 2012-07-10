package com.novadart.novabill.frontend.client.facade.xsrf;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.HasRpcToken;
import com.google.gwt.user.client.rpc.XsrfToken;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.facade.EstimationService;
import com.novadart.novabill.shared.client.facade.EstimationServiceAsync;

public class XsrfEstimationServiceAsync extends XsrfProtectedService implements
EstimationServiceAsync {

	private static final EstimationServiceAsync estimation = 
			(EstimationServiceAsync)GWT.create(EstimationService.class);

	@Override
	public void get(final long id, final AsyncCallback<EstimationDTO> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate() {

			@Override
			public void performCall() {
				estimation.get(id, callback);
			}

			@Override
			public void manageException(final Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}

	@Override
	public void add(final EstimationDTO estimationDTO, final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate() {

			@Override
			public void performCall() {
				estimation.add(estimationDTO, callback);
			}

			@Override
			public void manageException(final Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}

	@Override
	public void remove(final Long id, final AsyncCallback<Void> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate() {

			@Override
			public void performCall() {
				estimation.remove(id, callback);
			}

			@Override
			public void manageException(final Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}

	@Override
	public void getAllForClient(final long clientId,
			final AsyncCallback<List<EstimationDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate() {

			@Override
			public void performCall() {
				estimation.getAllForClient(clientId, callback);
			}

			@Override
			public void manageException(final Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}

	@Override
	public void update(final EstimationDTO estimationDTO, final AsyncCallback<Void> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate() {

			@Override
			public void performCall() {
				estimation.update(estimationDTO, callback);
			}

			@Override
			public void manageException(final Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}

	@Override
	public void getNextEstimationId(final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate() {

			@Override
			public void performCall() {
				estimation.getNextEstimationId(callback);
			}

			@Override
			public void manageException(final Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}
	
	@Override
	protected void setXsrfToken(final XsrfToken token) {
		((HasRpcToken) estimation).setRpcToken(token);
	}

	@Override
	public void getAllForClientInRange(final long id, final int start, final int length,
			final AsyncCallback<PageDTO<EstimationDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate() {

			@Override
			public void performCall() {
				estimation.getAllForClientInRange(id, start, length, callback);
			}

			@Override
			public void manageException(final Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}

	@Override
	public void getAllInRange(final int start, final int length,
			final AsyncCallback<PageDTO<EstimationDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate() {

			@Override
			public void performCall() {
				estimation.getAllInRange(start, length, callback);
			}

			@Override
			public void manageException(final Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}

}
