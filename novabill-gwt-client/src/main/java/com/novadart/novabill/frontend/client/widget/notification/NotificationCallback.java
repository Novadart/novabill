package com.novadart.novabill.frontend.client.widget.notification;

public interface NotificationCallback<T> {
	public void onNotificationClosed(T value);
}