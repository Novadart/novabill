package com.novadart.novabill.frontend.client.demo.resources;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface DemoImageResources extends ClientBundle {
	public static final DemoImageResources INSTANCE = GWT.create(DemoImageResources.class);
	
	ImageResource invoice();
}
