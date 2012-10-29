package com.novadart.novabill.frontend.client.ui.center.business;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.gwtshared.client.validation.Validation;
import com.novadart.gwtshared.client.validation.widget.ValidatedListBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.Const;
import com.novadart.novabill.frontend.client.datawatcher.DataWatcher;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.ui.center.BusinessView;
import com.novadart.novabill.frontend.client.ui.util.LocaleWidgets;
import com.novadart.novabill.frontend.client.ui.widget.notification.InlineNotification;
import com.novadart.novabill.frontend.client.ui.widget.notification.Notification;
import com.novadart.novabill.frontend.client.ui.widget.validation.EmailValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.NotEmptyValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.NumberValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.PostcodeValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.SsnOrVatIdValidation;
import com.novadart.novabill.frontend.client.ui.widget.validation.VatIdValidation;
import com.novadart.novabill.frontend.client.util.ExportUtils;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.facade.LogoUploadStatus;

public class BusinessViewImpl extends Composite implements BusinessView {

	private static BusinessViewImplUiBinder uiBinder = GWT
			.create(BusinessViewImplUiBinder.class);

	interface BusinessViewImplUiBinder extends
	UiBinder<Widget, BusinessViewImpl> {
	}

	private Presenter presenter;

	@UiField FormPanel formPanel;
	@UiField Image logo;
	
	@UiField InlineNotification inlineNotification;

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
	@UiField ValidatedTextBox web;
	
	private boolean logoUpdateCompleted = true;

	public BusinessViewImpl() {
		BusinessDTO b = Configuration.getBusiness();

		NotEmptyValidation nev = new NotEmptyValidation();
		NumberValidation nuv = new NumberValidation(true);

		name = new ValidatedTextBox(nev);
		name.setText(b.getName());
		ssn = new ValidatedTextBox(new SsnOrVatIdValidation(true));
		ssn.setText(b.getSsn());
		vatID = new ValidatedTextBox(new VatIdValidation(true));
		vatID.setText(b.getVatID());
		address = new ValidatedTextBox(nev);
		address.setText(b.getAddress());
		city = new ValidatedTextBox(nev);
		city.setText(b.getCity());
		province = LocaleWidgets.createProvinceListBox("");
		province.setSelectedItem(b.getProvince());
		country = LocaleWidgets.createCountryListBox("");
		country.setSelectedItemByValue(b.getCountry());
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
		
		formPanel.addSubmitHandler(new FormPanel.SubmitHandler() {
			
			@Override
			public void onSubmit(SubmitEvent event) {
				logoUpdateCompleted = false;
			}
		});
		
		formPanel.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				String resultCodeStr = event.getResults();
				logoUpdateCompleted = true;
				
				int resultCode = 0;
				
				if(! Validation.isPositiveNumber(resultCodeStr)){
					Notification.showMessage(I18N.INSTANCE.errorLogoIllegalRequest());
					return;
				} else {
					resultCode = Integer.parseInt(resultCodeStr);
					if(resultCode > LogoUploadStatus.values().length){
						Notification.showMessage(I18N.INSTANCE.errorLogoIllegalRequest());
						return;	
					}
				}
				
				LogoUploadStatus status = LogoUploadStatus.values()[resultCode];
				switch(status){
				case ILLEGAL_PAYLOAD:
					Notification.showMessage(I18N.INSTANCE.errorLogoIllegalFile());
					break;
					
				case ILLEGAL_SIZE:
					Notification.showMessage(I18N.INSTANCE.errorLogoSizeTooBig());
					break;
					
					default:
				case ILLEGAL_REQUEST:
				case INTERNAL_ERROR:
					Notification.showMessage(I18N.INSTANCE.errorLogoIllegalRequest());
					break;
					
				case OK:
					logo.setUrl(Const.genLogoUrl());
					break;
				
				}
			}
		});
		
	}


	@Override
	public void setClean() {
		FileUpload fileUpload = new FileUpload();
		fileUpload.setName("file");
		fileUpload.setStyleName("logoFileUpload");
		fileUpload.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				formPanel.submit();
			}
		});
		formPanel.setWidget(fileUpload);
		for (ValidatedTextBox v : new ValidatedTextBox[]{name,	ssn, vatID, address, city, 
				postcode, phone, email, mobile, fax, web}) {
			v.reset();
		}
		
		BusinessDTO b = Configuration.getBusiness();
		
		name.setText(b.getName());
		ssn.setText(b.getSsn());
		vatID.setText(b.getVatID());
		address.setText(b.getAddress());
		city.setText(b.getCity());
		province.reset();
		province.setSelectedItem(b.getProvince());
		country.reset();
		country.setSelectedItemByValue(b.getCountry());
		postcode.setText(b.getPostcode());
		phone.setText(b.getPostcode());
		email.setText(b.getEmail());
		mobile.setText(b.getMobile());
		fax.setText(b.getFax());
		inlineNotification.hide();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	private boolean validate(){
		boolean validationOk = true;
		inlineNotification.hide();
		
		if(vatID.getText().isEmpty() && ssn.getText().isEmpty()){
			inlineNotification.showMessage(I18N.INSTANCE.fillVatIdOrSsn());
			ssn.setValidationErrorStyle();
			vatID.setValidationErrorStyle();
			validationOk = false;
		}
		
		for (ValidatedTextBox v : new ValidatedTextBox[]{name,	ssn, vatID, address, city, 
				postcode, phone, email, mobile, fax, web}) {
			v.validate();
			validationOk = validationOk && v.isValid();
		}
		
		province.validate();
		validationOk = validationOk && province.isValid();
		country.validate();
		validationOk = validationOk && country.isValid();
		return validationOk;
	}

	@UiHandler("saveData")
	void onSaveDataClicked(ClickEvent e){
		if(!logoUpdateCompleted){
			Notification.showMessage(I18N.INSTANCE.errorLogoNotYetUploaded());
			return;
		}
		
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
			
			ServerFacade.business.update(b, new AsyncCallback<Void>() {
				
				@Override
				public void onSuccess(Void result) {
					Configuration.setBusiness(b);
					presenter.goTo(new HomePlace());
					DataWatcher.getInstance().fireBusinessEvent();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					Notification.showMessage(I18N.INSTANCE.errorServerCommunication());
				}
			});
			
		} 
	}
	
	@UiHandler("exportClientData")
	void onExportClientDataClicked(ClickEvent e){
		ExportUtils.exportData(true, false, false);
	}

	@UiHandler("exportInvoiceData")
	void onExportInvoiceDataClicked(ClickEvent e){
		ExportUtils.exportData(false, true, false);
	}
	
	@UiHandler("exportEstimationData")
	void onExportEstimationDataClicked(ClickEvent e){
		ExportUtils.exportData(false, false, true);
	}
	
}
