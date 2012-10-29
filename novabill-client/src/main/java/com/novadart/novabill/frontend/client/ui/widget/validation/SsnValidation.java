package com.novadart.novabill.frontend.client.ui.widget.validation;

import com.novadart.novabill.frontend.client.i18n.I18N;

public class SsnValidation extends com.novadart.gwtshared.client.validation.SsnValidation {

	@Override
	public String getErrorMessage() {
		return I18N.INSTANCE.ssnValidationError();
	}

}
