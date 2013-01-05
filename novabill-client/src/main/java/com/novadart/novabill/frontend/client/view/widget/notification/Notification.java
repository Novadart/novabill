package com.novadart.novabill.frontend.client.view.widget.notification;

import com.google.gwt.user.client.Window;

public class Notification {
	
	public static void showMessage(String message){
		Window.alert(message);
	}

	public static boolean showYesNoRequest(String message){
		return Window.confirm(message);
	}
	
}
