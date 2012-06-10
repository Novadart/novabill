package com.novadart.novabill.frontend.client.ui.widget.validation;

import com.google.gwt.regexp.shared.RegExp;
import com.novadart.gwtshared.client.validation.ValidationBundle;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.shared.client.validation.RegularExpressionConstants;

public class SsnValidation implements ValidationBundle<String> {
	
	private static final RegExp SSN_REGEXP = 
			RegExp.compile(RegularExpressionConstants.SSN_REGEX);

	private final boolean canBeEmpty;
	
	public SsnValidation() {
		this(false);
	}
	
	public SsnValidation(boolean canbeEmpty) {
		this.canBeEmpty = canbeEmpty;
	}

	@Override
	public boolean isValid(String text) {
		return canBeEmpty ? (text.isEmpty() || isSSN(text)) : isSSN(text);
	}

	@Override
	public String getErrorMessage() {
		return I18N.INSTANCE.ssnValidationError();
	}

	public static boolean isSSN(String text){
		return SSN_REGEXP.exec(text) != null;
	}
}
