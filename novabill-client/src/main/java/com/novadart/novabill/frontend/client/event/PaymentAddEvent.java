package com.novadart.novabill.frontend.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;

public class PaymentAddEvent extends GwtEvent<PaymentAddHandler> {

	public static final Type<PaymentAddHandler> TYPE = new Type<PaymentAddHandler>();
	private final PaymentTypeDTO payment;

	public PaymentAddEvent(PaymentTypeDTO payment) {
		this.payment = payment;
	}
	
	@Override
	public Type<PaymentAddHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PaymentAddHandler handler) {
		handler.onPaymentAdd(this);
	}
	
	public PaymentTypeDTO getPayment() {
		return payment;
	}

}
