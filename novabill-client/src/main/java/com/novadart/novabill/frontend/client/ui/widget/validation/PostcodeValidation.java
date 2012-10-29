package com.novadart.novabill.frontend.client.ui.widget.validation;

import com.novadart.novabill.frontend.client.i18n.I18N;

public class PostcodeValidation extends com.novadart.gwtshared.client.validation.PostcodeValidation {

	@Override
	public String getErrorMessage() {
		return I18N.INSTANCE.postcodeValidationError();
	}

}
