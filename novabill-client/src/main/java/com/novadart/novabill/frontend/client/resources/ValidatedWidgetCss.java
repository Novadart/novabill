package com.novadart.novabill.frontend.client.resources;

import com.novadart.gwtshared.client.validation.widget.ValidatedWidget;

public interface ValidatedWidgetCss extends ValidatedWidget.Style {

	String validationError();

	String validationOk();

	String validationMessage();

	String validationBaloon();

}
