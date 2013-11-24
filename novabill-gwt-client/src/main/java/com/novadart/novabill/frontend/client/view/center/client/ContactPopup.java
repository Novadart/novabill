package com.novadart.novabill.frontend.client.view.center.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.shared.client.dto.ContactDTO;

public class ContactPopup extends PopupPanel {

	private static ContactPopupUiBinder uiBinder = GWT
			.create(ContactPopupUiBinder.class);

	interface ContactPopupUiBinder extends UiBinder<Widget, ContactPopup> {
	}
	
	@UiField Label name;
	@UiField Label surname;
	@UiField Label email;
	@UiField Label mobile;
	@UiField Label phone;
	@UiField Label fax;

	ContactPopup() {
		setWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiFactory
	I18N getI18n(){
		return I18N.INSTANCE;
	}
	
	public void setContact(ContactDTO contact) {
		name.setText(contact.getFirstName());
		surname.setText(contact.getLastName());
		email.setText(contact.getEmail());
		mobile.setText(contact.getMobile());
		phone.setText(contact.getPhone());
		fax.setText(contact.getFax());
	}

	void reset(){
		name.setText("");
		surname.setText("");
		email.setText("");
		mobile.setText("");
		phone.setText("");
		fax.setText("");
	}
	
	public boolean isEmpty(){
		return (name.getText().isEmpty() && surname.getText().isEmpty() && email.getText().isEmpty()
				&& mobile.getText().isEmpty() && phone.getText().isEmpty() && fax.getText().isEmpty());
	}
	
}