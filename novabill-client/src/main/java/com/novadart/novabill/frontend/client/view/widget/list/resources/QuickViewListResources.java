package com.novadart.novabill.frontend.client.view.widget.list.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellList.Resources;

public interface QuickViewListResources extends Resources {
	public static final QuickViewListResources INSTANCE = GWT.create(QuickViewListResources.class);

	@Source("QuickViewListStyle.css")
	QuickViewListStyle quickViewListStyle();

	@Source("CellListStyle.css")
	CellListStyle cellListStyle();
	
}
