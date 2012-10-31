package com.novadart.novabill.frontend.client.facade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.gwtshared.client.dialog.Dialog;

public class AuthDialog extends Dialog {

	private static AuthDialogUiBinder uiBinder = GWT
			.create(AuthDialogUiBinder.class);

	interface AuthDialogUiBinder extends UiBinder<Widget, AuthDialog> {
	}

	@UiField TextBox username;
	@UiField PasswordTextBox password;
	@UiField Button submit;
	private final AsyncCallback<Boolean> callback;
	
	public AuthDialog(AsyncCallback<Boolean> callback) {
		this.callback = callback;
		setWidget(uiBinder.createAndBindUi(this));
		addStyleName("AuthDialog");
	}

	@UiHandler("submit")
	void onSubmitClicked(ClickEvent e){
		username.setEnabled(false);
		password.setEnabled(false);
		submit.setEnabled(false);
		
		String user = username.getText();
		String pwd = password.getText();
		
		if(!user.isEmpty() && !pwd.isEmpty()){
			ServerFacade.authenticate(user, pwd, callback);
		}
	}
	
}
