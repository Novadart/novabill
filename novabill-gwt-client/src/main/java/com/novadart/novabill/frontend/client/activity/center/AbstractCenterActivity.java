package com.novadart.novabill.frontend.client.activity.center;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Image;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.activity.BasicActivity;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.resources.ImageResources;

public abstract class AbstractCenterActivity extends BasicActivity {
	
	private static final Image LOADER = new Image(ImageResources.INSTANCE.loader());
	
	static{
		LOADER.setStyleName(GlobalBundle.INSTANCE.globalCss().centerViewLoader());
	}
	
	private final JavaScriptObject callback;
	
	public AbstractCenterActivity(ClientFactory clientFactory) {
		super(clientFactory);
		this.callback = null;
	}
	
	public AbstractCenterActivity(ClientFactory clientFactory, JavaScriptObject callback) {
		super(clientFactory);
		this.callback = callback;
	}
	
	public JavaScriptObject getCallback() {
		return callback;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(LOADER);
	}

}
