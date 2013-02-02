package com.novadart.novabill.frontend.client.widget.list.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellList.Resources;

public interface QuickViewListResources extends Resources {
	public static final QuickViewListResources INSTANCE = GWT.create(QuickViewListResources.class);

	@Source("CellListStyle.css")
	CellListStyle cellListStyle();
	
}
