package com.novadart.novabill.shared.client.facade;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;

public interface PaymentTypeServiceAsync {

	void add(PaymentTypeDTO paymentTypeDTO, AsyncCallback<Long> callback);

	void update(PaymentTypeDTO paymentTypeDTO, AsyncCallback<Void> callback);
	
	void remove(Long businessID, Long id, AsyncCallback<Void> callback);

	void getAll(Long businessID, AsyncCallback<List<PaymentTypeDTO>> callback);

}
