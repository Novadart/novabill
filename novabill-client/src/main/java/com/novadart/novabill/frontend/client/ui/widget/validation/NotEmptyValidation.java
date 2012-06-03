package com.novadart.novabill.frontend.client.ui.widget.validation;

import com.novadart.gwtshared.client.validation.ValidationBundle;
import com.novadart.novabill.frontend.client.i18n.I18N;

public class NotEmptyValidation implements ValidationBundle<String> {

	@Override
	public boolean isValid(String text) {
		return !text.isEmpty();
	}

	@Override
	public String getErrorMessage() {
		return I18N.INSTANCE.notEmptyValidationError();
	}

}
