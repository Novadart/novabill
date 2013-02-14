package com.novadart.novabill.frontend.client.widget.dialog.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.gwtshared.client.LoaderButton;
import com.novadart.gwtshared.client.dialog.Dialog;
import com.novadart.gwtshared.client.validation.widget.ValidatedListBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.event.ClientAddEvent;
import com.novadart.novabill.frontend.client.event.ClientUpdateEvent;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.resources.GlobalCss;
import com.novadart.novabill.frontend.client.resources.ImageResources;
import com.novadart.novabill.frontend.client.view.HasUILocking;
import com.novadart.novabill.frontend.client.view.util.LocaleWidgets;
import com.novadart.novabill.frontend.client.widget.notification.InlineNotification;
import com.novadart.novabill.frontend.client.widget.validation.AlternativeSsnVatIdValidation;
import com.novadart.novabill.frontend.client.widget.validation.ValidationKit;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.ContactDTO;

public class ClientDialog extends Dialog implements HasUILocking {

	private static ClientDialogUiBinder uiBinder = GWT
			.create(ClientDialogUiBinder.class);

	interface ClientDialogUiBinder extends UiBinder<Widget, ClientDialog> {
	}

	private static final ClientDialog instance = new ClientDialog();
	private static EventBus eventBus;

	public static ClientDialog getInstance(EventBus eventBus){
		ClientDialog.eventBus = eventBus;
		instance.clearData();
		return instance;
	}
	
	@UiField InlineNotification inlineNotification;
	
	@UiField Label clientDialogTitle;

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
	
	@UiField(provided=true) LoaderButton ok;
	@UiField Button cancel;
	
	private AlternativeSsnVatIdValidation ssnOrVatIdValidation = new AlternativeSsnVatIdValidation(); 
	
	private ClientDTO client = null;

	private ClientDialog() {
		super(GlobalBundle.INSTANCE.dialog());
		companyName = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NOT_EMPTY);
		
		vatID =  new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.VAT_ID);
		vatID.setShowMessageOnError(true);
		ssn =  new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.SSN_OR_VAT_ID);
		ssn.setShowMessageOnError(true);
		ssnOrVatIdValidation.addWidget(vatID);
		ssnOrVatIdValidation.addWidget(ssn);
		
		postcode = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NUMBER);
		
		address = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NOT_EMPTY);
		city = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NOT_EMPTY);
		country = LocaleWidgets.createCountryListBox("");
		
		phone = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);
		mobile = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);
		fax = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);
		contactPhone = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);
		contactMobile = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);
		contactFax = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);
		
		web = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);
		contactName = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);
		contactSurname = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);

		email = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.OPTIONAL_EMAIL);
		contactEmail = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.OPTIONAL_EMAIL);
		
		province = LocaleWidgets.createProvinceListBox("");
		
		ok = new LoaderButton(ImageResources.INSTANCE.loader(), GlobalBundle.INSTANCE.loaderButton());
		
		setWidget(uiBinder.createAndBindUi(this));
		addStyleName(GlobalBundle.INSTANCE.globalCss().panel());
		
		ok.addStyleName("submit");
		ok.getButton().addStyleName(GlobalBundle.INSTANCE.globalCss().button());
	}

	@UiFactory
	I18N getI18N(){
		return I18N.INSTANCE;
	}
	
	@UiFactory
	GlobalCss getGlobalCss(){
		return GlobalBundle.INSTANCE.globalCss();
	}

	public void setClient(ClientDTO client) {
		this.client = client;
		clientDialogTitle.setText(I18N.INSTANCE.modifyClientTitle());
		
		boolean isIT = switchValidationByCountry(client.getCountry());

		companyName.setText(client.getName());
		address.setText(client.getAddress());
		city.setText(client.getCity());
		if(isIT){
			province.setSelectedItem(client.getProvince());
		}
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
		final ClientDTO client;

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
		
		ok.showLoader(true);
		setLocked(true);
		if(this.client == null) {
			ServerFacade.client.add(Configuration.getBusinessId(), client, new ManagedAsyncCallback<Long>() {

				@Override
				public void onSuccess(Long result) {
					ok.showLoader(false);
					eventBus.fireEvent(new ClientAddEvent(client));
					hide();
					setLocked(false);
				}

				@Override
				public void onFailure(Throwable caught) {
					ok.showLoader(false);
					super.onFailure(caught);
					setLocked(false);
				}
			});
		} else {
			
			ServerFacade.client.update(Configuration.getBusinessId(), client, new ManagedAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					ok.showLoader(false);
					eventBus.fireEvent(new ClientUpdateEvent(client));
					hide();
					setLocked(false);
				}

				@Override
				public void onFailure(Throwable caught) {
					ok.showLoader(false);
					super.onFailure(caught);
					setLocked(false);
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
		switchValidationByCountry(country.getSelectedItemValue());
	}
	
	private boolean switchValidationByCountry(String country){
		boolean isIT = "IT".equalsIgnoreCase(country);
		province.setEnabled(isIT);
		province.reset();
		setVatIdSsnValidation(isIT);
		vatID.reset();
		ssn.reset();
		postcode.setValidationBundle(isIT ? ValidationKit.NUMBER : ValidationKit.NOT_EMPTY);
		postcode.reset();
		return isIT;
	}
	
	private void setVatIdSsnValidation(boolean activate){
		if(activate){
			ssn.setValidationBundle(ValidationKit.SSN_OR_VAT_ID);
			vatID.setValidationBundle(ValidationKit.VAT_ID);
		} else {
			ssn.setValidationBundle(ValidationKit.NOT_EMPTY);
			vatID.setValidationBundle(ValidationKit.NOT_EMPTY);
		}
	}

	private void clearData(){
		client = null;
		setVatIdSsnValidation(true);
		postcode.setValidationBundle(ValidationKit.NUMBER);
		province.reset();
		web.setText("");
		for (ValidatedTextBox tb: new ValidatedTextBox[]{companyName, 
				postcode, phone, mobile, fax, email, address, city, web,
				contactEmail, contactFax, contactMobile, contactName, contactPhone,
				contactSurname}){
			tb.reset();
		}
		ssnOrVatIdValidation.reset();
		
		province.setEnabled(true);
		province.reset();
		country.reset();
		country.setSelectedItemByValue("IT");
		ok.setText(I18N.INSTANCE.submit());
		inlineNotification.hide();
		clientDialogTitle.setText(I18N.INSTANCE.addNewClientTitle());
		ok.reset();
		
		setLocked(false);
	}

	private boolean validate(){
		boolean isValid = true;
		inlineNotification.hide();
		
		ssnOrVatIdValidation.validate();
		if(!ssnOrVatIdValidation.isValid()){
			inlineNotification.showMessage(ssnOrVatIdValidation.getErrorMessage());
			isValid = false;
		}
		
		for (ValidatedTextBox tb: new ValidatedTextBox[]{companyName, 
				postcode, phone, mobile, fax, email, address, city, web,
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

	@Override
	public void setLocked(boolean value) {
		companyName.setEnabled(!value);
		address.setEnabled(!value);
		city.setEnabled(!value);
		province.setEnabled(!value);
		country.setEnabled(!value);
		postcode.setEnabled(!value);
		phone.setEnabled(!value);
		mobile.setEnabled(!value);
		fax.setEnabled(!value);
		email.setEnabled(!value);
		web.setEnabled(!value);
		vatID.setEnabled(!value);
		ssn.setEnabled(!value);
		contactMobile.setEnabled(!value);
		contactFax.setEnabled(!value);
		contactEmail.setEnabled(!value);
		contactPhone.setEnabled(!value);
		contactName.setEnabled(!value);
		contactSurname.setEnabled(!value);
		cancel.setEnabled(!value);
	}

}
