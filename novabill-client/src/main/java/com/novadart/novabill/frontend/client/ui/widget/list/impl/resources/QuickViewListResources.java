package com.novadart.novabill.frontend.client.ui.widget.list.impl.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface QuickViewListResources extends ClientBundle	 {
	public static final QuickViewListResources INSTANCE = GWT.create(QuickViewListResources.class);

	@Source("QuickViewListStyle.css")
	QuickViewListStyle quickViewListStyle();
	
}
