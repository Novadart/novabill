package com.novadart.novabill.frontend.client.ui.widget.validation;

public abstract class TextLengthValidation extends com.novadart.gwtshared.client.validation.TextLengthValidation {

	public TextLengthValidation() {
		super(1500);
	}
	
	@Override
	public String getErrorMessage() {
		return null;
	}


}
