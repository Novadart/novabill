package com.novadart.novabill.frontend.client.view.center.invoice;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
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
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.view.center.AccountDocument;
import com.novadart.novabill.frontend.client.view.center.AccountDocumentCss;
import com.novadart.novabill.frontend.client.view.center.ItemInsertionForm;
import com.novadart.novabill.frontend.client.view.util.LocaleWidgets;
import com.novadart.novabill.frontend.client.widget.ValidatedTextArea;
import com.novadart.novabill.frontend.client.widget.payment.SelectPayment;
import com.novadart.novabill.frontend.client.widget.tip.TipFactory;
import com.novadart.novabill.frontend.client.widget.tip.Tips;
import com.novadart.novabill.frontend.client.widget.validation.ValidationKit;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;

import java.util.Date;
import java.util.List;

public class InvoiceViewImpl extends AccountDocument implements InvoiceView {

	private static InvoiceViewImplUiBinder uiBinder = GWT
			.create(InvoiceViewImplUiBinder.class);

	interface InvoiceViewImplUiBinder extends UiBinder<Widget, InvoiceViewImpl> {
	}

	@UiField FlowPanel docControls;

	@UiField(provided=true) SelectPayment payment;
	@UiField CheckBox makePaymentAsDefault;
	@UiField(provided=true) ItemInsertionForm itemInsertionForm;
	@UiField Label clientName;
	@UiField(provided=true) ValidatedDateBox date;
	@UiField ListBox documentIDClassListBox;
	@UiField Label invoiceNumber;
	@UiField(provided=true) ValidatedTextBox number;
	@UiField ValidatedTextArea paymentNote;
	@UiField ValidatedTextArea note;

	@UiField CheckBox setToAddress;
	@UiField HorizontalPanel toAddressContainer;
	@UiField(provided=true) RichTextArea toAddrCompanyName;
	@UiField(provided=true) RichTextBox toAddrStreetName;
	@UiField(provided=true) RichTextBox toAddrPostCode;
	@UiField(provided=true) RichTextBox toAddrCity;
	@UiField(provided=true) RichTextBox toAddrProvince;
	@UiField(provided=true) ValidatedListBox toAddrCountry;
	@UiField ListBox toAddrButtonDefault;

	@UiField Label totalBeforeTaxes;
	@UiField Label totalTax;
	@UiField Label totalAfterTaxes;

	@UiField Label invoiceNumberSuffix;

	@UiField(provided=true) LoaderButton createInvoice;
	@UiField Button abort;

	@UiField SimplePanel tipPayment;

	private Presenter presenter;

	public InvoiceViewImpl(boolean readonly) {
		payment = new SelectPayment(new SelectPayment.Handler() {

			@Override
			public void onPaymentSelected(PaymentTypeDTO payment) {
				presenter.onPaymentSelected(payment);
			}

			@Override
			public void onPaymentClear() {
				presenter.onPaymentClear();
			}
		});

		number = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NUMBER);

		itemInsertionForm = new ItemInsertionForm(false, readonly, new ItemInsertionForm.Handler() {

			@Override
			public void onItemListUpdated(List<AccountingDocumentItemDTO> items) {
				CalcUtils.calculateTotals(itemInsertionForm.getItems(), totalTax, totalBeforeTaxes, totalAfterTaxes);
			}

		});

		date = new ValidatedDateBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NOT_EMPTY_DATE);
		date.setFormat(new DateBox.DefaultFormat
				(DocumentUtils.DOCUMENT_DATE_FORMAT));
		createInvoice = new LoaderButton(ImageResources.INSTANCE.loader(), GlobalBundle.INSTANCE.loaderButton());

		toAddrCity = new RichTextBox(GlobalBundle.INSTANCE.richTextBoxCss(), I18N.INSTANCE.city(),ValidationKit.DEFAULT);
		toAddrCity.addStyleName(CSS.box());
		toAddrCompanyName = new RichTextArea(GlobalBundle.INSTANCE.richTextAreaCss(), I18N.INSTANCE.companyName(), new TextLengthValidation(255) {
			@Override
			public String getErrorMessage() {
				return I18NM.get.textLengthError(getMaxLength());
			}
		}, ValidationKit.DEFAULT);
		toAddrCompanyName.addStyleName(CSS.box());
		toAddrCompanyName.addStyleName(CSS.companyName());
		toAddrPostCode = new RichTextBox(GlobalBundle.INSTANCE.richTextBoxCss(), I18N.INSTANCE.postcode(),ValidationKit.DEFAULT);
		toAddrPostCode.addStyleName(CSS.box());
		toAddrStreetName = new RichTextBox(GlobalBundle.INSTANCE.richTextBoxCss(), I18N.INSTANCE.address(),ValidationKit.DEFAULT);
		toAddrStreetName.addStyleName(CSS.box());
		toAddrProvince = new RichTextBox(GlobalBundle.INSTANCE.richTextBoxCss(), I18N.INSTANCE.province(),ValidationKit.DEFAULT);
		toAddrProvince.addStyleName(CSS.box());
		toAddrCountry = LocaleWidgets.createCountryListBox(I18N.INSTANCE.country());

		initWidget(uiBinder.createAndBindUi(this));
		setStyleName(CSS.accountDocumentView());

		createInvoice.getButton().setStyleName(CSS.createButton()+" btn green");

		TipFactory.show(Tips.center_invoice_payment, tipPayment);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		presenter.onLoad();
	}

	@UiFactory
	I18N getI18N(){
		return I18N.INSTANCE;
	}

	@UiFactory
	AccountDocumentCss getAccountDocumentCss(){
		return CSS;
	}

	@UiFactory
	GlobalCss getGlobalCss(){
		return GlobalBundle.INSTANCE.globalCss();
	}

	@Override
	public ValidatedTextBox getNumber() {
		return number;
	}

	@UiHandler("documentIDClassListBox")
	void onDocIdClassChange(ChangeEvent	e){
		presenter.onDocumentIdClassChange();
	}

	@UiHandler("date")
	void onDateChanged(ValueChangeEvent<Date> e){
		presenter.onDateChanged(e.getValue());
	}

	@UiHandler("createInvoice")
	void onCreateInvoiceClicked(ClickEvent e){
		presenter.onCreateDocumentClicked();
	}

	@UiHandler("abort")
	void onCancelClicked(ClickEvent e){
		presenter.onCancelClicked();
	}

	@UiHandler("setToAddress")
	void onSetToAddress(ValueChangeEvent<Boolean> e){
		toAddressContainer.setVisible(e.getValue());
	}

	@UiHandler("toAddrButtonDefault")
	void onToAddressButtonDefaultChange(ChangeEvent e){
		presenter.onToAddressButtonDefaultChange();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public CheckBox getSetToAddress() {
		return setToAddress;
	}

	@Override
	public RichTextArea getToAddrCompanyName() {
		return toAddrCompanyName;
	}

	@Override
	public RichTextBox getToAddrStreetName() {
		return toAddrStreetName;
	}

	@Override
	public RichTextBox getToAddrPostCode() {
		return toAddrPostCode;
	}

	@Override
	public RichTextBox getToAddrCity() {
		return toAddrCity;
	}

	@Override
	public RichTextBox getToAddrProvince() {
		return toAddrProvince;
	}

	@Override
	public ValidatedListBox getToAddrCountry() {
		return toAddrCountry;
	}

	@Override
	public ListBox getToAddrButtonDefault() {
		return toAddrButtonDefault;
	}

	@Override
	public HorizontalPanel getToAddressContainer() {
		return toAddressContainer;
	}

	@Override
	public ItemInsertionForm getItemInsertionForm() {
		return itemInsertionForm;
	}

	@Override
	public Label getClientName() {
		return clientName;
	}

	@Override
	public ValidatedDateBox getDate() {
		return date;
	}

	@Override
	public Label getInvoiceNumber() {
		return invoiceNumber;
	}

	@Override
	public ListBox getDocumentIDClassListBox() {
		return documentIDClassListBox;
	}

	@Override
	public ValidatedTextArea getPaymentNote() {
		return paymentNote;
	}

	@Override
	public ValidatedTextArea getNote() {
		return note;
	}

	@Override
	public Label getTotalBeforeTaxes() {
		return totalBeforeTaxes;
	}

	@Override
	public Label getTotalTax() {
		return totalTax;
	}

	@Override
	public Label getTotalAfterTaxes() {
		return totalAfterTaxes;
	}

	@Override
	public Label getInvoiceNumberSuffix() {
		return invoiceNumberSuffix;
	}

	@Override
	public LoaderButton getCreateDocument() {
		return createInvoice;
	}

	@Override
	public Button getAbort() {
		return abort;
	}

	@Override
	public SelectPayment getPayment() {
		return payment;
	}

	@Override
	public CheckBox getMakePaymentAsDefault() {
		return makePaymentAsDefault;
	}

	@Override
	public void reset() {
		//reset widget statuses
		number.reset();

		//reset widget contents		
		date.reset();
		paymentNote.setText("");
		note.setText("");
		totalTax.setText("");
		totalBeforeTaxes.setText("");
		totalAfterTaxes.setText("");
		itemInsertionForm.reset();

		setToAddress.setValue(false);
		toAddressContainer.setVisible(false);
		toAddrCity.reset();
		toAddrCompanyName.reset();
		toAddrPostCode.reset();
		toAddrStreetName.reset();
		toAddrProvince.setEnabled(true);
		toAddrProvince.reset();
		toAddrCountry.reset();

		createInvoice.reset();

		payment.reset();

		makePaymentAsDefault.setValue(false);
		makePaymentAsDefault.setVisible(false);
		setLocked(false);
	}

	@Override
	public void setLocked(boolean value) {
		payment.setEnabled(!value);
		itemInsertionForm.setLocked(value);
		date.setEnabled(!value);
		number.setEnabled(!value);
		paymentNote.setEnabled(!value);
		note.setEnabled(!value);
		makePaymentAsDefault.setEnabled(!value);
		abort.setEnabled(!value);

		toAddrCompanyName.setEnabled(!value);
		toAddrStreetName.setEnabled(!value);
		toAddrPostCode.setEnabled(!value);
		toAddrCity.setEnabled(!value);
		toAddrProvince.setEnabled(!value);
		toAddrCountry.setEnabled(!value);
		toAddrButtonDefault.setEnabled(!value);
	}

}
