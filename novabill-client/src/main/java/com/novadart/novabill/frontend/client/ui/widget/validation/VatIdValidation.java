package com.novadart.novabill.frontend.client.ui.widget.validation;

import com.novadart.novabill.frontend.client.i18n.I18N;

public class VatIdValidation extends com.novadart.gwtshared.client.validation.VatIdValidation {

	public VatIdValidation(boolean optional) {
		super(optional);
	}
	
	public VatIdValidation() {
		super(false);
	}
	
	@Override
	public String getErrorMessage() {
		return I18N.INSTANCE.vatidValidationError();
	}
	
}
