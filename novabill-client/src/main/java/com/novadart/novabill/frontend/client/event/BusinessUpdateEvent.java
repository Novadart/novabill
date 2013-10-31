package com.novadart.novabill.frontend.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.novadart.novabill.shared.client.dto.BusinessDTO;

public class BusinessUpdateEvent extends GwtEvent<BusinessUpdateHandler> {

	public static final Type<BusinessUpdateHandler> TYPE = new Type<BusinessUpdateHandler>();
	
	private final BusinessDTO business;
	
	public BusinessUpdateEvent(BusinessDTO business) {
		this.business = business;
	}
	
	@Override
	public Type<BusinessUpdateHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(BusinessUpdateHandler handler) {
		handler.onBusinessUpdate(this);
	}

	public BusinessDTO getBusiness() {
		return business;
	}
	
}
