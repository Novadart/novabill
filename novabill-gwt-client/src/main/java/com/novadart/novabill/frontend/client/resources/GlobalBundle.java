package com.novadart.novabill.frontend.client.resources;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface GlobalBundle extends ClientBundle {
	public static final GlobalBundle INSTANCE = GWT.create(GlobalBundle.class);

	@Source("LoaderButton.css")
	LoaderButtonCss loaderButton();

	@Source("ValidatedWidgetCss.css")
	ValidatedWidgetCss validatedWidget();

	@Source("RichTextBoxCss.css")
	RichTextBoxCss richTextBoxCss();
	
	@Source("RichTextAreaCss.css")
	RichTextAreaCss richTextAreaCss();

	@Source("DialogCss.css")
	DialogCss dialog();

	@Source("GlobalCss.css")
	GlobalCss globalCss();

}
