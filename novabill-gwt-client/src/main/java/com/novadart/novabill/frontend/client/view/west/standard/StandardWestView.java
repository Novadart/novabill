package com.novadart.novabill.frontend.client.view.west.standard;

import com.google.gwt.user.cellview.client.CellList;
import com.novadart.novabill.frontend.client.view.View;
import com.novadart.novabill.frontend.client.widget.search.ClientSearch;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public interface StandardWestView extends View<StandardWestView.Presenter> {
	
	public static interface Presenter extends com.novadart.novabill.frontend.client.presenter.Presenter {
		
		void onAddClientClicked();
		
		void onLoad();
	}

	ClientSearch getClientSearch();
	
	CellList<ClientDTO> getClientList();

}
