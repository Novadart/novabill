package com.novadart.novabill.frontend.client.widget;

import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.widget.validation.ValidationKit;



public class ValidatedTextArea extends com.novadart.gwtshared.client.validation.widget.ValidatedTextArea {
	
	public ValidatedTextArea() {
		super(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.TEXT_LENGTH);
	}
	
}
