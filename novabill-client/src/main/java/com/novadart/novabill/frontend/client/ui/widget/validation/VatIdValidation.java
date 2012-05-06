package com.novadart.novabill.frontend.client.ui.widget.validation;

import com.novadart.gwtshared.client.validation.Validation;
import com.novadart.gwtshared.client.validation.ValidationBundle;
import com.novadart.novabill.frontend.client.i18n.I18N;

public class VatIdValidation implements ValidationBundle<String> {
	
	private final boolean canBeEmpty;
	
	public VatIdValidation() {
		this(false);
	}
	
	public VatIdValidation(boolean canbeEmpty) {
		this.canBeEmpty = canbeEmpty;
	}

	@Override
	public boolean isValid(String text) {
		return canBeEmpty ? (text.isEmpty() || Validation.isVatId(text)) : Validation.isVatId(text);
	}

	@Override
	public String getErrorMessage() {
		return I18N.get.vatidValidationError();
	}

}
