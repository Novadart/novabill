package com.novadart.novabill.frontend.client.ui.widget;

import com.novadart.novabill.frontend.client.ui.widget.validation.TextLengthValidation;


public class ValidatedTextArea extends com.novadart.gwtshared.client.validation.widget.ValidatedTextArea {
	
	private static final TextLengthValidation TEXT_LENGTH_VALIDATION = new TextLengthValidation();
	
	public ValidatedTextArea() {
		super(TEXT_LENGTH_VALIDATION);
	}
	
}
