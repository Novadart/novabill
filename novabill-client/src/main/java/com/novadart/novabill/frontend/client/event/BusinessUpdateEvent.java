package com.novadart.novabill.frontend.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.novadart.novabill.shared.client.dto.IBusinessDTO;

public class BusinessUpdateEvent extends GwtEvent<BusinessUpdateHandler> {

	public static final Type<BusinessUpdateHandler> TYPE = new Type<BusinessUpdateHandler>();
	
	private final IBusinessDTO business;
	
	public BusinessUpdateEvent(IBusinessDTO business) {
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

	public IBusinessDTO getBusiness() {
		return business;
	}
	
}
