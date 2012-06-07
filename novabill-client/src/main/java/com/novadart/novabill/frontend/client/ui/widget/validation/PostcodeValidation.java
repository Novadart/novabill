package com.novadart.novabill.frontend.client.ui.widget.validation;

import com.novadart.gwtshared.client.validation.Validation;
import com.novadart.gwtshared.client.validation.ValidationBundle;
import com.novadart.novabill.frontend.client.i18n.I18N;

public class PostcodeValidation implements ValidationBundle<String> {

	@Override
	public boolean isValid(String text) {
		return Validation.isInteger(text) && text.length() == 5;
	}

	@Override
	public String getErrorMessage() {
		return I18N.INSTANCE.postcodeValidationError();
	}

}
