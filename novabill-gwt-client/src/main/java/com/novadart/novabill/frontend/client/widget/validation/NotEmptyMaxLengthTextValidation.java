package com.novadart.novabill.frontend.client.widget.validation;

import com.novadart.gwtshared.client.validation.TextLengthValidation;
import com.novadart.novabill.frontend.client.i18n.I18NM;

public class NotEmptyMaxLengthTextValidation extends TextLengthValidation {

	public NotEmptyMaxLengthTextValidation(int maxLength) {
		super(maxLength);
	}

	@Override
	public boolean isValid(String value) {
		return !value.isEmpty() && super.isValid(value);
	}
	
	@Override
	public String getErrorMessage() {
		return I18NM.get.textLengthError(getMaxLength());
	}
	
	

}
