package com.novadart.novabill.frontend.client.demo.facade.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.facade.PaymentTypeServiceAsync;

public class DemoPaymentTypeService implements PaymentTypeServiceAsync {

	@Override
	public void add(PaymentTypeDTO paymentTypeDTO, AsyncCallback<Long> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(PaymentTypeDTO paymentTypeDTO,
			AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(Long businessID, Long id, AsyncCallback<Void> callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAll(Long businessID,
			AsyncCallback<List<PaymentTypeDTO>> callback) {
		// TODO Auto-generated method stub

	}

}
