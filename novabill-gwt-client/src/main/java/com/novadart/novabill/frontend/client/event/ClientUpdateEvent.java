package com.novadart.novabill.frontend.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class ClientUpdateEvent extends GwtEvent<ClientUpdateHandler> {

	public static final Type<ClientUpdateHandler> TYPE = new Type<ClientUpdateHandler>();
	private final ClientDTO client;

	public ClientUpdateEvent(ClientDTO client) {
		this.client = client;
	}
	
	@Override
	public Type<ClientUpdateHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ClientUpdateHandler handler) {
		handler.onClientUpdate(this);
	}
	
	public ClientDTO getClient() {
		return client;
	}

}
