package com.novadart.novabill.frontend.client.ui.widget.validation;

import com.novadart.gwtshared.client.validation.Validate;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.i18n.I18N;

public class VatIdTextBox extends ValidatedTextBox {
	
	private final boolean canBeEmpty;
	
	public VatIdTextBox(boolean canbeEmpty) {
		super(I18N.get.vatidValidationError());
		this.canBeEmpty = canbeEmpty;
	}

	@Override
	protected boolean validate(String text) {
		return canBeEmpty ? (getText().isEmpty() || Validate.isVatId(text)) : Validate.isVatId(text);
	}

}
