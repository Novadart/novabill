package com.novadart.novabill.frontend.client.facade.xsrf;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.facade.PaymentTypeService;
import com.novadart.novabill.shared.client.facade.PaymentTypeServiceAsync;

public class XsrfPaymentType extends XsrfProtectedService<PaymentTypeServiceAsync> implements PaymentTypeServiceAsync {

	XsrfPaymentType() {
		super((PaymentTypeServiceAsync)GWT.create(PaymentTypeService.class));
	}

	@Override
	public void add(final PaymentTypeDTO paymentTypeDTO, final AsyncCallback<Long> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<PaymentTypeServiceAsync>(callback) {

			@Override
			protected void performCall(PaymentTypeServiceAsync service) {
				service.add(paymentTypeDTO, callback);
			}
		});
	}

	@Override
	public void update(final PaymentTypeDTO paymentTypeDTO, final AsyncCallback<Void> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<PaymentTypeServiceAsync>(callback) {

			@Override
			protected void performCall(PaymentTypeServiceAsync service) {
				service.update(paymentTypeDTO, callback);
			}
		});
	}

	@Override
	public void remove(final Long businessID, final Long id, final AsyncCallback<Void> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<PaymentTypeServiceAsync>(callback) {

			@Override
			protected void performCall(PaymentTypeServiceAsync service) {
				service.remove(businessID, id, callback);
			}
		});
	}

	@Override
	public void getAll(final Long businessID, final AsyncCallback<List<PaymentTypeDTO>> callback) {
		performXsrfProtectedCall(new XsrfServerCallDelegate<PaymentTypeServiceAsync>(callback) {

			@Override
			protected void performCall(PaymentTypeServiceAsync service) {
				service.getAll(businessID, callback);
			}
		});
	}


}
