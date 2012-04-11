package com.novadart.novabill.frontend.client.ui.bootstrap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.gwtshared.client.textbox.RichTextBox;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.ui.widget.dialog.Dialog;
import com.novadart.novabill.frontend.client.ui.widget.validation.EmailValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.NotEmptyValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.NumberValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.PostcodeValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.SsnValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.VatIdValidation;
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
	@UiField(provided=true) RichTextBox province;
	@UiField(provided=true) RichTextBox country;
	@UiField(provided=true) RichTextBox postcode;
	@UiField(provided=true) RichTextBox phone;
	@UiField(provided=true) RichTextBox email;
	@UiField(provided=true) RichTextBox mobile;
	@UiField(provided=true) RichTextBox fax;
	@UiField RichTextBox web;
	
	
	private Handler handler;
	
	public BootstrapDialog() {
		NotEmptyValidation nev = new NotEmptyValidation();
		NumberValidation nuv = new NumberValidation(true);
		name = new RichTextBox(I18N.get.companyName(), nev);
		ssn = new RichTextBox(I18N.get.ssn(), new SsnValidation());
		vatID = new RichTextBox(I18N.get.vatID(), new VatIdValidation());
		address = new RichTextBox(I18N.get.address(), nev);
		city = new RichTextBox(I18N.get.city(), nev);
		province = new RichTextBox(I18N.get.province(), nev);
		country = new RichTextBox(I18N.get.country(), nev);
		postcode = new RichTextBox(I18N.get.postcode(), new PostcodeValidation());
		phone = new RichTextBox(I18N.get.phone(), nuv);
		email = new RichTextBox(I18N.get.email(), new EmailValidation(true));
		mobile = new RichTextBox(I18N.get.mobile(), nuv);
		fax = new RichTextBox(I18N.get.fax(), nuv);
		setWidget(uiBinder.createAndBindUi(this));
		addStyleName("BootstrapDialog");
	}
	
	private boolean validate(){
		boolean valid = true;
		for (RichTextBox r : new RichTextBox[]{name, ssn, vatID, address, city, province, country, postcode}) {
			r.validate();
			valid &= r.isValid();
		}
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
			b.setProvince(province.getText());
			b.setCountry(country.getText());
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
