package com.novadart.novabill.frontend.client.view.center.estimation;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
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
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.novadart.gwtshared.client.LoaderButton;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.view.center.AccountDocument;
import com.novadart.novabill.frontend.client.view.center.ItemInsertionForm;
import com.novadart.novabill.frontend.client.widget.ValidatedTextArea;
import com.novadart.novabill.frontend.client.widget.validation.ValidationKit;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;

public class EstimationViewImpl extends AccountDocument implements EstimationView {

	private static EstimationViewImplUiBinder uiBinder = GWT
			.create(EstimationViewImplUiBinder.class);

	interface EstimationViewImplUiBinder extends UiBinder<Widget, EstimationViewImpl> {
	}
	
	@UiField Label titleLabel;
	@UiField FlowPanel docControls;
	@UiField ScrollPanel docScroll;

	@UiField Label clientName;
	@UiField(provided=true) ValidatedTextBox number;
	@UiField(provided=true) DateBox date;
	@UiField(provided=true) DateBox validTill;
	@UiField ValidatedTextArea note;
	@UiField ValidatedTextArea paymentNote;
	@UiField ValidatedTextArea limitations;
	
	@UiField(provided=true) ItemInsertionForm itemInsertionForm;
	
	@UiField Label totalBeforeTaxes;
	@UiField Label totalTax;
	@UiField Label totalAfterTaxes;
	
	@UiField LoaderButton modifyDocument;
	@UiField LoaderButton createEstimation;
	@UiField LoaderButton convertToInvoice;
	@UiField Button abort;
	
	
	private Presenter presenter;

	public EstimationViewImpl() {
		itemInsertionForm = new ItemInsertionForm(new ItemInsertionForm.Handler() {
			
			@Override
			public void onItemListUpdated(List<AccountingDocumentItemDTO> items) {
				DocumentUtils.calculateTotals(itemInsertionForm.getItems(), totalTax, totalBeforeTaxes, totalAfterTaxes);
			}
			
		});
		
		number = new ValidatedTextBox(ValidationKit.NUMBER);

		date = new DateBox();
		date.setFormat(new DateBox.DefaultFormat
				(DateTimeFormat.getFormat("dd MMMM yyyy")));
		validTill = new DateBox();
		validTill.setFormat(new DateBox.DefaultFormat
				(DateTimeFormat.getFormat("dd MMMM yyyy")));
		initWidget(uiBinder.createAndBindUi(this));
		setStyleName("AccountDocumentView");
		
		modifyDocument.getButton().setStyleName("modifyButton button");
		createEstimation.getButton().setStyleName("createButton button");
		convertToInvoice.getButton().setStyleName("convertToInvoice button");
		
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
	I18N getI18N(){
		return I18N.INSTANCE;
	}

	@Override
	public ScrollPanel getDocScroll() {
		return docScroll;
	}
	
	@Override
	public ValidatedTextBox getNumber() {
		return number;
	}


	@UiHandler("convertToInvoice")
	void onConvertToInvoice(ClickEvent e){
		presenter.onConvertToInvoiceClicked();
	}


	@UiHandler("createEstimation")
	void onCreateEstimationClicked(ClickEvent e){
		presenter.onCreateDocumentClicked();
	}


	@UiHandler("modifyDocument")
	void onModifyEstimationClicked(ClickEvent e){
		presenter.onModifyDocumentClicked();
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
	public void clean() {
		number.reset();

		//reset widget statuses
		createEstimation.setVisible(false);
		modifyDocument.setVisible(false);
		convertToInvoice.setVisible(false);

		//reset widget contents		
		note.setText("");
		paymentNote.setText("");
		limitations.setText("");
		totalTax.setText("");
		totalBeforeTaxes.setText("");
		totalAfterTaxes.setText("");
		itemInsertionForm.reset();
		
		modifyDocument.reset();
		createEstimation.reset();
		convertToInvoice.reset();
		setLocked(false);
	}

	@Override
	public void setLocked(boolean value) {
		number.setEnabled(!value);
		date.setEnabled(!value);
		validTill.setEnabled(!value);
		note.setEnabled(!value);
		paymentNote.setEnabled(!value);
		limitations.setEnabled(!value);
		
		itemInsertionForm.setLocked(value);
		
		abort.setEnabled(!value);
	}

	@Override
	public Button getAbort() {
		return abort;
	}

	@Override
	public DateBox getDate() {
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
	public LoaderButton getCreateDocument() {
		return createEstimation;
	}
	
	@Override
	public LoaderButton getModifyDocument() {
		return modifyDocument;
	}
	
	@Override
	public ValidatedTextArea getNote() {
		return note;
	}

	@Override
	public Label getTitleLabel() {
		return titleLabel;
	}

	@Override
	public DateBox getValidTill() {
		return validTill;
	}

	@Override
	public ValidatedTextArea getPaymentNote() {
		return paymentNote;
	}

	@Override
	public ValidatedTextArea getLimitations() {
		return limitations;
	}

	@Override
	public LoaderButton getConvertToInvoice() {
		return convertToInvoice;
	}

	
}
