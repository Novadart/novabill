package com.novadart.novabill.frontend.client.presenter;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public interface Presenter {
	void goTo(Place place);
	void go(AcceptsOneWidget panel);
}