package com.novadart.novabill.frontend.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface PaymentAddHandler extends EventHandler {
	
	public void onPaymentAdd(PaymentAddEvent event);

}
