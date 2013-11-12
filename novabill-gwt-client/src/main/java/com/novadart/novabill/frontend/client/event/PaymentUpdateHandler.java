package com.novadart.novabill.frontend.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface PaymentUpdateHandler extends EventHandler {
	
	public void onPaymentUpdate(PaymentUpdateEvent event);

}
