package com.novadart.novabill.frontend.client.ui.widget.validation;

import com.novadart.gwtshared.client.validation.Validation;
import com.novadart.gwtshared.client.validation.ValidationBundle;
import com.novadart.novabill.frontend.client.i18n.I18N;

public class EmailValidation implements ValidationBundle {

	private final boolean canBeEmpty;

	public EmailValidation() {
		this(false);
	}

	public EmailValidation(boolean canbeEmpty) {
		this.canBeEmpty = canbeEmpty;
	}

	@Override
	public boolean isValid(String text) {
		return canBeEmpty ? (text.isEmpty() || Validation.isEmail(text)) : Validation.isEmail(text);
	}

	@Override
	public String getErrorMessage() {
		return I18N.get.emailValidationError();
	}
}
