package com.novadart.novabill.frontend.client.ui.center;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Composite;

public abstract class AccountDocument extends Composite {

	@Override
	protected void onLoad() {
		super.onLoad();
		
		int myHeight = getElement().getOffsetHeight();
		int nonBodySize = 0;
		for (Element elm : getNonBodyElements()) {
			nonBodySize += elm.getOffsetHeight();
		}
		getBody().getStyle().setHeight(myHeight - nonBodySize, Unit.PX);
	}
	
	protected abstract Element[] getNonBodyElements();
	
	protected abstract Element getBody();
}
