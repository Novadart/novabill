package com.novadart.novabill.frontend.client.view.center;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.util.WidgetUtils;

public abstract class AccountDocument extends Composite {
	
	protected static final AccountDocumentCss CSS = GWT.create(AccountDocumentCss.class);

	@Override
	protected void onLoad() {
		super.onLoad();
		WidgetUtils.setElementHeightToFillSpace(getBody(), getElement(), getNonBodyElements());
	}
	
	protected abstract Element[] getNonBodyElements();
	
	protected abstract Element getBody();
	
	protected abstract ValidatedTextBox getNumber();
	
	protected abstract ScrollPanel getDocScroll();
	
}
