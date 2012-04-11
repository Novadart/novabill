package com.novadart.novabill.frontend.client.ui.widget.validation;

import com.novadart.gwtshared.client.validation.Validation;
import com.novadart.gwtshared.client.validation.ValidationBundle;
import com.novadart.novabill.frontend.client.i18n.I18N;

public class SsnValidation implements ValidationBundle {

	private final boolean canBeEmpty;
	
	public SsnValidation() {
		this(false);
	}
	
	public SsnValidation(boolean canbeEmpty) {
		this.canBeEmpty = canbeEmpty;
	}

	@Override
	public boolean isValid(String text) {
		return canBeEmpty ? (text.isEmpty() || Validation.isSSN(text)) : Validation.isSSN(text);
	}

	@Override
	public String getErrorMessage() {
		return I18N.get.ssnValidationError();
	}

}
