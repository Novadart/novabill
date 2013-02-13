package com.novadart.novabill.frontend.client.widget.notification;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class InlineNotification extends Composite {

	private static InlineNotificationUiBinder uiBinder = GWT
			.create(InlineNotificationUiBinder.class);

	@UiField Label label;
	
	interface InlineNotificationUiBinder extends
			UiBinder<Widget, InlineNotification> {
	}

	public InlineNotification() {
		initWidget(uiBinder.createAndBindUi(this));
		setVisible(false);
	}
	
	public void showMessage(String message){
		label.setText(message);
		setVisible(true);
	}
	
	public void hide(){
		setVisible(false);
	}

}
