package com.novadart.novabill.frontend.client.view.widget.validation;

import com.novadart.gwtshared.client.validation.OptionalFieldValidation;
import com.novadart.novabill.frontend.client.i18n.I18N;

class SsnOrVatIdValidation extends OptionalFieldValidation<String> {
	
	SsnOrVatIdValidation() {
		super(false);
	}
	
	SsnOrVatIdValidation(boolean optional) {
		super(optional);
	}
	
	@Override
	public boolean isValid(String text) {
		return isOptional() ? (text.isEmpty() || ValidationKit.SSN.isValid(text) || ValidationKit.VAT_ID.isValid(text))
				: ValidationKit.SSN.isValid(text) || ValidationKit.VAT_ID.isValid(text);
	}

	@Override
	public String getErrorMessage() {
		return I18N.INSTANCE.ssnOrVatIdValidationError();
	}

}
