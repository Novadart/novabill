package com.novadart.novabill.frontend.client.widget.notification;



public class Notification {
	
	public static void showMessage(String message){
		showMessage(message, new NotificationCallback() {
			@Override
			public void onNotificationClosed(boolean value) {}
		});
	}
	
	public static native void showMessage(String message, NotificationCallback callback)/*-{
		$wnd.Angular_Dialogs.alert(message, function(){
			callback.@com.novadart.novabill.frontend.client.widget.notification.NotificationCallback::onNotificationClosed(Z)(true);
		});
	}-*/;
	
	public static native void showConfirm(String message, NotificationCallback callback)/*-{
		$wnd.Angular_Dialogs.confirm(message, function(value){
			callback.@com.novadart.novabill.frontend.client.widget.notification.NotificationCallback::onNotificationClosed(Z)(value);
		});
	}-*/;
	
	
}
