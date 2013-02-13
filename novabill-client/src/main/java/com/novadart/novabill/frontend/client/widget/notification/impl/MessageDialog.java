package com.novadart.novabill.frontend.client.widget.notification.impl;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.frontend.client.widget.notification.NotificationDialog;

public class MessageDialog extends NotificationDialog<Void> {

	public MessageDialog(NotificationCallback<Void> onClose) {
		super(onClose);
	}

	@Override
	protected void addButtons(FlowPanel buttons) {
		Button ok = new Button("ok");
		ok.setStyleName(GlobalBundle.INSTANCE.globalCss().button());
		ok.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		buttons.add(ok);
	}

	@Override
	protected Void getValue() {
		return null;
	}

}
