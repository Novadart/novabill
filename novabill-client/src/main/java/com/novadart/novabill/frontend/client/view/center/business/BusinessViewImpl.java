package com.novadart.novabill.frontend.client.view.center.business;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.gwtshared.client.LoaderButton;
import com.novadart.gwtshared.client.validation.widget.ValidatedListBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.Const;
import com.novadart.novabill.frontend.client.event.BusinessUpdateEvent;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.util.ExportUtils;
import com.novadart.novabill.frontend.client.view.HasUILocking;
import com.novadart.novabill.frontend.client.view.center.BusinessView;
import com.novadart.novabill.frontend.client.view.util.LocaleWidgets;
import com.novadart.novabill.frontend.client.widget.notification.InlineNotification;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.validation.AlternativeSsnVatIdValidation;
import com.novadart.novabill.frontend.client.widget.validation.ValidationKit;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.facade.LogoUploadStatus;

public class BusinessViewImpl extends Composite implements BusinessView, HasUILocking {

	private static BusinessViewImplUiBinder uiBinder = GWT
			.create(BusinessViewImplUiBinder.class);

	interface BusinessViewImplUiBinder extends
	UiBinder<Widget, BusinessViewImpl> {
	}

	private Presenter presenter;
	private EventBus eventBus;

	@UiField Button updateLogo;
	@UiField Button removeLogo;
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
	@UiField(provided=true) ValidatedTextBox web;
	
	@UiField LoaderButton saveData;
	@UiField Button exportClientData;
	@UiField Button exportInvoiceData;
	@UiField Button exportEstimationData;
	@UiField Button exportCreditNoteData;
	@UiField Button exportTransportDocumentData;
	
	private boolean logoUpdateCompleted = true;
	private AlternativeSsnVatIdValidation ssnOrVatIdValidation = new AlternativeSsnVatIdValidation();

	public BusinessViewImpl() {

		name = new ValidatedTextBox(ValidationKit.NOT_EMPTY);
		ssn = new ValidatedTextBox(ValidationKit.SSN_OR_VAT_ID);
		ssn.setShowMessageOnError(true);
		vatID = new ValidatedTextBox(ValidationKit.VAT_ID);
		vatID.setShowMessageOnError(true);
		ssnOrVatIdValidation.addWidget(ssn);
		ssnOrVatIdValidation.addWidget(vatID);
		
		address = new ValidatedTextBox(ValidationKit.NOT_EMPTY);
		city = new ValidatedTextBox(ValidationKit.NOT_EMPTY);
		province = LocaleWidgets.createProvinceListBox("");
		country = LocaleWidgets.createCountryListBoxItalyOnly("");
		postcode = new ValidatedTextBox(ValidationKit.POSTCODE);
		phone = new ValidatedTextBox(ValidationKit.DEFAULT);
		email = new ValidatedTextBox(ValidationKit.OPTIONAL_EMAIL);
		mobile = new ValidatedTextBox(ValidationKit.DEFAULT);
		fax = new ValidatedTextBox(ValidationKit.DEFAULT);
		web = new ValidatedTextBox(ValidationKit.DEFAULT);

		initWidget(uiBinder.createAndBindUi(this));
		
		saveData.getButton().setStyleName("saveData button");
		
		logo.setUrl(Const.genLogoUrl());
		
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
				
				// for some reason Internet Explorer wraps the number in <pre></pre>, thus instead of 0 you get <pre>0</pre>
				if(resultCodeStr.toLowerCase().contains("<pre>")){
					resultCodeStr = String.valueOf(resultCodeStr.charAt(5));
				}
				
				int resultCode = Integer.parseInt(resultCodeStr);
				
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
					formPanel.setVisible(false);
					formPanel.reset();
					updateLogo.setVisible(true);
					break;
				
				}
				
				logoUpdateCompleted = true;
			}
		});
		
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		
		BusinessDTO b = Configuration.getBusiness();
		
		name.setText(b.getName());
		ssn.setText(b.getSsn());
		vatID.setText(b.getVatID());
		address.setText(b.getAddress());
		city.setText(b.getCity());
		province.setSelectedItem(b.getProvince());
		country.setSelectedItemByValue(b.getCountry());
		postcode.setText(b.getPostcode());
		phone.setText(b.getPhone());
		email.setText(b.getEmail());
		mobile.setText(b.getMobile());
		fax.setText(b.getFax());
		web.setText(b.getWeb());
	}
	
	
	@UiHandler("removeLogo")
	void onRemoveLogoClicked(ClickEvent e){
		ServerFacade.deleteLogo(new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				formPanel.setVisible(false);
				formPanel.reset();
				updateLogo.setVisible(true);
				logo.setUrl(Const.genLogoUrl());
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Notification.showMessage(I18N.INSTANCE.errorServerCommunication());
			}
		});
	}
	
	@UiHandler("updateLogo")
	void onUpdateLogoClicked(ClickEvent e){
		ServerFacade.business.generateLogoOpToken(new ManagedAsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				formPanel.setAction(Const.UPDATE_LOGO+result);
				updateLogo.setVisible(false);
				formPanel.setVisible(true);
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
		ssnOrVatIdValidation.reset();
		
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
		web.reset();
		inlineNotification.hide();
		saveData.reset();
		setLocked(false);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@Override
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	private boolean validate(){
		boolean validationOk = true;
		inlineNotification.hide();
		
		ssnOrVatIdValidation.validate();
		if(!ssnOrVatIdValidation.isValid()){
			inlineNotification.showMessage(ssnOrVatIdValidation.getErrorMessage());
			validationOk = false;
		}
		
		for (ValidatedTextBox v : new ValidatedTextBox[]{name, address, city, 
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
			
			saveData.showLoader(true);
			setLocked(true);
			
			ServerFacade.business.update(b, new ManagedAsyncCallback<Void>() {
				
				@Override
				public void onSuccess(Void result) {
					saveData.showLoader(true);
					Configuration.setBusiness(b);
					presenter.goTo(new HomePlace());
					eventBus.fireEvent(new BusinessUpdateEvent(b));
					setLocked(false);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					saveData.showLoader(true);
					super.onFailure(caught);
					setLocked(false);
				}
			});
			
		} 
	}
	
	@UiHandler("exportClientData")
	void onExportClientDataClicked(ClickEvent e){
		ExportUtils.exportData(true, false, false, false, false);
	}

	@UiHandler("exportInvoiceData")
	void onExportInvoiceDataClicked(ClickEvent e){
		ExportUtils.exportData(false, true, false, false, false);
	}
	
	@UiHandler("exportEstimationData")
	void onExportEstimationDataClicked(ClickEvent e){
		ExportUtils.exportData(false, false, true, false, false);
	}
	
	@UiHandler("exportCreditNoteData")
	void onExportCreditNoteDataClicked(ClickEvent e){
		ExportUtils.exportData(false, false, false, true, false);
	}
	
	@UiHandler("exportTransportDocumentData")
	void onExportTransportDocumentDataClicked(ClickEvent e){
		ExportUtils.exportData(false, false, false, false, true);
	}
	
	@Override
	public void setLocked(boolean value) {
		updateLogo.setEnabled(!value);
		removeLogo.setEnabled(!value);
		
		name.setEnabled(!value);
		ssn.setEnabled(!value);
		vatID.setEnabled(!value);
		address.setEnabled(!value);
		city.setEnabled(!value);
		province.setEnabled(!value);
		country.setEnabled(!value);
		postcode.setEnabled(!value);
		phone.setEnabled(!value);
		email.setEnabled(!value);
		mobile.setEnabled(!value);
		fax.setEnabled(!value);
		web.setEnabled(!value);
		exportClientData.setEnabled(!value);
		exportEstimationData.setEnabled(!value);
		exportInvoiceData.setEnabled(!value);
		exportCreditNoteData.setEnabled(!value);
		exportTransportDocumentData.setEnabled(!value);
	}
	
}
