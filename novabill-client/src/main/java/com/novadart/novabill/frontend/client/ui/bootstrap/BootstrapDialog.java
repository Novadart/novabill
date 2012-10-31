package com.novadart.novabill.frontend.client.ui.bootstrap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.gwtshared.client.dialog.Dialog;
import com.novadart.gwtshared.client.textbox.RichTextBox;
import com.novadart.gwtshared.client.validation.DefaultValidation;
import com.novadart.gwtshared.client.validation.widget.ValidatedListBox;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.ui.util.LocaleWidgets;
import com.novadart.novabill.frontend.client.ui.widget.notification.InlineNotification;
import com.novadart.novabill.frontend.client.ui.widget.validation.EmailValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.NotEmptyValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.NumberValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.PostcodeValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.SsnOrVatIdValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.VatIdValidation;
import com.novadart.novabill.frontend.client.util.CountryUtils;
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
	
	private Handler handler;
	
	public BootstrapDialog() {
		super(false);
		NotEmptyValidation nev = new NotEmptyValidation();
		NumberValidation nuv = new NumberValidation(true);
		name = new RichTextBox(I18N.INSTANCE.companyName(), nev);
		ssn = new RichTextBox(I18N.INSTANCE.ssn(), new SsnOrVatIdValidation(true));
		vatID = new RichTextBox(I18N.INSTANCE.vatID(), new VatIdValidation(true));
		address = new RichTextBox(I18N.INSTANCE.address(), nev);
		city = new RichTextBox(I18N.INSTANCE.city(), nev);
		province = LocaleWidgets.createProvinceListBox(I18N.INSTANCE.province()); 
		country = LocaleWidgets.createCountryListBox(I18N.INSTANCE.country());
		country.setSelectedItem(CountryUtils.getRegionName("IT"));
		postcode = new RichTextBox(I18N.INSTANCE.postcode(), new PostcodeValidation());
		phone = new RichTextBox(I18N.INSTANCE.phone(), nuv);
		email = new RichTextBox(I18N.INSTANCE.companyEmail(), new EmailValidation(true));
		mobile = new RichTextBox(I18N.INSTANCE.mobile(), nuv);
		fax = new RichTextBox(I18N.INSTANCE.fax(), nuv);
		web = new RichTextBox(I18N.INSTANCE.web(), new DefaultValidation<String>());
		setWidget(uiBinder.createAndBindUi(this));
		addStyleName("BootstrapDialog");
	}
	
	private boolean validate(){
		boolean valid = true;
		inlineNotification.hide();
		
		if(vatID.getText().isEmpty() && ssn.getText().isEmpty()){
			inlineNotification.showMessage(I18N.INSTANCE.fillVatIdOrSsn());
			ssn.setValidationErrorStyle();
			vatID.setValidationErrorStyle();
			valid = false;
		}
		
		for (RichTextBox r : new RichTextBox[]{name, ssn, vatID, address, city, 
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
			b.setCountry(country.getSelectedItemText());
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
