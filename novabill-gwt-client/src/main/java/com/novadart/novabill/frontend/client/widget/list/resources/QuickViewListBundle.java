package com.novadart.novabill.frontend.client.widget.list.resources;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface QuickViewListBundle extends ClientBundle {
	public static QuickViewListBundle INSTANCE = GWT.create(QuickViewListBundle.class);

	@Source("QuickViewListCss.css")
	QuickViewListCss quickViewListCss();

}
