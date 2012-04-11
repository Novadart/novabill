package com.novadart.novabill.frontend.client.ui.widget.validation;

import com.novadart.gwtshared.client.validation.Validation;
import com.novadart.gwtshared.client.validation.ValidationBundle;
import com.novadart.novabill.frontend.client.i18n.I18N;

public class NumberValidation implements ValidationBundle {

	private final boolean canBeEmpty;

	public NumberValidation() {
		this(false);
	}

	public NumberValidation(boolean canbeEmpty) {
		this.canBeEmpty = canbeEmpty;
	}

	@Override
	public boolean isValid(String text) {
		return canBeEmpty ? (text.isEmpty() || Validation.isPositiveNumber(text)) : Validation.isPositiveNumber(text);
	}

	@Override
	public String getErrorMessage() {
		return I18N.get.numberValidationError();
	}

}
