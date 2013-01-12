package com.novadart.novabill.frontend.client.view.widget.notification;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.gwtshared.client.dialog.Dialog;
import com.novadart.novabill.frontend.client.i18n.I18N;

public abstract class NotificationDialog<T> extends Dialog {
	
	private static NotificationDialogUiBinder uiBinder = GWT
			.create(NotificationDialogUiBinder.class);

	interface NotificationDialogUiBinder extends
			UiBinder<Widget, NotificationDialog<?>> {
	}

	@UiField Label message;
	@UiField FlowPanel buttons;
	
	private final NotificationCallback<T> onClose;
	
	public NotificationDialog(NotificationCallback<T> onClose) {
		setAutoHideEnabled(false);
		setWidget(uiBinder.createAndBindUi(this));
		addButtons(buttons);
		addStyleName("NotificationDialog");
		this.onClose = onClose;
	}

	@UiFactory
	I18N getI18N(){
		return I18N.INSTANCE;
	}

	protected abstract void addButtons(FlowPanel buttons);
	
	protected abstract T getValue();
	
	@Override
	protected void onUnload() {
		super.onUnload();
		this.onClose.onNotificationClosed(getValue());
	}
	
	public void setMessage(String text) {
		message.setText(text);
	}
	
}