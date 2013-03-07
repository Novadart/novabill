package com.novadart.novabill.frontend.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ImageResources extends ClientBundle {
	public static final ImageResources INSTANCE = GWT.create(ImageResources.class);

	ImageResource loader();

	ImageResource clear_left();
	
	ImageResource delete();

	ImageResource fancy_close();

	ImageResource edit();

}
