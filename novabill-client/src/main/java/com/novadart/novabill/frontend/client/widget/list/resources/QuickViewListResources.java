package com.novadart.novabill.frontend.client.widget.list.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellList;

public interface QuickViewListResources extends CellList.Resources {
	public static final QuickViewListResources INSTANCE = GWT.create(QuickViewListResources.class);

	
}
