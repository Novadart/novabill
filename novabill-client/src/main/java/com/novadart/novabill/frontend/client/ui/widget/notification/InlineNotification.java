package com.novadart.novabill.frontend.client.ui.widget.notification;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

public class InlineNotification extends Composite {

	private Label label = new Label();
	
	public InlineNotification() {
		initWidget(label);
		setStyleName("InlineNotification");
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
