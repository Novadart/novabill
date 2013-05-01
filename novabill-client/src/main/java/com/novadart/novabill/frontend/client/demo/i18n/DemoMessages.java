package com.novadart.novabill.frontend.client.demo.i18n;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.Constants;

public interface DemoMessages extends Constants {
	public static final DemoMessages INSTANCE = GWT.create(DemoMessages.class);
	
	String functionNotAvailable();
	String searchNotAvailable();
}
