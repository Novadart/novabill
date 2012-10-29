package com.novadart.novabill.frontend.client.ui.center.client.dialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.gwtshared.client.validation.DefaultValidation;
import com.novadart.gwtshared.client.validation.widget.ValidatedListBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.datawatcher.DataWatcher;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.ui.util.LocaleWidgets;
import com.novadart.novabill.frontend.client.ui.widget.dialog.Dialog;
import com.novadart.novabill.frontend.client.ui.widget.notification.InlineNotification;
import com.novadart.novabill.frontend.client.ui.widget.notification.Notification;
import com.novadart.novabill.frontend.client.ui.widget.validation.EmailValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.NotEmptyValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.NumberValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.PostcodeValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.SsnOrVatIdValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.VatIdValidation;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.ContactDTO;

public class ClientDialog extends Dialog {

	private static ClientDialogUiBinder uiBinder = GWT
			.create(ClientDialogUiBinder.class);

	interface ClientDialogUiBinder extends UiBinder<Widget, ClientDialog> {
	}

	private static final ClientDialog instance = new ClientDialog();

	public static ClientDialog getInstance(){
		instance.clearData();
		return instance;
	}
	
	@UiField InlineNotification inlineNotification;

	@UiField(provided=true) ValidatedTextBox companyName;
	@UiField(provided=true) ValidatedTextBox address;
	@UiField(provided=true) ValidatedTextBox city;
	@UiField(provided=true) ValidatedListBox province;
	@UiField(provided=true) ValidatedListBox country;
	@UiField(provided=true) ValidatedTextBox postcode;
	@UiField(provided=true) ValidatedTextBox phone;
	@UiField(provided=true) ValidatedTextBox mobile;
	@UiField(provided=true) ValidatedTextBox fax;
	@UiField(provided=true) ValidatedTextBox email;
	@UiField(provided=true) ValidatedTextBox web;
	@UiField(provided=true) ValidatedTextBox vatID;
	@UiField(provided=true) ValidatedTextBox ssn;

	@UiField(provided=true) ValidatedTextBox contactMobile;
	@UiField(provided=true) ValidatedTextBox contactFax;
	@UiField(provided=true) ValidatedTextBox contactEmail;
	@UiField(provided=true) ValidatedTextBox contactPhone;
	@UiField(provided=true) ValidatedTextBox contactName;
	@UiField(provided=true) ValidatedTextBox contactSurname;
	
	@UiField Button ok;
	
	private ClientDTO client = null;

	private ClientDialog() {
		NotEmptyValidation ne = new NotEmptyValidation();
		companyName = new ValidatedTextBox(ne);
		vatID =  new ValidatedTextBox(new VatIdValidation(true));
		ssn =  new ValidatedTextBox(new SsnOrVatIdValidation(true));
		postcode = new ValidatedTextBox(new PostcodeValidation());
		address = new ValidatedTextBox(ne);
		city = new ValidatedTextBox(ne);
		country = LocaleWidgets.createCountryListBox("");
		
		NumberValidation nv = new NumberValidation(true);
		phone = new ValidatedTextBox(nv);
		mobile = new ValidatedTextBox(nv);
		fax = new ValidatedTextBox(nv);
		contactPhone = new ValidatedTextBox(nv);
		contactMobile = new ValidatedTextBox(nv);
		contactFax = new ValidatedTextBox(nv);
		
		DefaultValidation<String> dv = new DefaultValidation<String>();
		web = new ValidatedTextBox(dv);
		contactName = new ValidatedTextBox(dv);
		contactSurname = new ValidatedTextBox(dv);

		email = new ValidatedTextBox(new EmailValidation(true));
		contactEmail = new ValidatedTextBox(new EmailValidation(true));
		
		province = LocaleWidgets.createProvinceListBox("");
		setWidget(uiBinder.createAndBindUi(this));
		addStyleName("ClientDialog panel");
	}

	@UiFactory
	I18N getI18N(){
		return I18N.INSTANCE;
	}

	public void setClient(ClientDTO client) {
		this.client = client;

		companyName.setText(client.getName());
		address.setText(client.getAddress());
		city.setText(client.getCity());
		province.setSelectedItem(client.getProvince());
		country.setSelectedItemByValue(client.getCountry());
		postcode.setText(client.getPostcode());
		phone.setText(client.getPhone());
		mobile.setText(client.getMobile());
		fax.setText(client.getFax());
		email.setText(client.getEmail());
		web.setText(client.getWeb());
		vatID.setText(client.getVatID());
		ssn.setText(client.getSsn());
		
		ContactDTO ct = client.getContact();
		contactEmail.setText(ct.getEmail());
		contactFax.setText(ct.getFax());
		contactMobile.setText(ct.getMobile());
		contactName.setText(ct.getFirstName());
		contactPhone.setText(ct.getPhone());
		contactSurname.setText(ct.getLastName());
		
		ok.setText(I18N.INSTANCE.saveModifications());
	}


	@UiHandler("ok")
	void onOkClicked(ClickEvent e){
		if(!validate()){
			return;
		}

		ContactDTO contact;
		ClientDTO client;

		if(this.client == null){
			client = new ClientDTO();
			contact = new ContactDTO();
			client.setContact(contact);
		} else {
			client = this.client;
			contact = this.client.getContact();
		}
		
		client.setAddress(address.getText());
		client.setCity(city.getText());
		client.setCountry(country.getSelectedItemValue());
		client.setEmail(email.getText());
		client.setFax(fax.getText());
		client.setMobile(mobile.getText());
		client.setName(companyName.getText());
		client.setPhone(phone.getText());
		client.setPostcode(postcode.getText());
		if(country.getSelectedItemValue().equalsIgnoreCase("IT")){
			client.setProvince(province.getItemText(province.getSelectedIndex()));
		} else {
			client.setProvince("");
		}
		
		client.setSsn(ssn.getText());
		client.setVatID(vatID.getText());
		client.setWeb(web.getText());

		contact.setEmail(contactEmail.getText());
		contact.setFax(contactFax.getText());
		contact.setFirstName(contactName.getText());
		contact.setLastName(contactSurname.getText());
		contact.setMobile(contactMobile.getText());
		contact.setPhone(contactPhone.getText());
		
		if(this.client == null) {
			ServerFacade.client.add(client, new WrappedAsyncCallback<Long>() {

				@Override
				public void onSuccess(Long result) {
					DataWatcher.getInstance().fireClientEvent();
					DataWatcher.getInstance().fireStatsEvent();
					hide();
				}

				@Override
				public void onException(Throwable caught) {
					Notification.showMessage(I18N.INSTANCE.errorServerCommunication());
				}
			});
		} else {
			
			ServerFacade.client.update(client, new WrappedAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					DataWatcher.getInstance().fireClientEvent();
					hide();
				}

				@Override
				public void onException(Throwable caught) {
					Notification.showMessage(I18N.INSTANCE.errorServerCommunication());
				}
			});
			
		}
	}

	@UiHandler("cancel")
	void onCancelClicked(ClickEvent e){
		hide();
	}
	
	@UiHandler("country")
	void onCountryChange(ChangeEvent event){
		province.setEnabled(country.getSelectedItemValue().equalsIgnoreCase("IT"));
		province.reset();
	}

	private void clearData(){
		client = null;
		province.reset();
		web.setText("");
		for (ValidatedTextBox tb: new ValidatedTextBox[]{vatID, companyName, 
				ssn, postcode, phone, mobile, fax, email, address, city, web,
				contactEmail, contactFax, contactMobile, contactName, contactPhone,
				contactSurname}){
			tb.reset();
		}
		province.setEnabled(true);
		province.reset();
		country.reset();
		country.setSelectedItemByValue("IT");
		ok.setText(I18N.INSTANCE.submit());
		inlineNotification.hide();
	}

	private boolean validate(){
		boolean isValid = true;
		inlineNotification.hide();
		
		if(vatID.getText().isEmpty() && ssn.getText().isEmpty()){
			inlineNotification.showMessage(I18N.INSTANCE.fillVatIdOrSsn());
			ssn.setValidationErrorStyle();
			vatID.setValidationErrorStyle();
			isValid = false;
		}
		
		for (ValidatedTextBox tb: new ValidatedTextBox[]{vatID, companyName, 
				ssn, postcode, phone, mobile, fax, email, address, city, web,
				contactEmail, contactFax, contactMobile, contactName, contactPhone,
				contactSurname}){
			tb.validate();
			isValid = isValid && tb.isValid();
		}
		
		country.validate();
		isValid = isValid && country.isValid();
		if(country.getSelectedItemValue().equalsIgnoreCase("IT")){
			province.validate();
			isValid = isValid && province.isValid();
		}
		return isValid;
	}


}
