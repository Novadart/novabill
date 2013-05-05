package com.novadart.novabill.frontend.client.demo.facade.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.facade.PaymentTypeServiceAsync;

public class DemoPaymentTypeService implements PaymentTypeServiceAsync {

	@Override
	public void add(PaymentTypeDTO paymentTypeDTO, AsyncCallback<Long> callback) {
		Data.save(paymentTypeDTO);
		callback.onSuccess(paymentTypeDTO.getId());
	}

	@Override
	public void update(PaymentTypeDTO paymentTypeDTO, AsyncCallback<Void> callback) {
		Data.save(paymentTypeDTO);
		callback.onSuccess(null);
	}

	@Override
	public void remove(Long businessID, Long id, AsyncCallback<Void> callback) {
		Data.removePayment(id);
		callback.onSuccess(null);
	}

	@Override
	public void getAll(Long businessID,	AsyncCallback<List<PaymentTypeDTO>> callback) {
		callback.onSuccess(Data.getPayments());
	}

	@Override
	public void get(Long id, AsyncCallback<PaymentTypeDTO> callback) {
		callback.onSuccess(Data.getPayment(id));
	}

}
