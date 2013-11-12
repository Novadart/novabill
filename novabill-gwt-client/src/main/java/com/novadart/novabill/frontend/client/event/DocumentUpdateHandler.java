package com.novadart.novabill.frontend.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface DocumentUpdateHandler extends EventHandler {
	
	public void onDocumentUpdate(DocumentUpdateEvent event);

}
