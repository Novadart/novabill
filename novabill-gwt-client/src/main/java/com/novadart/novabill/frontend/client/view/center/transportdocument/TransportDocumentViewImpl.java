package com.novadart.novabill.frontend.client.view.center.transportdocument;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.novadart.gwtshared.client.LoaderButton;
import com.novadart.gwtshared.client.textbox.RichTextArea;
import com.novadart.gwtshared.client.textbox.RichTextBox;
import com.novadart.gwtshared.client.validation.TextLengthValidation;
import com.novadart.gwtshared.client.validation.widget.ValidatedDateBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedListBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.i18n.I18NM;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.resources.GlobalCss;
import com.novadart.novabill.frontend.client.resources.ImageResources;
import com.novadart.novabill.frontend.client.util.CalcUtils;
import com.novadart.novabill.frontend.client.view.center.AccountDocument;
import com.novadart.novabill.frontend.client.view.center.AccountDocumentCss;
import com.novadart.novabill.frontend.client.view.center.ItemInsertionForm;
import com.novadart.novabill.frontend.client.view.util.LocaleWidgets;
import com.novadart.novabill.frontend.client.widget.ValidatedTextArea;
import com.novadart.novabill.frontend.client.widget.validation.ValidationKit;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;

public class TransportDocumentViewImpl extends AccountDocument implements TransportDocumentView {

	private static TransportDocumentViewImplUiBinder uiBinder = GWT
			.create(TransportDocumentViewImplUiBinder.class);

	interface TransportDocumentViewImplUiBinder extends UiBinder<Widget, TransportDocumentViewImpl> {
	}

	@UiField FlowPanel docControls;
	@UiField Label readonlyWarning;

	@UiField CheckBox setFromAddress;
	@UiField HorizontalPanel fromAddressContainer;
	@UiField(provided=true) RichTextArea fromAddrCompanyName;
	@UiField(provided=true) RichTextBox fromAddrStreetName;
	@UiField(provided=true) RichTextBox fromAddrPostCode;
	@UiField(provided=true) RichTextBox fromAddrCity;
	@UiField(provided=true) RichTextBox fromAddrProvince;
	@UiField(provided=true) ValidatedListBox fromAddrCountry;
	@UiField Button fromAddrButtonDefault;

	@UiField CheckBox setToAddress;
	@UiField HorizontalPanel toAddressContainer;
	@UiField(provided=true) RichTextArea toAddrCompanyName;
	@UiField(provided=true) RichTextBox toAddrStreetName;
	@UiField(provided=true) RichTextBox toAddrPostCode;
	@UiField(provided=true) RichTextBox toAddrCity;
	@UiField(provided=true) RichTextBox toAddrProvince;
	@UiField(provided=true) ValidatedListBox toAddrCountry;
	@UiField ListBox toAddrButtonDefault;

	@UiField(provided=true) ValidatedTextBox numberOfPackages;
	@UiField(provided=true) ValidatedTextBox totalWeight;
	@UiField(provided=true) com.novadart.gwtshared.client.validation.widget.ValidatedTextArea transporter;
	@UiField ListBox loadTransporterAddress;

	@UiField(provided=true) ValidatedDateBox transportStartDate;
	@UiField(provided=true) ValidatedListBox hour;
	@UiField(provided=true) ValidatedListBox minute;

	@UiField(provided=true) ItemInsertionForm itemInsertionForm;

	@UiField(provided=true) ValidatedTextBox transportationResponsibility;
	@UiField(provided=true) ValidatedTextBox tradeZone;
	@UiField(provided=true) com.novadart.gwtshared.client.validation.widget.ValidatedTextArea appearanceOfTheGoods;

	@UiField Label clientName;
	@UiField(provided=true) ValidatedTextBox number;
	@UiField(provided=true) ValidatedDateBox date;
	@UiField(provided=true) ValidatedTextBox cause;
	@UiField ValidatedTextArea note;

	@UiField Label totalBeforeTaxes;
	@UiField Label totalTax;
	@UiField Label totalAfterTaxes;

	@UiField Button countItems;
	@UiField Button totalWeightCalc;

	@UiField(provided=true) LoaderButton createTransportDocument;
	@UiField Button abort;

	private Presenter presenter;

	public TransportDocumentViewImpl(boolean readonly) {

		number = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NUMBER);
		transportationResponsibility = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);
		tradeZone = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);
		appearanceOfTheGoods = new com.novadart.gwtshared.client.validation.widget.ValidatedTextArea(GlobalBundle.INSTANCE.validatedWidget(), 
				new TextLengthValidation(255) {

			@Override
			public String getErrorMessage() {
				return I18NM.get.textLengthError(255);
			}
		});
		cause = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);
		numberOfPackages = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);
		totalWeight = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.DEFAULT);
		transporter = new com.novadart.gwtshared.client.validation.widget.ValidatedTextArea(GlobalBundle.INSTANCE.validatedWidget(), 
				new TextLengthValidation(255) {

			@Override
			public String getErrorMessage() {
				return I18NM.get.textLengthError(255);
			}
		});

		fromAddrCity = new RichTextBox(GlobalBundle.INSTANCE.richTextBoxCss(), I18N.INSTANCE.city());
		fromAddrCity.addStyleName(CSS.box());
		fromAddrCompanyName = new RichTextArea(GlobalBundle.INSTANCE.richTextAreaCss(), I18N.INSTANCE.companyName(), new TextLengthValidation(255) {
			@Override
			public String getErrorMessage() {
				return I18NM.get.textLengthError(getMaxLength());
			}
		});
		fromAddrCompanyName.addStyleName(CSS.box());
		fromAddrCompanyName.addStyleName(CSS.companyName());
		fromAddrPostCode = new RichTextBox(GlobalBundle.INSTANCE.richTextBoxCss(), I18N.INSTANCE.postcode());
		fromAddrPostCode.addStyleName(CSS.box());
		fromAddrStreetName = new RichTextBox(GlobalBundle.INSTANCE.richTextBoxCss(), I18N.INSTANCE.address());
		fromAddrStreetName.addStyleName(CSS.box());
		fromAddrProvince = new RichTextBox(GlobalBundle.INSTANCE.richTextBoxCss(), I18N.INSTANCE.province());
		fromAddrProvince.addStyleName(CSS.box());
		fromAddrCountry = LocaleWidgets.createCountryListBox(I18N.INSTANCE.country());

		toAddrCity = new RichTextBox(GlobalBundle.INSTANCE.richTextBoxCss(), I18N.INSTANCE.city(),ValidationKit.DEFAULT);
		toAddrCity.addStyleName(CSS.box());
		toAddrCompanyName = new RichTextArea(GlobalBundle.INSTANCE.richTextAreaCss(), I18N.INSTANCE.companyName(), new TextLengthValidation(255) {
			@Override
			public String getErrorMessage() {
				return I18NM.get.textLengthError(getMaxLength());
			} 
		}, ValidationKit.DEFAULT);
		toAddrCompanyName.addStyleName(CSS.companyName());
		toAddrCompanyName.addStyleName(CSS.box());
		toAddrPostCode = new RichTextBox(GlobalBundle.INSTANCE.richTextBoxCss(), I18N.INSTANCE.postcode(),ValidationKit.DEFAULT);
		toAddrPostCode.addStyleName(CSS.box());
		toAddrStreetName = new RichTextBox(GlobalBundle.INSTANCE.richTextBoxCss(), I18N.INSTANCE.address(),ValidationKit.DEFAULT);
		toAddrStreetName.addStyleName(CSS.box());
		toAddrProvince = new RichTextBox(GlobalBundle.INSTANCE.richTextBoxCss(), I18N.INSTANCE.province());
		toAddrProvince.addStyleName(CSS.box());
		toAddrCountry = LocaleWidgets.createCountryListBox(I18N.INSTANCE.country());

		String str;
		hour = new ValidatedListBox(GlobalBundle.INSTANCE.validatedWidget());
		for(int i=0; i<24; i++){
			str = String.valueOf(i);
			hour.addItem(str.length() < 2 ? "0"+str : str);
		}

		minute = new ValidatedListBox(GlobalBundle.INSTANCE.validatedWidget());
		for(int i=0; i<60; i++){
			str = String.valueOf(i);
			minute.addItem(str.length() < 2 ? "0"+str : str);
		}

		date = new ValidatedDateBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NOT_EMPTY_DATE);
		date.setFormat(new DateBox.DefaultFormat
				(DateTimeFormat.getFormat("dd MMMM yyyy")));
		date.addValueChangeHandler(new ValueChangeHandler<Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				transportStartDate.setValue(event.getValue());
				hour.setSelectedItemByValue("09");
				minute.setSelectedItemByValue("00");
			}
		});

		itemInsertionForm = new ItemInsertionForm(true, readonly, new ItemInsertionForm.Handler() {

			@Override
			public void onItemListUpdated(List<AccountingDocumentItemDTO> items) {
				CalcUtils.calculateTotals(itemInsertionForm.getItems(), totalTax, totalBeforeTaxes, totalAfterTaxes);
			}
		});

		transportStartDate = new ValidatedDateBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NOT_EMPTY_DATE);
		transportStartDate.setFormat(new DateBox.DefaultFormat
				(DateTimeFormat.getFormat("dd MMMM yyyy")));
		createTransportDocument = new LoaderButton(ImageResources.INSTANCE.loader(), GlobalBundle.INSTANCE.loaderButton());
		initWidget(uiBinder.createAndBindUi(this));
		setStyleName(CSS.accountDocumentView());

		createTransportDocument.getButton().setStyleName(CSS.createButton()+" btn green");
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		presenter.onLoad();
	}

	@UiFactory
	GlobalCss getGlobalCss(){
		return GlobalBundle.INSTANCE.globalCss();
	}

	@Override
	public ValidatedTextBox getNumber() {
		return number;
	}

	@UiHandler("setFromAddress")
	void onSetFromAddress(ValueChangeEvent<Boolean> e){
		fromAddressContainer.setVisible(e.getValue());
	}

	@UiHandler("setToAddress")
	void onSetToAddress(ValueChangeEvent<Boolean> e){
		toAddressContainer.setVisible(e.getValue());
	}

	@UiFactory
	I18N getI18N(){
		return I18N.INSTANCE;
	}

	@UiFactory
	AccountDocumentCss getAccountDocumentCss(){
		return CSS;
	}

	@UiHandler("fromAddrButtonDefault")
	void onFromAddressButtonDefaultCLicked(ClickEvent e){
		presenter.onFromAddressButtonDefaultCLicked();
	}

	@UiHandler("toAddrButtonDefault")
	void onToAddressButtonDefaultChange(ChangeEvent e){
		presenter.onToAddressButtonDefaultChange();
	}
	
	@UiHandler("loadTransporterAddress")
	void onLoadTransporterAddressChange(ChangeEvent e){
		presenter.onLoadTransporterAddressChange();
	}

	@UiHandler("createTransportDocument")
	void onCreateTransportDocumentClicked(ClickEvent e){
		presenter.onCreateDocumentClicked();
	}

	@UiHandler("countItems")
	void onCountItemsClicked(ClickEvent e){
		presenter.onCountItemsCLicked();
	}

	@UiHandler("totalWeightCalc")
	void onTotalWeightCalcClicked(ClickEvent e){
		presenter.onTotalWeightCalcClicked();
	}

	@UiHandler("abort")
	void onCancelClicked(ClickEvent e){
		presenter.onCancelClicked();
	}


	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void reset() {
		number.reset();
		readonlyWarning.setVisible(false);

		//reset widget contents	
		date.reset();
		transportStartDate.reset();
		note.reset();
		numberOfPackages.reset();
		totalWeight.reset();
		transporter.reset();
		transportationResponsibility.reset();
		appearanceOfTheGoods.reset();
		cause.reset();
		cause.setText(I18N.INSTANCE.transportDocumentCauseDefaultText());
		tradeZone.reset();
		hour.reset();
		minute.reset();
		numberOfPackages.reset();

		setFromAddress.setValue(false);
		fromAddressContainer.setVisible(false);
		fromAddrCity.reset();
		fromAddrCompanyName.reset();
		fromAddrPostCode.reset();
		fromAddrStreetName.reset();
		fromAddrProvince.setEnabled(true);
		fromAddrProvince.reset();
		fromAddrCountry.reset();

		setToAddress.setValue(false);
		toAddressContainer.setVisible(false);
		toAddrCity.reset();
		toAddrCompanyName.reset();
		toAddrPostCode.reset();
		toAddrStreetName.reset();
		toAddrProvince.setEnabled(true);
		toAddrProvince.reset();
		toAddrCountry.reset();

		itemInsertionForm.reset();

		totalTax.setText("");
		totalBeforeTaxes.setText("");
		totalAfterTaxes.setText("");

		createTransportDocument.reset();
		setLocked(false);
	}

	@Override
	public void setLocked(boolean value) {
		fromAddrCompanyName.setEnabled(!value);
		fromAddrStreetName.setEnabled(!value);
		fromAddrPostCode.setEnabled(!value);
		fromAddrCity.setEnabled(!value);
		fromAddrProvince.setEnabled(!value);
		fromAddrCountry.setEnabled(!value);
		fromAddrButtonDefault.setEnabled(!value);

		totalWeightCalc.setEnabled(!value);
		countItems.setEnabled(!value);

		toAddrCompanyName.setEnabled(!value);
		toAddrStreetName.setEnabled(!value);
		toAddrPostCode.setEnabled(!value);
		toAddrCity.setEnabled(!value);
		toAddrProvince.setEnabled(!value);
		toAddrCountry.setEnabled(!value);
		toAddrButtonDefault.setEnabled(!value);

		numberOfPackages.setEnabled(!value);
		totalWeight.setEnabled(!value);
		transporter.setEnabled(!value);
		loadTransporterAddress.setEnabled(!value);

		appearanceOfTheGoods.setEnabled(!value);
		cause.setEnabled(!value);
		transportStartDate.setEnabled(!value);
		hour.setEnabled(!value);
		minute.setEnabled(!value);

		itemInsertionForm.setLocked(value);

		transportationResponsibility.setEnabled(!value);
		tradeZone.setEnabled(!value);

		number.setEnabled(!value);
		date.setEnabled(!value);
		note.setEnabled(!value);

		createTransportDocument.setEnabled(!value);
		abort.setEnabled(!value);
	}

	@Override
	public LoaderButton getCreateDocument() {
		return createTransportDocument;
	}

	@Override
	public Button getAbort() {
		return abort;
	}

	@Override
	public ValidatedDateBox getDate() {
		return date;
	}

	@Override
	public Label getClientName() {
		return clientName;
	}

	@Override
	public Label getTotalAfterTaxes() {
		return totalAfterTaxes;
	}

	@Override
	public Label getTotalTax() {
		return totalTax;
	}

	@Override
	public Label getTotalBeforeTaxes() {
		return totalBeforeTaxes;
	}

	@Override
	public ItemInsertionForm getItemInsertionForm() {
		return itemInsertionForm;
	}

	@Override
	public ValidatedTextArea getNote() {
		return note;
	}

	public RichTextArea getFromAddrCompanyName() {
		return fromAddrCompanyName;
	}

	public RichTextBox getFromAddrStreetName() {
		return fromAddrStreetName;
	}

	public RichTextBox getFromAddrPostCode() {
		return fromAddrPostCode;
	}

	public RichTextBox getFromAddrCity() {
		return fromAddrCity;
	}

	public RichTextBox getFromAddrProvince() {
		return fromAddrProvince;
	}

	public ValidatedListBox getFromAddrCountry() {
		return fromAddrCountry;
	}

	public Button getFromAddrButtonDefault() {
		return fromAddrButtonDefault;
	}

	public RichTextArea getToAddrCompanyName() {
		return toAddrCompanyName;
	}

	public RichTextBox getToAddrStreetName() {
		return toAddrStreetName;
	}

	public RichTextBox getToAddrPostCode() {
		return toAddrPostCode;
	}

	public RichTextBox getToAddrCity() {
		return toAddrCity;
	}

	public RichTextBox getToAddrProvince() {
		return toAddrProvince;
	}

	public ValidatedListBox getToAddrCountry() {
		return toAddrCountry;
	}

	public ListBox getToAddrButtonDefault() {
		return toAddrButtonDefault;
	}

	public ValidatedTextBox getNumberOfPackages() {
		return numberOfPackages;
	}

	public ValidatedTextBox getTotalWeight() {
		return totalWeight;
	}

	public com.novadart.gwtshared.client.validation.widget.ValidatedTextArea getTransporter() {
		return transporter;
	}
	
	public ListBox getLoadTransporterAddress() {
		return loadTransporterAddress;
	}

	public ValidatedDateBox getTransportStartDate() {
		return transportStartDate;
	}

	public ValidatedListBox getHour() {
		return hour;
	}

	public ValidatedListBox getMinute() {
		return minute;
	}

	public ValidatedTextBox getTransportationResponsibility() {
		return transportationResponsibility;
	}

	public ValidatedTextBox getTradeZone() {
		return tradeZone;
	}

	public ValidatedTextBox getCause() {
		return cause;
	}

	public com.novadart.gwtshared.client.validation.widget.ValidatedTextArea getAppearanceOfTheGoods() {
		return appearanceOfTheGoods;
	}

	@Override
	public CheckBox getSetFromAddress() {
		return setFromAddress;
	}

	@Override
	public CheckBox getSetToAddress() {
		return setToAddress;
	}

	@Override
	public HorizontalPanel getFromAddressContainer() {
		return fromAddressContainer;
	}

	@Override
	public HorizontalPanel getToAddressContainer() {
		return toAddressContainer;
	}

	@Override
	public Button getCountItems() {
		return countItems;
	}

	@Override
	public Label getReadonlyWarning() {
		return readonlyWarning;
	}
}
