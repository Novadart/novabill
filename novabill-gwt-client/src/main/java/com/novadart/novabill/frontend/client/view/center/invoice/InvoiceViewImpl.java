package com.novadart.novabill.frontend.client.view.center.invoice;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.novadart.gwtshared.client.LoaderButton;
import com.novadart.gwtshared.client.validation.widget.ValidatedDateBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.resources.GlobalCss;
import com.novadart.novabill.frontend.client.resources.ImageResources;
import com.novadart.novabill.frontend.client.util.CalcUtils;
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.view.center.AccountDocument;
import com.novadart.novabill.frontend.client.view.center.AccountDocumentCss;
import com.novadart.novabill.frontend.client.view.center.ItemInsertionForm;
import com.novadart.novabill.frontend.client.widget.ValidatedTextArea;
import com.novadart.novabill.frontend.client.widget.payment.SelectPayment;
import com.novadart.novabill.frontend.client.widget.tip.TipFactory;
import com.novadart.novabill.frontend.client.widget.tip.Tips;
import com.novadart.novabill.frontend.client.widget.validation.ValidationKit;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;

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
	@UiField Label invoiceNumber;
	@UiField(provided=true) ValidatedTextBox number;
	@UiField ValidatedTextArea paymentNote;
	@UiField ValidatedTextArea note;

	@UiField Label totalBeforeTaxes;
	@UiField Label totalTax;
	@UiField Label totalAfterTaxes;
	
	@UiField Label invoiceNumberSuffix;

	@UiField(provided=true) LoaderButton createInvoice;
	@UiField Button abort;
	
	@UiField SimplePanel tipPayment;

	private Presenter presenter;
	
	public InvoiceViewImpl() {
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
		
		itemInsertionForm = new ItemInsertionForm(new ItemInsertionForm.Handler() {
			
			@Override
			public void onItemListUpdated(List<AccountingDocumentItemDTO> items) {
				CalcUtils.calculateTotals(itemInsertionForm.getItems(), totalTax, totalBeforeTaxes, totalAfterTaxes);
			}

		});
		
		date = new ValidatedDateBox(GlobalBundle.INSTANCE.validatedWidget(), ValidationKit.NOT_EMPTY_DATE);
		date.setFormat(new DateBox.DefaultFormat
				(DocumentUtils.DOCUMENT_DATE_FORMAT));
		createInvoice = new LoaderButton(ImageResources.INSTANCE.loader(), GlobalBundle.INSTANCE.loaderButton());
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

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
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
		
		createInvoice.reset();
		
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
	}
	
}
