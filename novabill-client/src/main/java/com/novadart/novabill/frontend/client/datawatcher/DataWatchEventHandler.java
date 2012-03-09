package com.novadart.novabill.frontend.client.datawatcher;

import com.google.gwt.event.shared.EventHandler;
import com.novadart.novabill.frontend.client.datawatcher.DataWatchEvent.DATA;

public interface DataWatchEventHandler extends EventHandler {
	public void onDataUpdated(DATA data);
}
