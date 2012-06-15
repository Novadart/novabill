package com.novadart.novabill.frontend.client.datawatcher;

import com.google.gwt.event.shared.GwtEvent;

public class DataWatchEvent extends GwtEvent<DataWatchEventHandler> {
	
	public static final Type<DataWatchEventHandler> TYPE = new Type<DataWatchEventHandler>();
	
	public static enum DATA{
		INVOICE,
		ESTIMATION,
		CLIENT,
		CLIENT_DATA,
		STATS,
		BUSINESS
	}
	
	private final DATA data;
	
	public DataWatchEvent(DATA data) {
		this.data = data;
	}
	

	@Override
	public Type<DataWatchEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DataWatchEventHandler handler) {
		handler.onDataUpdated(data);
	}

}
