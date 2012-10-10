package com.novadart.novabill.frontend.client.ui.center;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Composite;

public abstract class AccountDocument extends Composite {

	@Override
	protected void onLoad() {
		super.onLoad();
		
		int myHeight = getElement().getOffsetHeight();
		int headerHeight = getHeader().getOffsetHeight();
		getBody().getStyle().setHeight(myHeight - headerHeight, Unit.PX);
	}
	
	protected abstract Element getHeader();
	
	protected abstract Element getBody();
}
