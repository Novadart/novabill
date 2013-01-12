package com.novadart.novabill.frontend.client.view.widget.notification;

public interface NotificationCallback<T> {
	public void onNotificationClosed(T value);
}