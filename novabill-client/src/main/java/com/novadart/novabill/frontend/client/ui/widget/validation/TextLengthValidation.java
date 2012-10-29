package com.novadart.novabill.frontend.client.ui.widget.validation;

import com.novadart.novabill.frontend.client.i18n.I18NM;

public class TextLengthValidation extends com.novadart.gwtshared.client.validation.TextLengthValidation {

	public TextLengthValidation() {
		super(1500);
	}
	
	@Override
	public String getErrorMessage() {
		return I18NM.get.textLengthError(1500);
	}


}
