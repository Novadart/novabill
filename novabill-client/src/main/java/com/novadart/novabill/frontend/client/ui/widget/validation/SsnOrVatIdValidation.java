package com.novadart.novabill.frontend.client.ui.widget.validation;

import com.novadart.novabill.frontend.client.i18n.I18N;

public class SsnOrVatIdValidation extends OptionalFieldValidation<String> {
	private static final SsnValidation SSN_VALIDATION = new SsnValidation();
	private static final VatIdValidation VAT_ID_VALIDATION = new VatIdValidation();

	public SsnOrVatIdValidation() {
		super(false);
	}
	
	public SsnOrVatIdValidation(boolean optional) {
		super(optional);
	}
	
	@Override
	public boolean isValid(String text) {
		return isOptional() ? (text.isEmpty() || SSN_VALIDATION.isValid(text) || VAT_ID_VALIDATION.isValid(text))
				: SSN_VALIDATION.isValid(text) || VAT_ID_VALIDATION.isValid(text);
	}

	@Override
	public String getErrorMessage() {
		return I18N.INSTANCE.ssnOrVatIdValidationError();
	}

}
