package com.novadart.novabill.frontend.client.ui.widget.list;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.view.client.NoSelectionModel;
import com.novadart.novabill.frontend.client.ui.widget.list.impl.resources.QuickViewListResources;

public class QuickViewList<T> extends CellList<T> {
	
	static{
		QuickViewListResources.INSTANCE.quickViewListStyle().ensureInjected();
	}
	
	public QuickViewList(QuickViewCell<T> cell) {
		super(cell);
		
		setSelectionModel(new NoSelectionModel<T>());
		setPageSize(10);
		
		setStyleName("QuickViewList");
	}
	
}
