package com.novadart.novabill.frontend.client.ui.widget.validation;

import com.google.gwt.regexp.shared.RegExp;
import com.novadart.gwtshared.client.validation.ValidationBundle;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.shared.client.validation.RegularExpressionConstants;

public class VatIdValidation implements ValidationBundle<String> {
	
	private static final RegExp VATID_REGEXP = 
			RegExp.compile(RegularExpressionConstants.VAT_ID_REGEX);
	
	private final boolean canBeEmpty;
	
	public VatIdValidation() {
		this(false);
	}
	
	public VatIdValidation(boolean canbeEmpty) {
		this.canBeEmpty = canbeEmpty;
	}

	@Override
	public boolean isValid(String text) {
		return canBeEmpty ? (text.isEmpty() || isVatId(text)) : isVatId(text);
	}

	@Override
	public String getErrorMessage() {
		return I18N.INSTANCE.vatidValidationError();
	}
	
	public static boolean isVatId(String text){
		return VATID_REGEXP.exec(text) != null;
	}

}
