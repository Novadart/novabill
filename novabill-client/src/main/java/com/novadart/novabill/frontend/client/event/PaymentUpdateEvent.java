package com.novadart.novabill.frontend.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;

public class PaymentUpdateEvent extends GwtEvent<PaymentUpdateHandler> {

	public static final Type<PaymentUpdateHandler> TYPE = new Type<PaymentUpdateHandler>();
	private final PaymentTypeDTO payment;

	public PaymentUpdateEvent(PaymentTypeDTO payment) {
		this.payment = payment;
	}
	
	@Override
	public Type<PaymentUpdateHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PaymentUpdateHandler handler) {
		handler.onPaymentUpdate(this);
	}
	
	public PaymentTypeDTO getPayment() {
		return payment;
	}

}
