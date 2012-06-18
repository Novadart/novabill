package com.novadart.novabill.frontend.client.datawatcher;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.novadart.novabill.frontend.client.datawatcher.DataWatchEvent.DATA;

public class DataWatcher {

	private static final DataWatcher instance = new DataWatcher();

	public static DataWatcher getInstance() {
		return instance;
	}

	
	private final HandlerManager handlerManager;
	
	private DataWatcher() {
		handlerManager = new HandlerManager(this);
	}
	
	public void fireInvoiceEvent(){
		handlerManager.fireEvent(new DataWatchEvent(DATA.INVOICE));
	}
	
	public void fireEstimationEvent(){
		handlerManager.fireEvent(new DataWatchEvent(DATA.ESTIMATION));
	}
	
	public void fireClientEvent(){
		handlerManager.fireEvent(new DataWatchEvent(DATA.CLIENT));
	}
	
	public void fireClientDataEvent(){
		handlerManager.fireEvent(new DataWatchEvent(DATA.CLIENT_DATA));
	}
	
	public void fireStatsEvent(){
		handlerManager.fireEvent(new DataWatchEvent(DATA.STATS));
	}
	
	public void fireBusinessEvent(){
		handlerManager.fireEvent(new DataWatchEvent(DATA.BUSINESS));
	}
	
	
	public HandlerRegistration addDataEventHandler(DataWatchEventHandler handler){
		return handlerManager.addHandler(DataWatchEvent.TYPE, handler);
	}

}
