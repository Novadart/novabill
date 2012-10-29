package com.novadart.novabill.frontend.client.ui.widget.validation;

import com.novadart.gwtshared.client.validation.Validation;
import com.novadart.novabill.frontend.client.i18n.I18N;

public class NumberValidation extends OptionalFieldValidation<String> {

	public NumberValidation() {
		super(false);
	}

	public NumberValidation(boolean optional) {
		super(optional);
	}

	@Override
	public boolean isValid(String text) {
		return isOptional() ? (text.isEmpty() || Validation.isPositiveNumber(text)) : Validation.isPositiveNumber(text);
	}

	@Override
	public String getErrorMessage() {
		return I18N.INSTANCE.numberValidationError();
	}

}
