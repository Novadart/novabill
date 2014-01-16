package com.novadart.novabill.frontend.client.bridge.ui;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;

public class HTMLWrapper implements AcceptsOneWidget {

	private final String elementId;
	
	public HTMLWrapper(String elementId) {
		this.elementId = elementId;
	}
	
	@Override
	public void setWidget(IsWidget w) {
		RootPanel panel = RootPanel.get(elementId);
		panel.clear();
		panel.add(w);
	}

}
