package com.novadart.novabill.frontend.client.activity.center;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Image;
import com.novadart.gwtshared.client.resources.ImageResources;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.activity.BasicActivity;

public abstract class AbstractCenterActivity extends BasicActivity {
	
	private static final Image LOADER = new Image(ImageResources.INSTANCE.loader());
	
	static{
		LOADER.setStyleName("centerViewLoader");
	}

	public AbstractCenterActivity(ClientFactory clientFactory) {
		super(clientFactory);
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(LOADER);
	}

}
