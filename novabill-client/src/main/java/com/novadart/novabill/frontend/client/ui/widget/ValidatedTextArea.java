package com.novadart.novabill.frontend.client.ui.widget;

import com.novadart.novabill.frontend.client.ui.widget.validation.ValidationKit;



public class ValidatedTextArea extends com.novadart.gwtshared.client.validation.widget.ValidatedTextArea {
	
	public ValidatedTextArea() {
		super(ValidationKit.TEXT_LENGTH);
	}
	
}
