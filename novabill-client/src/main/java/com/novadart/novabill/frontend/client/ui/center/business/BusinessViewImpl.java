package com.novadart.novabill.frontend.client.ui.center.business;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.Const;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.ui.center.BusinessView;
import com.novadart.novabill.frontend.client.ui.widget.validation.EmailValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.NotEmptyValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.NumberValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.PostcodeValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.SsnOrVatIdValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.VatIdValidation;
import com.novadart.novabill.shared.client.dto.BusinessDTO;

public class BusinessViewImpl extends Composite implements BusinessView {

	private static BusinessViewImplUiBinder uiBinder = GWT
			.create(BusinessViewImplUiBinder.class);

	interface BusinessViewImplUiBinder extends
	UiBinder<Widget, BusinessViewImpl> {
	}

	private Presenter presenter;

	@UiField FormPanel formPanel;
	@UiField Image logo;

	@UiField(provided=true) ValidatedTextBox name;
	@UiField(provided=true) ValidatedTextBox ssn;
	@UiField(provided=true) ValidatedTextBox vatID;
	@UiField(provided=true) ValidatedTextBox address;
	@UiField(provided=true) ValidatedTextBox city;
	@UiField(provided=true) ValidatedTextBox province;
	@UiField(provided=true) ValidatedTextBox country;
	@UiField(provided=true) ValidatedTextBox postcode;
	@UiField(provided=true) ValidatedTextBox phone;
	@UiField(provided=true) ValidatedTextBox email;
	@UiField(provided=true) ValidatedTextBox mobile;
	@UiField(provided=true) ValidatedTextBox fax;
	@UiField ValidatedTextBox web;

	public BusinessViewImpl() {
		BusinessDTO b = Configuration.getBusiness();

		NotEmptyValidation nev = new NotEmptyValidation();
		NumberValidation nuv = new NumberValidation(true);

		name = new ValidatedTextBox(nev);
		name.setText(b.getName());
		ssn = new ValidatedTextBox(new SsnOrVatIdValidation());
		ssn.setText(b.getSsn());
		vatID = new ValidatedTextBox(new VatIdValidation());
		vatID.setText(b.getVatID());
		address = new ValidatedTextBox(nev);
		address.setText(b.getAddress());
		city = new ValidatedTextBox(nev);
		city.setText(b.getCity());
		province = new ValidatedTextBox(nev);
		province.setText(b.getProvince());
		country = new ValidatedTextBox(nev);
		country.setText(b.getCountry());
		postcode = new ValidatedTextBox(new PostcodeValidation());
		postcode.setText(b.getPostcode());
		phone = new ValidatedTextBox(nuv);
		phone.setText(b.getPostcode());
		email = new ValidatedTextBox(new EmailValidation(true));
		email.setText(b.getEmail());
		mobile = new ValidatedTextBox(nuv);
		mobile.setText(b.getMobile());
		fax = new ValidatedTextBox(nuv);
		fax.setText(b.getFax());

		initWidget(uiBinder.createAndBindUi(this));

		formPanel.setAction(Const.URL_LOGO);
		formPanel.setMethod(FormPanel.METHOD_POST);
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);

		formPanel.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				logo.setUrl(Const.genLogoUrl());
			}
		});
		logo.setUrl(Const.genLogoUrl());
	}


	@Override
	public void setClean() {
		FileUpload fileUpload = new FileUpload();
		fileUpload.setName("name");
		fileUpload.setStyleName("logoFileUpload");
		fileUpload.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				formPanel.submit();
			}
		});
		formPanel.setWidget(fileUpload);
		for (ValidatedTextBox v : new ValidatedTextBox[]{name,	ssn, vatID, address, city, province, 
				country, postcode, phone, email, mobile, fax, web}) {
			v.reset();
		}
		
		BusinessDTO b = Configuration.getBusiness();
		
		name.setText(b.getName());
		ssn.setText(b.getSsn());
		vatID.setText(b.getVatID());
		address.setText(b.getAddress());
		city.setText(b.getCity());
		province.setText(b.getProvince());
		country.setText(b.getCountry());
		postcode.setText(b.getPostcode());
		phone.setText(b.getPostcode());
		email.setText(b.getEmail());
		mobile.setText(b.getMobile());
		fax.setText(b.getFax());
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	private boolean validate(){
		boolean validationOk = true;
		for (ValidatedTextBox v : new ValidatedTextBox[]{name,	ssn, vatID, address, city, province, 
				country, postcode, phone, email, mobile, fax, web}) {
			v.validate();
			validationOk = validationOk && v.isValid();
		}
		return validationOk;
	}

	@UiHandler("saveData")
	void onSaveDataClicked(ClickEvent e){
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
			
			ServerFacade.business.update(b, new AsyncCallback<Void>() {
				
				@Override
				public void onSuccess(Void result) {
					Configuration.setBusiness(b);
					presenter.goTo(new HomePlace());
				}
				
				@Override
				public void onFailure(Throwable caught) {
					Window.alert(I18N.get.errorServerCommunication());
				}
			});
			
		} 
	}

}
