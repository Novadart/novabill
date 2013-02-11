package com.novadart.novabill.frontend.client.resources;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface GlobalBundle extends ClientBundle {
	public static final GlobalBundle INSTANCE = GWT.create(GlobalBundle.class);

	@Source("LoaderButton.css")
	LoaderButtonCss loaderButton();

}
