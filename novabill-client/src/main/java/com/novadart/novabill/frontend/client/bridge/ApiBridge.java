package com.novadart.novabill.frontend.client.bridge;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ApiBridge {

	void inject(AsyncCallback<Void> callback);
}
