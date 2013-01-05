package com.novadart.novabill.frontend.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class BusinessUpdateEvent extends GwtEvent<BusinessUpdateHandler> {

	public static final Type<BusinessUpdateHandler> TYPE = new Type<BusinessUpdateHandler>();
	
	@Override
	public Type<BusinessUpdateHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(BusinessUpdateHandler handler) {
		handler.onBusinessUpdate(this);
	}
	
}
