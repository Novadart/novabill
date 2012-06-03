package com.novadart.novabill.frontend.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ImageResources extends ClientBundle {
	public static final ImageResources INSTANCE = GWT.create(ImageResources.class);

	ImageResource bullet();

	ImageResource pdf();

	ImageResource loader();

	ImageResource delete();

}
