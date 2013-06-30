package com.novadart.novabill.frontend.client.widget.list;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.view.client.NoSelectionModel;
import com.novadart.novabill.frontend.client.resources.ImageResources;
import com.novadart.novabill.frontend.client.widget.list.resources.QuickViewListBundle;
import com.novadart.novabill.frontend.client.widget.list.resources.QuickViewListResources;

public class QuickViewList<T> extends CellList<T> {
	
	static{
		QuickViewListResources.INSTANCE.cellListStyle().ensureInjected();
		QuickViewListBundle.INSTANCE.quickViewListCss().ensureInjected();
	}
	
	public QuickViewList(QuickViewCell<T> cell) {
		super(cell, QuickViewListResources.INSTANCE);
		setLoadingIndicator(new Image(ImageResources.INSTANCE.loader()));
		setSelectionModel(new NoSelectionModel<T>());
	}
	
}
