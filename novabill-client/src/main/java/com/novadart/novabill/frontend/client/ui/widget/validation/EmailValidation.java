package com.novadart.novabill.frontend.client.ui.widget.validation;

import com.novadart.gwtshared.client.validation.Validation;
import com.novadart.novabill.frontend.client.i18n.I18N;

public class EmailValidation extends OptionalFieldValidation<String> {

	public EmailValidation() {
		super(false);
	}

	public EmailValidation(boolean optional) {
		super(optional);
	}

	@Override
	public boolean isValid(String value) {
		return isOptional() ? (value.isEmpty() || Validation.isEmail(value)) : Validation.isEmail(value);
	}

	@Override
	public String getErrorMessage() {
		return I18N.INSTANCE.emailValidationError();
	}
}
