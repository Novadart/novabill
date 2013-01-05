package com.novadart.novabill.frontend.client.view.bootstrap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.gwtshared.client.dialog.Dialog;
import com.novadart.gwtshared.client.textbox.RichTextBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedListBox;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.util.CountryUtils;
import com.novadart.novabill.frontend.client.view.util.LocaleWidgets;
import com.novadart.novabill.frontend.client.view.widget.notification.InlineNotification;
import com.novadart.novabill.frontend.client.view.widget.validation.AlternativeSsnVatIdValidation;
import com.novadart.novabill.frontend.client.view.widget.validation.ValidationKit;
import com.novadart.novabill.shared.client.dto.BusinessDTO;

public class BootstrapDialog extends Dialog {

	private static BootstrapDialogUiBinder uiBinder = GWT
			.create(BootstrapDialogUiBinder.class);


	interface BootstrapDialogUiBinder extends UiBinder<Widget, BootstrapDialog> {
	}

	public interface Handler {
		public void businessData(BusinessDTO business);
	}

	@UiField(provided=true) RichTextBox name;
	@UiField(provided=true) RichTextBox ssn;
	@UiField(provided=true) RichTextBox vatID;
	@UiField(provided=true) RichTextBox address;
	@UiField(provided=true) RichTextBox city;
	@UiField(provided=true) ValidatedListBox province;
	@UiField(provided=true) ValidatedListBox country;
	@UiField(provided=true) RichTextBox postcode;
	@UiField(provided=true) RichTextBox phone;
	@UiField(provided=true) RichTextBox email;
	@UiField(provided=true) RichTextBox mobile;
	@UiField(provided=true) RichTextBox fax;
	@UiField(provided=true) RichTextBox web;

	@UiField InlineNotification inlineNotification;
	
	private AlternativeSsnVatIdValidation ssnOrVatIdValidation = new AlternativeSsnVatIdValidation(); 
	private Handler handler;

	public BootstrapDialog() {
		super(false);
		name = new RichTextBox(I18N.INSTANCE.companyName(), ValidationKit.NOT_EMPTY);
		
		ssn = new RichTextBox(I18N.INSTANCE.ssn(), ValidationKit.SSN_OR_VAT_ID);
		ssn.setShowMessageOnError(false);
		vatID = new RichTextBox(I18N.INSTANCE.vatID(), ValidationKit.OPTIONAL_VAT_ID);
		vatID.setShowMessageOnError(false);
		ssnOrVatIdValidation.addWidget(ssn);
		ssnOrVatIdValidation.addWidget(vatID);
		
		address = new RichTextBox(I18N.INSTANCE.address(), ValidationKit.NOT_EMPTY);
		city = new RichTextBox(I18N.INSTANCE.city(), ValidationKit.NOT_EMPTY);
		province = LocaleWidgets.createProvinceListBox(I18N.INSTANCE.province()); 
		country = LocaleWidgets.createCountryListBoxItalyOnly(I18N.INSTANCE.country());
		country.setSelectedItem(CountryUtils.getRegionName("IT"));
		postcode = new RichTextBox(I18N.INSTANCE.postcode(), ValidationKit.POSTCODE);
		phone = new RichTextBox(I18N.INSTANCE.phone(), ValidationKit.OPTIONAL_NUMBER);
		email = new RichTextBox(I18N.INSTANCE.companyEmail(), ValidationKit.OPTIONAL_EMAIL);
		mobile = new RichTextBox(I18N.INSTANCE.mobile(), ValidationKit.OPTIONAL_NUMBER);
		fax = new RichTextBox(I18N.INSTANCE.fax(), ValidationKit.OPTIONAL_NUMBER);
		web = new RichTextBox(I18N.INSTANCE.web(), ValidationKit.DEFAULT);
		setWidget(uiBinder.createAndBindUi(this));
		addStyleName("BootstrapDialog");
	}

	private boolean validate(){
		boolean valid = true;
		inlineNotification.hide();

		ssnOrVatIdValidation.validate();
		if(!ssnOrVatIdValidation.isValid()){
			inlineNotification.showMessage(ssnOrVatIdValidation.getErrorMessage());
			valid = false;
		}

		for (RichTextBox r : new RichTextBox[]{name, address, city, 
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
