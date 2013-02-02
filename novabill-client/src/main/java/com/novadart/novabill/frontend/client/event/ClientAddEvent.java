package com.novadart.novabill.frontend.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class ClientAddEvent extends GwtEvent<ClientAddHandler> {

	public static final Type<ClientAddHandler> TYPE = new Type<ClientAddHandler>();
	private final ClientDTO client;

	public ClientAddEvent(ClientDTO client) {
		this.client = client;
	}
	
	@Override
	public Type<ClientAddHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ClientAddHandler handler) {
		handler.onClientAdd(this);
	}
	
	public ClientDTO getClient() {
		return client;
	}
}
