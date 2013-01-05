package com.novadart.novabill.frontend.client.view;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface View extends IsWidget {

	public void setPresenter(Presenter presenter);
	public void setClean();
	
	public interface Presenter {
		public void goTo(Place place);
	}

}
