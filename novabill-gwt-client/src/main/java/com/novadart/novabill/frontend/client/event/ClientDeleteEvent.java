package com.novadart.novabill.frontend.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class ClientDeleteEvent extends GwtEvent<ClientDeleteHandler> {

	public static final Type<ClientDeleteHandler> TYPE = new Type<ClientDeleteHandler>();
	private final ClientDTO client;

	public ClientDeleteEvent(ClientDTO client) {
		this.client = client;
	}
	
	@Override
	public Type<ClientDeleteHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ClientDeleteHandler handler) {
		handler.onClientDelete(this);
	}
	
	public ClientDTO getClient() {
		return client;
	}

}
