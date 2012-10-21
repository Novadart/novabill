package com.novadart.novabill.frontend.client.ui.center;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.novadart.novabill.frontend.client.util.WidgetUtils;

public abstract class AccountDocument extends Composite {

	@Override
	protected void onLoad() {
		super.onLoad();
		WidgetUtils.setElementHeightToFillSpace(getBody(), getElement(), getNonBodyElements());
	}
	
	protected abstract Element[] getNonBodyElements();
	
	protected abstract Element getBody();
}
