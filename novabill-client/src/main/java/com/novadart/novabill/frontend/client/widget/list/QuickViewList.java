package com.novadart.novabill.frontend.client.widget.list;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.view.client.NoSelectionModel;
import com.novadart.novabill.frontend.client.widget.list.resources.QuickViewListResources;

public class QuickViewList<T> extends CellList<T> {
	
	static{
		QuickViewListResources.INSTANCE.cellListStyle().ensureInjected();
	}
	
	public QuickViewList(QuickViewCell<T> cell) {
		super(cell, QuickViewListResources.INSTANCE);
		
		setSelectionModel(new NoSelectionModel<T>());
		setStyleName("QuickViewList");
	}
	
}
