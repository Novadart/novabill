package com.novadart.novabill.frontend.client.ui.widget.validation;

import com.novadart.gwtshared.client.validation.ValidationBundle;
import com.novadart.novabill.frontend.client.i18n.I18N;

public class SsnOrVatIdValidation implements ValidationBundle {
	private static final SsnValidation SSN_VALIDATION = new SsnValidation();
	private static final VatIdValidation VAT_ID_VALIDATION = new VatIdValidation();

	@Override
	public boolean isValid(String text) {
		return SSN_VALIDATION.isValid(text) || VAT_ID_VALIDATION.isValid(text);
	}

	@Override
	public String getErrorMessage() {
		return I18N.get.ssnOrVatIdValidationError();
	}

}
