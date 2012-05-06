package com.novadart.novabill.frontend.client.ui.widget.validation;

import com.novadart.gwtshared.client.validation.Validation;
import com.novadart.gwtshared.client.validation.ValidationBundle;
import com.novadart.novabill.frontend.client.i18n.I18N;

public class EmailValidation implements ValidationBundle<String> {

	private final boolean canBeEmpty;

	public EmailValidation() {
		this(false);
	}

	public EmailValidation(boolean canbeEmpty) {
		this.canBeEmpty = canbeEmpty;
	}

	@Override
	public boolean isValid(String value) {
		return canBeEmpty ? (value.isEmpty() || Validation.isEmail(value)) : Validation.isEmail(value);
	}

	@Override
	public String getErrorMessage() {
		return I18N.get.emailValidationError();
	}
}
