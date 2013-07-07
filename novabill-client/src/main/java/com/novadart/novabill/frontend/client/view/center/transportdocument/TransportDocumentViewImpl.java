package com.novadart.novabill.frontend.client.view.center.transportdocument;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.novadart.gwtshared.client.LoaderButton;
import com.novadart.gwtshared.client.textbox.RichTextBox;
import com.novadart.gwtshared.client.validation.ValidationBundle;
import com.novadart.gwtshared.client.validation.widget.ValidatedDateBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedListBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.resources.GlobalCss;
import com.novadart.novabill.frontend.client.resources.ImageResources;
import com.novadart.novabill.frontend.client.util.DocumentUtils;
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

	@UiField Label titleLabel;
	@UiField FlowPanel docControls;
	@UiField ScrollPanel docScroll;

	@UiField(provided=true) RichTextBox fromAddrCompanyName;
	@UiField(provided=true) RichTextBox fromAddrStreetName;
	@UiField(provided=true) RichTextBox fromAddrPostCode;
	@UiField(provided=true) RichTextBox fromAddrCity;
	@UiField(provided=true) ValidatedListBox fromAddrProvince;
	@UiField(provided=true) ValidatedListBox fromAddrCountry;
	@UiField Button fromAddrButtonDefault;
	
	@UiField(provided=true) RichTextBox toAddrCompanyName;
	@UiField(provided=true) RichTextBox toAddrStreetName;
	@UiField(provided=true) RichTextBox toAddrPostCode;
	@UiField(provided=true) RichTextBox toAddrCity;
	@UiField(provided=true) ValidatedListBox toAddrProvince;
	@UiField(provided=true) ValidatedListBox toAddrCountry;
	@UiField Button toAddrButtonDefault;
	
	@UiField(provided=true) ValidatedTextBox numberOfPackages;
	@UiField(provided=true) ValidatedTextBox transporter;

	@UiField(provided=true) ValidatedDateBox transportStartDate;
	@UiField(provided=true) ValidatedListBox hour;
	@UiField(provided=true) ValidatedListBox minute;

	@UiField(provided=true) ItemInsertionForm itemInsertionForm;

	@UiField TextBox transportationResponsibility;
	@UiField TextBox tradeZone;

	@UiField Label clientName;
	@UiField(provided=true) ValidatedTextBox number;
	@UiField(provided=true) ValidatedDateBox date;
	@UiField TextBox cause;
	@UiField ValidatedTextArea note;

	@UiField Label totalBeforeTaxes;
	@UiField Label totalTax;
	@UiField Label totalAfterTaxes;

	@UiField(provided=true) LoaderButton createTransportDocument;
	@UiField Button abort;

	private Presenter presenter;

	public TransportDocumentViewImpl() {
		ValidationBundle<String> nev = new ValidationBundle<String>() {

			@Override
			public boolean isValid(String value) { 	return !value.isEmpty(); }

			@Override
			public String getErrorMessage() {	return null; }
		};

		number = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NUMBER);

		numberOfPackages = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NUMBER);
		transporter = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), nev);

		fromAddrCity = new RichTextBox(GlobalBundle.INSTANCE.richTextBoxCss(), I18N.INSTANCE.city(), nev);
		fromAddrCompanyName = new RichTextBox(GlobalBundle.INSTANCE.richTextBoxCss(), I18N.INSTANCE.companyName(),nev);
		fromAddrPostCode = new RichTextBox(GlobalBundle.INSTANCE.richTextBoxCss(), I18N.INSTANCE.postcode(), nev);
		fromAddrStreetName = new RichTextBox(GlobalBundle.INSTANCE.richTextBoxCss(), I18N.INSTANCE.address(),nev);
		fromAddrProvince = LocaleWidgets.createProvinceListBox(I18N.INSTANCE.province());
		fromAddrCountry = LocaleWidgets.createCountryListBox(I18N.INSTANCE.country());

		toAddrCity = new RichTextBox(GlobalBundle.INSTANCE.richTextBoxCss(), I18N.INSTANCE.city(),nev);
		toAddrCompanyName = new RichTextBox(GlobalBundle.INSTANCE.richTextBoxCss(), I18N.INSTANCE.companyName(), nev);
		toAddrPostCode = new RichTextBox(GlobalBundle.INSTANCE.richTextBoxCss(), I18N.INSTANCE.postcode(),nev);
		toAddrStreetName = new RichTextBox(GlobalBundle.INSTANCE.richTextBoxCss(), I18N.INSTANCE.address(),nev);
		toAddrProvince = LocaleWidgets.createProvinceListBox(I18N.INSTANCE.province());
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

		itemInsertionForm = new ItemInsertionForm(new ItemInsertionForm.Handler() {

			@Override
			public void onItemListUpdated(List<AccountingDocumentItemDTO> items) {
				DocumentUtils.calculateTotals(itemInsertionForm.getItems(), totalTax, totalBeforeTaxes, totalAfterTaxes);
			}
		});

		transportStartDate = new ValidatedDateBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NOT_EMPTY_DATE);
		transportStartDate.setFormat(new DateBox.DefaultFormat
				(DateTimeFormat.getFormat("dd MMMM yyyy")));
		createTransportDocument = new LoaderButton(ImageResources.INSTANCE.loader(), GlobalBundle.INSTANCE.loaderButton());
		initWidget(uiBinder.createAndBindUi(this));
		setStyleName(CSS.accountDocumentView());

		createTransportDocument.getButton().setStyleName(CSS.createButton()+" "+GlobalBundle.INSTANCE.globalCss().button());
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		presenter.onLoad();
	}

	@Override
	protected Element getBody() {
		return docScroll.getElement();
	}

	@Override
	protected Element[] getNonBodyElements() {
		return new Element[]{titleLabel.getElement(), docControls.getElement()};
	}

	@UiFactory
	GlobalCss getGlobalCss(){
		return GlobalBundle.INSTANCE.globalCss();
	}
	
	@Override
	public ScrollPanel getDocScroll() {
		return docScroll;
	}

	@Override
	public ValidatedTextBox getNumber() {
		return number;
	}

	@UiHandler("fromAddrCountry")
	void onFromCountryChange(ChangeEvent event){
		presenter.onFromCountryChange();
	}

	@UiHandler("toAddrCountry")
	void onToCountryChange(ChangeEvent event){
		presenter.onToCountryChange();
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
	void onToAddressButtonDefaultCLicked(ClickEvent e){
		presenter.onToAddressButtonDefaultCLicked();
	}


	@UiHandler("createTransportDocument")
	void onCreateTransportDocumentClicked(ClickEvent e){
		presenter.onCreateDocumentClicked();
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

		//reset widget contents	
		date.reset();
		transportStartDate.reset();
		note.reset();
		numberOfPackages.reset();
		transporter.reset();
		transportationResponsibility.setText("");
		tradeZone.setText("");
		hour.reset();
		minute.reset();
		numberOfPackages.reset();

		fromAddrCity.reset();
		fromAddrCompanyName.reset();
		fromAddrPostCode.reset();
		fromAddrStreetName.reset();
		fromAddrProvince.setEnabled(true);
		fromAddrProvince.reset();
		fromAddrCountry.reset();

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
		
		toAddrCompanyName.setEnabled(!value);
		toAddrStreetName.setEnabled(!value);
		toAddrPostCode.setEnabled(!value);
		toAddrCity.setEnabled(!value);
		toAddrProvince.setEnabled(!value);
		toAddrCountry.setEnabled(!value);
		toAddrButtonDefault.setEnabled(!value);

		numberOfPackages.setEnabled(!value);
		transporter.setEnabled(!value);

		transportStartDate.setEnabled(!value);
		hour.setEnabled(!value);
		minute.setEnabled(!value);

		itemInsertionForm.setLocked(value);

		transportationResponsibility.setEnabled(!value);
		tradeZone.setEnabled(!value);

		number.setEnabled(!value);
		date.setEnabled(!value);
		note.setEnabled(!value);

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

	public Label getTitleLabel() {
		return titleLabel;
	}

	public RichTextBox getFromAddrCompanyName() {
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

	public ValidatedListBox getFromAddrProvince() {
		return fromAddrProvince;
	}

	public ValidatedListBox getFromAddrCountry() {
		return fromAddrCountry;
	}

	public Button getFromAddrButtonDefault() {
		return fromAddrButtonDefault;
	}

	public RichTextBox getToAddrCompanyName() {
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

	public ValidatedListBox getToAddrProvince() {
		return toAddrProvince;
	}

	public ValidatedListBox getToAddrCountry() {
		return toAddrCountry;
	}

	public Button getToAddrButtonDefault() {
		return toAddrButtonDefault;
	}

	public ValidatedTextBox getNumberOfPackages() {
		return numberOfPackages;
	}

	public ValidatedTextBox getTransporter() {
		return transporter;
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

	public TextBox getTransportationResponsibility() {
		return transportationResponsibility;
	}

	public TextBox getTradeZone() {
		return tradeZone;
	}

	public TextBox getCause() {
		return cause;
	}
	
}
