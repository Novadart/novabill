package com.novadart.novabill.frontend.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;

public class PaymentDeleteEvent extends GwtEvent<PaymentDeleteHandler> {

	public static final Type<PaymentDeleteHandler> TYPE = new Type<PaymentDeleteHandler>();
	private final PaymentTypeDTO payment;

	public PaymentDeleteEvent(PaymentTypeDTO payment) {
		this.payment = payment;
	}
	
	@Override
	public Type<PaymentDeleteHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PaymentDeleteHandler handler) {
		handler.onPaymentDelete(this);
	}
	
	public PaymentTypeDTO getPayment() {
		return payment;
	}

}
