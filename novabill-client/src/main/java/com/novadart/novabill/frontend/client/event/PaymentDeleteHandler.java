package com.novadart.novabill.frontend.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface PaymentDeleteHandler extends EventHandler {
	
	public void onPaymentDelete(PaymentDeleteEvent event);

}
