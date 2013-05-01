package com.novadart.novabill.frontend.client.demo.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.novadart.novabill.frontend.client.demo.i18n.DemoMessages;
import com.novadart.novabill.frontend.client.view.MainWidgetImpl;
import com.novadart.novabill.frontend.client.widget.notification.Notification;

public class DemoMainWidgetImpl extends MainWidgetImpl {
	
	public DemoMainWidgetImpl() {
		changePasswordAnchor.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				event.preventDefault();
				Notification.showMessage(DemoMessages.INSTANCE.functionNotAvailable());
			}
		});
	}
	
	@Override
	protected void onFeedbackClicked(ClickEvent e) {
		Notification.showMessage(DemoMessages.INSTANCE.functionNotAvailable());
	}

}
