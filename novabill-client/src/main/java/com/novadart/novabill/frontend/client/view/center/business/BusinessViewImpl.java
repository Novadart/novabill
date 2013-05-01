package com.novadart.novabill.frontend.client.view.center.business;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
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
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.resources.GlobalCss;
import com.novadart.novabill.frontend.client.resources.ImageResources;
import com.novadart.novabill.frontend.client.view.HasUILocking;
import com.novadart.novabill.frontend.client.view.util.LocaleWidgets;
import com.novadart.novabill.frontend.client.widget.notification.InlineNotification;
import com.novadart.novabill.frontend.client.widget.validation.AlternativeSsnVatIdValidation;
import com.novadart.novabill.frontend.client.widget.validation.ValidationKit;

public class BusinessViewImpl extends Composite implements BusinessView, HasUILocking {

	private static BusinessViewImplUiBinder uiBinder = GWT
			.create(BusinessViewImplUiBinder.class);

	interface BusinessViewImplUiBinder extends
	UiBinder<Widget, BusinessViewImpl> {
	}


	private BusinessView.Presenter presenter;

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

	@UiField(provided=true) LoaderButton saveData;
	@UiField Button exportClientData;
	@UiField Button exportInvoiceData;
	@UiField Button exportEstimationData;
	@UiField Button exportCreditNoteData;
	@UiField Button exportTransportDocumentData;
	
	@UiField Anchor deleteAccount;
	
	private AlternativeSsnVatIdValidation ssnOrVatIdValidation = new AlternativeSsnVatIdValidation();

	public BusinessViewImpl() {

		name = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NOT_EMPTY);
		ssn = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.SSN_OR_VAT_ID);
		ssn.setShowMessageOnError(true);
		vatID = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.VAT_ID);
		vatID.setShowMessageOnError(true);
		ssnOrVatIdValidation.addWidget(ssn);
		ssnOrVatIdValidation.addWidget(vatID);

		address = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NOT_EMPTY);
		city = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NOT_EMPTY);
		province = LocaleWidgets.createProvinceListBox("");
		country = LocaleWidgets.createCountryListBoxItalyOnly("");
		postcode = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.POSTCODE);
		phone = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);
		email = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.OPTIONAL_EMAIL);
		mobile = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);
		fax = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);
		web = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);

		saveData = new LoaderButton(ImageResources.INSTANCE.loader(), GlobalBundle.INSTANCE.loaderButton());

		initWidget(uiBinder.createAndBindUi(this));

		saveData.getButton().addStyleName(GlobalBundle.INSTANCE.globalCss().button());

		logo.setUrl(ClientFactory.INSTANCE.getLogoUrl());

		formPanel.setMethod(FormPanel.METHOD_POST);
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);

		formPanel.addSubmitHandler(new FormPanel.SubmitHandler() {

			@Override
			public void onSubmit(SubmitEvent event) {
				presenter.onLogoSubmit();
			}
		});

		formPanel.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				presenter.onLogoSubmitComplete(event.getResults());
			}
		});
		
		deleteAccount.setHref(ClientFactory.INSTANCE.getDeleteAccountUrl());

	}

	@UiFactory
	I18N getI18n(){
		return I18N.INSTANCE;
	}

	@UiFactory
	GlobalCss getGlobalCss(){
		return GlobalBundle.INSTANCE.globalCss();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		presenter.onLoad();
	}


	@UiHandler("removeLogo")
	void onRemoveLogoClicked(ClickEvent e){
		presenter.onRemoveLogoClicked();
	}

	@UiHandler("updateLogo")
	void onUpdateLogoClicked(ClickEvent e){
		presenter.onUpdateLogoClicked();
	}


	@Override
	public void reset() {
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
		province.reset();
		country.reset();
		web.reset();
		inlineNotification.hide();
		saveData.reset();
		setLocked(false);
	}

	@UiHandler("saveData")
	void onSaveDataClicked(ClickEvent e){
		presenter.onSaveDataClicked();
	}

	@UiHandler("exportClientData")
	void onExportClientDataClicked(ClickEvent e){
		presenter.onExportClientDataClicked();
	}

	@UiHandler("exportInvoiceData")
	void onExportInvoiceDataClicked(ClickEvent e){
		presenter.onExportInvoiceDataClicked();
	}

	@UiHandler("exportEstimationData")
	void onExportEstimationDataClicked(ClickEvent e){
		presenter.onExportEstimationDataClicked();
	}

	@UiHandler("exportCreditNoteData")
	void onExportCreditNoteDataClicked(ClickEvent e){
		presenter.onExportCreditNoteDataClicked();
	}

	@UiHandler("exportTransportDocumentData")
	void onExportTransportDocumentDataClicked(ClickEvent e){
		presenter.onExportTransportDocumentDataClicked();
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

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public Button getUpdateLogo() {
		return updateLogo;
	}

	@Override
	public Button getRemoveLogo() {
		return removeLogo;
	}

	@Override
	public FormPanel getFormPanel() {
		return formPanel;
	}

	@Override
	public Image getLogo() {
		return logo;
	}

	@Override
	public InlineNotification getInlineNotification() {
		return inlineNotification;
	}

	@Override
	public ValidatedTextBox getName() {
		return name;
	}

	@Override
	public ValidatedTextBox getSsn() {
		return ssn;
	}

	@Override
	public ValidatedTextBox getVatID() {
		return vatID;
	}

	@Override
	public ValidatedTextBox getAddress() {
		return address;
	}

	@Override
	public ValidatedTextBox getCity() {
		return city;
	}

	@Override
	public ValidatedListBox getProvince() {
		return province;
	}

	@Override
	public ValidatedListBox getCountry() {
		return country;
	}

	@Override
	public ValidatedTextBox getPostcode() {
		return postcode;
	}

	@Override
	public ValidatedTextBox getPhone() {
		return phone;
	}

	@Override
	public ValidatedTextBox getEmail() {
		return email;
	}

	@Override
	public ValidatedTextBox getMobile() {
		return mobile;
	}

	@Override
	public ValidatedTextBox getFax() {
		return fax;
	}

	@Override
	public ValidatedTextBox getWeb() {
		return web;
	}

	@Override
	public LoaderButton getSaveData() {
		return saveData;
	}

	@Override
	public Button getExportClientData() {
		return exportClientData;
	}

	@Override
	public Button getExportInvoiceData() {
		return exportInvoiceData;
	}

	@Override
	public Button getExportEstimationData() {
		return exportEstimationData;
	}

	@Override
	public Button getExportCreditNoteData() {
		return exportCreditNoteData;
	}

	@Override
	public Button getExportTransportDocumentData() {
		return exportTransportDocumentData;
	}

	@Override
	public AlternativeSsnVatIdValidation getSsnOrVatIdValidation() {
		return ssnOrVatIdValidation;
	}
	
	@Override
	public Anchor getDeleteAccount() {
		return deleteAccount;
	}

}
