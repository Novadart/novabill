package com.novadart.novabill.frontend.client.view.west.configuration;

import com.google.gwt.user.client.ui.Hyperlink;
import com.novadart.novabill.frontend.client.view.View;
import com.novadart.novabill.frontend.client.view.west.configuration.ConfigurationWestViewImpl.Style;

public interface ConfigurationWestView extends View<ConfigurationWestView.Presenter> {

	public static interface Presenter extends com.novadart.novabill.frontend.client.presenter.Presenter {
		
		void onLoad();
	}

	Hyperlink getPayment();

	Hyperlink getGeneral();

	Style getStyle();
}
