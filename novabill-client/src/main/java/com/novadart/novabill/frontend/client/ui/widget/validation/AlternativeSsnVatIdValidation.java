package com.novadart.novabill.frontend.client.ui.widget.validation;

import com.novadart.gwtshared.client.validation.composite.AlternativeValidation;
import com.novadart.novabill.frontend.client.i18n.I18N;

public class AlternativeSsnVatIdValidation extends AlternativeValidation {

	@Override
	public String getErrorMessage() {
		return I18N.INSTANCE.ssnOrVatIdValidationError();
	}

}