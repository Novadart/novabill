package com.novadart.novabill.frontend.client.view.center.creditnote;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
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
import com.novadart.novabill.frontend.client.view.center.AccountDocument;
import com.novadart.novabill.frontend.client.view.center.AccountDocumentCss;
import com.novadart.novabill.frontend.client.view.center.ItemInsertionForm;
import com.novadart.novabill.frontend.client.view.util.LocaleWidgets;
import com.novadart.novabill.frontend.client.widget.ValidatedTextArea;
import com.novadart.novabill.frontend.client.widget.validation.ValidationKit;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;

import java.util.List;

public class CreditNoteViewImpl extends AccountDocument implements CreditNoteView {

	private static CreditNoteViewImplUiBinder uiBinder = GWT
			.create(CreditNoteViewImplUiBinder.class);

	interface CreditNoteViewImplUiBinder extends UiBinder<Widget, CreditNoteViewImpl> {
	}

	@UiField FlowPanel docControls;

	@UiField(provided=true) ItemInsertionForm itemInsertionForm;

	@UiField Label clientName;
	@UiField(provided=true) ValidatedDateBox date;
	@UiField Label creditNoteNumber;
	@UiField(provided=true) ValidatedTextBox number;
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

	@UiField Button abort;
	@UiField(provided=true) LoaderButton createCreditNote;

	private Presenter presenter;

	public CreditNoteViewImpl() {
		number = new ValidatedTextBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NUMBER);

		itemInsertionForm = new ItemInsertionForm(new ItemInsertionForm.Handler() {

			@Override
			public void onItemListUpdated(List<AccountingDocumentItemDTO> items) {
				CalcUtils.calculateTotals(itemInsertionForm.getItems(), totalTax, totalBeforeTaxes, totalAfterTaxes);
			}

		});
		date = new ValidatedDateBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NOT_EMPTY_DATE);
		date.setFormat(new DateBox.DefaultFormat
				(DateTimeFormat.getFormat("dd MMMM yyyy")));
		createCreditNote = new LoaderButton(ImageResources.INSTANCE.loader(), GlobalBundle.INSTANCE.loaderButton());

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

		createCreditNote.getButton().setStyleName(CSS.createButton()+" btn green");
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
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;

	}

	@UiFactory
	I18N getI18N(){
		return I18N.INSTANCE;
	}

	@UiFactory
	AccountDocumentCss getAccountDocumentCss(){
		return CSS;
	}

	@Override
	public ValidatedTextBox getNumber() {
		return number;
	}

	@UiHandler("createCreditNote")
	void onCreateCreditNoteClicked(ClickEvent e){
		presenter.onCreateDocumentClicked();
	}

	@UiHandler("abort")
	void onCancelClicked(ClickEvent e){
		presenter.onCancelClicked();
	}

	@Override
	public void reset() {
		//reset widget statuses
		number.reset();
		date.reset();

		//reset widget contents		
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

		createCreditNote.reset();
		setLocked(false);
	}

	@Override
	public void setLocked(boolean value) {
		itemInsertionForm.setLocked(value);

		date.setEnabled(!value);
		number.setEnabled(!value);
		note.setEnabled(!value);

		toAddrCompanyName.setEnabled(!value);
		toAddrStreetName.setEnabled(!value);
		toAddrPostCode.setEnabled(!value);
		toAddrCity.setEnabled(!value);
		toAddrProvince.setEnabled(!value);
		toAddrCountry.setEnabled(!value);
		toAddrButtonDefault.setEnabled(!value);

		abort.setEnabled(!value);
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
	public LoaderButton getCreateDocument() {
		return createCreditNote;
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

}
