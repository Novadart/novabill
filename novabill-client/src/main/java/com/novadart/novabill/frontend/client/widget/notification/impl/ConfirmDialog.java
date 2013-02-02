package com.novadart.novabill.frontend.client.widget.notification.impl;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.frontend.client.widget.notification.NotificationDialog;

public class ConfirmDialog extends NotificationDialog<Boolean> {

	private Boolean value = false;
	
	public ConfirmDialog(NotificationCallback<Boolean> onClose) {
		super(onClose);
	}

	@Override
	protected void addButtons(FlowPanel buttons) {
		Button button = new Button(I18N.INSTANCE.yes());
		button.setStyleName("button");
		button.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				value = true;
				hide();
			}
		});
		buttons.add(button);
		
		button = new Button(I18N.INSTANCE.no());
		button.setStyleName("button");
		button.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				value = false;
				hide();
			}
		});
		buttons.add(button);
	}

	@Override
	protected Boolean getValue() {
		return value;
	}

}
