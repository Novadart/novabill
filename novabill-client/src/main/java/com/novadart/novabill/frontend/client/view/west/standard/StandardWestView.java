package com.novadart.novabill.frontend.client.view.west.standard;

import com.novadart.novabill.frontend.client.view.View;
import com.novadart.novabill.frontend.client.widget.search.ClientSearch;

public interface StandardWestView extends View<StandardWestView.Presenter> {
	
	public static interface Presenter extends com.novadart.novabill.frontend.client.presenter.Presenter {
		
		void onAddClientClicked();
		
		void onLoad();
	}

	ClientSearch getClientSearch();

}
