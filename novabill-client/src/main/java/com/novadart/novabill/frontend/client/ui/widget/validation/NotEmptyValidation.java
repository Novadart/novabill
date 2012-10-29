package com.novadart.novabill.frontend.client.ui.widget.validation;

import com.novadart.novabill.frontend.client.i18n.I18N;

public class NotEmptyValidation extends com.novadart.gwtshared.client.validation.NotEmptyValidation {

	@Override
	public String getErrorMessage() {
		return I18N.INSTANCE.notEmptyValidationError();
	}

}
