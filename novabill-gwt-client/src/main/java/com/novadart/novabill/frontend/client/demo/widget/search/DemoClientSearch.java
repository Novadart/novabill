package com.novadart.novabill.frontend.client.demo.widget.search;

import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.novadart.novabill.frontend.client.demo.i18n.DemoMessages;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.search.ClientSearch;

public class DemoClientSearch extends ClientSearch {

	public DemoClientSearch() {
		getSearchInput().setReadOnly(true);
		getSearchInput().addFocusHandler(new FocusHandler() {
			
			@Override
			public void onFocus(FocusEvent event) {
				Notification.showMessage(DemoMessages.INSTANCE.searchNotAvailable());
			}
		});
	}
}
