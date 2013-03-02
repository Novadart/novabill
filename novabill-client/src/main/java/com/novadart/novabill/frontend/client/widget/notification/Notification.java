package com.novadart.novabill.frontend.client.widget.notification;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.novadart.novabill.frontend.client.widget.notification.impl.ConfirmDialog;
import com.novadart.novabill.frontend.client.widget.notification.impl.MessageDialog;


public class Notification {
	
	public static void showMessage(String message){
		showMessage(message, new NotificationCallback<Void>() {
			@Override
			public void onNotificationClosed(Void value) {}
		});
	}
	
	public static void showMessage(SafeHtml message){
		showMessage(message, new NotificationCallback<Void>() {
			@Override
			public void onNotificationClosed(Void value) {}
		});
	}
	
	public static void showMessage(String message, NotificationCallback<Void> onClose){
		MessageDialog md = new MessageDialog(onClose);
		md.setMessage(message);
		md.showCentered();
	}
	
	public static void showMessage(SafeHtml message, NotificationCallback<Void> onClose){
		MessageDialog md = new MessageDialog(onClose);
		md.setMessage(message);
		md.showCentered();
	}

	public static void showConfirm(String message, NotificationCallback<Boolean> onClose){
		ConfirmDialog cd = new ConfirmDialog(onClose);
		cd.setMessage(message);
		cd.showCentered();
	}
	
	public static void showConfirm(SafeHtml message, NotificationCallback<Boolean> onClose){
		ConfirmDialog cd = new ConfirmDialog(onClose);
		cd.setMessage(message);
		cd.showCentered();
	}
	
}
