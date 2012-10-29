package com.novadart.novabill.frontend.client.ui.widget.validation;

import com.novadart.gwtshared.client.validation.ValidationBundle;

public abstract class OptionalFieldValidation<ValueType> implements ValidationBundle<ValueType> {
	
	private final boolean optional;
	
	public OptionalFieldValidation(boolean optional) {
		this.optional = optional;
	}
	
	public boolean isOptional() {
		return optional;
	}
}
