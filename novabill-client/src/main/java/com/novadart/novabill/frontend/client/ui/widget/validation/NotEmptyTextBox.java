package com.novadart.novabill.frontend.client.ui.widget.validation;

import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.i18n.I18N;

public class NotEmptyTextBox extends ValidatedTextBox {

	public NotEmptyTextBox() {
		super(I18N.get.notEmptyValidationError());
	}

	@Override
	protected boolean validate(String text) {
		return !text.isEmpty();
	}

}
