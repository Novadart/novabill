package com.novadart.novabill.frontend.client.ui.widget.validation;

import com.novadart.novabill.frontend.client.i18n.I18N;

public class NumberValidation extends com.novadart.gwtshared.client.validation.NumberValidation {

	public NumberValidation(boolean optional) {
		super(optional);
	}
	
	public NumberValidation() {
		super(false);
	}
	
	@Override
	public String getErrorMessage() {
		return I18N.INSTANCE.numberValidationError();
	}

}
