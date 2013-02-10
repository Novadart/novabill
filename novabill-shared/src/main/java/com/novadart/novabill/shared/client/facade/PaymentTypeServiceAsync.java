package com.novadart.novabill.shared.client.facade;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;

public interface PaymentTypeServiceAsync {

	void add(PaymentTypeDTO paymentTypeDTO, AsyncCallback<Long> callback);

}
