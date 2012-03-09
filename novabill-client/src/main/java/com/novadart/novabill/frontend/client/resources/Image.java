package com.novadart.novabill.frontend.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Image extends ClientBundle {
	public static final Image get = GWT.create(Image.class);

	ImageResource bullet();

	ImageResource pdf();

}
