package com.novadart.novabill.frontend.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface ClientUpdateHandler extends EventHandler {
	
	public void onClientUpdate(ClientUpdateEvent event);

}
