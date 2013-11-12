package com.novadart.novabill.frontend.client.bridge.server;

import com.google.gwt.core.shared.GWT;
import com.novadart.novabill.frontend.client.facade.ServerFacade;

abstract class ServiceJS {

	protected static final ServerFacade SERVER_FACADE = GWT.create(ServerFacade.class);

}
