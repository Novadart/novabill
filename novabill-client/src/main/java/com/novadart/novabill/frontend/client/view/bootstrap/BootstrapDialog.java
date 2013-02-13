package com.novadart.novabill.frontend.client.view.bootstrap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.gwtshared.client.dialog.Dialog;
import com.novadart.gwtshared.client.validation.widget.ValidatedListBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.util.CountryUtils;
import com.novadart.novabill.frontend.client.view.util.LocaleWidgets;
import com.novadart.novabill.frontend.client.widget.notification.InlineNotification;
import com.novadart.novabill.frontend.client.widget.validation.AlternativeSsnVatIdValidation;
import com.novadart.novabill.frontend.client.widget.validation.ValidationKit;
import com.novadart.novabill.shared.client.dto.BusinessDTO;

public class BootstrapDialog extends Dialog {

	private static BootstrapDialogUiBinder uiBinder = GWT
			.create(BootstrapDialogUiBinder.class);


	interface BootstrapDialogUiBinder extends UiBinder<Widget, BootstrapDialog> {
	}

	public interface Handler {
		public void businessData(BusinessDTO business);
	}

	@UiField(provided=true) ValidatedTextBox name;
	@UiField(provided=true) ValidatedTextBox ssn;
	@UiField(provided=true) ValidatedTextBox vatID;
	@UiField(provided=true) ValidatedTextBox address;
	@UiField(provided=true) ValidatedTextBox city;
	@UiField(provided=true) ValidatedListBox province;
	@UiField(provided=true) ValidatedListBox country;
	@UiField(provided=true) ValidatedTextBox postcode;
	@UiField(provided=true) ValidatedTextBox phone;
	@UiField(provided=true) ValidatedTextBox email;
	@UiField(provided=true) ValidatedTextBox mobile;
	@UiField(provided=true) ValidatedTextBox fax;
	@UiField(provided=true) ValidatedTextBox web;

	@UiField InlineNotification inlineNotification;
	
	private AlternativeSsnVatIdValidation ssnOrVatIdValidation = new AlternativeSsnVatIdValidation(); 
	private Handler handler;

	public BootstrapDialog() {
		super(GlobalBundle.INSTANCE.dialog(), false);
		name = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NOT_EMPTY);
		
		ssn = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.SSN_OR_VAT_ID);
		ssn.setShowMessageOnError(true);
		vatID = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.OPTIONAL_VAT_ID);
		vatID.setShowMessageOnError(true);
		ssnOrVatIdValidation.addWidget(ssn);
		ssnOrVatIdValidation.addWidget(vatID);
		
		address = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NOT_EMPTY);
		city = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NOT_EMPTY);
		province = LocaleWidgets.createProvinceListBox(I18N.INSTANCE.province()); 
		country = LocaleWidgets.createCountryListBoxItalyOnly(I18N.INSTANCE.country());
		country.setSelectedItem(CountryUtils.getRegionName("IT"));
		postcode = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.POSTCODE);
		phone = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);
		email = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.OPTIONAL_EMAIL);
		mobile = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);
		fax = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);
		web = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);
		setWidget(uiBinder.createAndBindUi(this));
	}

	private boolean validate(){
		boolean valid = true;
		inlineNotification.hide();

		ssnOrVatIdValidation.validate();
		if(!ssnOrVatIdValidation.isValid()){
			inlineNotification.showMessage(ssnOrVatIdValidation.getErrorMessage());
			valid = false;
		}

		for (ValidatedTextBox r : new ValidatedTextBox[]{name, address, city, 
				postcode, phone, email, mobile, fax, web}) {
			r.validate();
			valid &= r.isValid();
		}

		country.validate();
		valid &= country.isValid();

		province.validate();
		valid &= province.isValid();

		return valid;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	@UiHandler("confirm")
	void onSubmitClicked(ClickEvent e){
		if(validate()){

			final BusinessDTO b = Configuration.getBusiness();
			b.setName(name.getText());
			b.setAddress(address.getText());
			b.setSsn(ssn.getText());
			b.setVatID(vatID.getText());
			b.setCity(city.getText());
			b.setProvince(province.getSelectedItemText());
			b.setCountry(country.getSelectedItemValue());
			b.setPostcode(postcode.getText());
			b.setPhone(phone.getText());
			b.setEmail(email.getText());
			b.setMobile(mobile.getText());
			b.setFax(fax.getText());
			b.setWeb(web.getText());

			handler.businessData(b);

		}
	}
}
