package com.novadart.novabill.frontend.client.widget.validation;

import com.novadart.gwtshared.client.validation.*;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.i18n.I18NM;

public class ValidationKit {
	
	public static final DefaultValidation<String> DEFAULT = new DefaultValidation<String>();

	public static final EmailValidation EMAIL = new EmailValidation() {

		@Override
		public String getErrorMessage() {
			return I18N.INSTANCE.emailValidationError();
		}
	};

	public static final EmailValidation OPTIONAL_EMAIL = new EmailValidation(true) {

		@Override
		public String getErrorMessage() {
			return I18N.INSTANCE.emailValidationError();
		}
	};

	public static final NotEmptyValidation NOT_EMPTY = new NotEmptyValidation() {

		@Override
		public String getErrorMessage() {
			return I18N.INSTANCE.notEmptyValidationError();
		}
	};
	
	public static final NotEmptyDateValidation NOT_EMPTY_DATE = new NotEmptyDateValidation() {

		@Override
		public String getErrorMessage() {
			return I18N.INSTANCE.notEmptyDateValidationError();
		}
	};

	public static final NumberValidation NUMBER = new NumberValidation() {

		@Override
		public String getErrorMessage() {
			return I18N.INSTANCE.numberValidationError();
		}
	};

	public static final NumberValidation PERCENT_NUMBER = new NumberValidation() {

		@Override
		public boolean isValid(String text) {
			if(text.isEmpty()){
				return isOptional();
			}

			if(Validation.isPositiveNumber(text)){
				int value = Integer.parseInt(text);
				return value >= 0 && value <= 100;
			}

			return false;
		}

		@Override
		public String getErrorMessage() {
			return I18N.INSTANCE.numberValidationError();
		}
	};

	public static final NumberValidation OPTIONAL_NUMBER = new NumberValidation(true) {

		@Override
		public String getErrorMessage() {
			return I18N.INSTANCE.numberValidationError();
		}
	};

	public static final PostcodeValidation POSTCODE = new PostcodeValidation() {

		@Override
		public String getErrorMessage() {
			return I18N.INSTANCE.postcodeValidationError();
		}
	};

	public static final SsnOrVatIdValidation SSN_OR_VAT_ID = new SsnOrVatIdValidation();

	public static final SsnValidation SSN = new SsnValidation() {

		@Override
		public String getErrorMessage() {
			return I18N.INSTANCE.ssnValidationError();
		}
	};

	public static final VatIdValidation VAT_ID = new VatIdValidation() {

		@Override
		public String getErrorMessage() {
			return I18N.INSTANCE.vatidValidationError();
		}
	};

	public static final VatIdValidation OPTIONAL_VAT_ID = new VatIdValidation(true) {

		@Override
		public String getErrorMessage() {
			return I18N.INSTANCE.vatidValidationError();
		}
	};
	
	public static final TextLengthValidation OPTIONAL_TEXT_LENGTH = new TextLengthValidation(1500) {
		
		@Override
		public String getErrorMessage() {
			return I18NM.get.textLengthError(1500);
		}
	}; 
	
}
