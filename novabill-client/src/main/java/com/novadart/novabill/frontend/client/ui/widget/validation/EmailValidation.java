package com.novadart.novabill.frontend.client.ui.widget.validation;

import com.novadart.novabill.frontend.client.i18n.I18N;

public class EmailValidation extends com.novadart.gwtshared.client.validation.EmailValidation {

	public EmailValidation(boolean optional) {
		super(optional);
	}
	
	public EmailValidation() {
		super(false);
	}

	@Override
	public String getErrorMessage() {
		return I18N.INSTANCE.emailValidationError();
	}
}
