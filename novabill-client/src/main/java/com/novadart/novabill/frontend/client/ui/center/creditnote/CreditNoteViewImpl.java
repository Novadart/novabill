package com.novadart.novabill.frontend.client.ui.center.creditnote;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;
import com.novadart.gwtshared.client.validation.widget.ValidatedListBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.datawatcher.DataWatcher;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.i18n.I18NM;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.place.ClientPlace.DOCUMENTS;
import com.novadart.novabill.frontend.client.ui.center.CreditNoteView;
import com.novadart.novabill.frontend.client.ui.widget.notification.Notification;
import com.novadart.novabill.frontend.client.ui.widget.table.ItemTable;
import com.novadart.novabill.frontend.client.ui.widget.validation.NumberValidation;
import com.novadart.novabill.frontend.client.util.CalcUtils;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.InvoiceItemDTO;
import com.novadart.novabill.shared.client.dto.PaymentType;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.InvoiceErrorObject;

public class CreditNoteViewImpl extends Composite implements CreditNoteView {

	private static CreditNoteViewImplUiBinder uiBinder = GWT
			.create(CreditNoteViewImplUiBinder.class);

	interface CreditNoteViewImplUiBinder extends UiBinder<Widget, CreditNoteViewImpl> {
	}

	@UiField Label paymentLabel;
	@UiField(provided=true) ValidatedListBox payment;
	@UiField(provided=true) ListBox tax;
	@UiField(provided=true) ItemTable itemTable;
	@UiField ScrollPanel itemTableScroller;

	@UiField TextBox item;
	@UiField TextBox quantity;
	@UiField TextBox unitOfMeasure;
	@UiField TextBox price;
	@UiField Label clientName;
	@UiField(provided=true) DateBox date;
	@UiField Label creditNoteNumber;
	@UiField(provided=true) ValidatedTextBox number;
	@UiField Label paymentNoteLabel;
	@UiField TextArea paymentNote;
	@UiField TextArea note;
	@UiField Button createCreditNote;
	@UiField Button modifyDocument;

	@UiField Label totalBeforeTaxes;
	@UiField Label totalTax;
	@UiField Label totalAfterTaxes;


	private Presenter presenter;
	private CreditNoteDTO creditNote;
	private ListDataProvider<InvoiceItemDTO> creditNoteItems = new ListDataProvider<InvoiceItemDTO>();
	private ClientDTO client;

	public CreditNoteViewImpl() {
		payment = new ValidatedListBox(I18N.INSTANCE.notEmptyValidationError());
		for (String item : I18N.INSTANCE.paymentItems()) {
			payment.addItem(item);
		}
		tax = new ListBox();
		for (String item : I18N.INSTANCE.vatItems()) {
			tax.addItem(item+"%", item);
		}
		number = new ValidatedTextBox(new NumberValidation());
		itemTable = new ItemTable(new ItemTable.Handler() {

			@Override
			public void delete(InvoiceItemDTO item) {
				creditNoteItems.getList().remove(item);
				creditNoteItems.refresh();
				updateFields();
			}
		});
		creditNoteItems.addDataDisplay(itemTable);

		date = new DateBox();
		date.setFormat(new DateBox.DefaultFormat
				(DateTimeFormat.getFormat("dd MMMM yyyy")));
		initWidget(uiBinder.createAndBindUi(this));
		setStyleName("InvoiceView");
	}

	@UiFactory
	I18N getI18N(){
		return I18N.INSTANCE;
	}

	@UiHandler("createCreditNote")
	void onCreateCreditNoteClicked(ClickEvent e){
		if(!validateCreditNote()){
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}

		CreditNoteDTO creditNote = createCreditNote(null);
		

		ServerFacade.creditNote.add(creditNote, new WrappedAsyncCallback<Long>() {

			@Override
			public void onSuccess(Long result) {
				Notification.showMessage(I18N.INSTANCE.creditNoteCreationSuccess());

				DataWatcher.getInstance().fireCreditNoteEvent();

				ClientPlace cp = new ClientPlace();
				cp.setClientId(client.getId());
				cp.setDocumentsListing(DOCUMENTS.creditNotes);
				presenter.goTo(cp);
			}

			@Override
			public void onException(Throwable caught) {
				if(caught instanceof ValidationException){
					handleServerValidationException((ValidationException) caught, true);
				} else {
					Notification.showMessage(I18N.INSTANCE.creditNoteCreationFailure());
				}
			}
		});
			
	}


	private CreditNoteDTO createCreditNote(CreditNoteDTO creditNote){
		CreditNoteDTO cn;

		if(creditNote != null){
			cn = creditNote;
		} else {
			cn = new CreditNoteDTO();
			cn.setBusiness(Configuration.getBusiness());
			cn.setClient(client);
		}


		cn.setDocumentID(Long.parseLong(number.getText()));
		cn.setAccountingDocumentDate(date.getValue());
		List<InvoiceItemDTO> invItems = new ArrayList<InvoiceItemDTO>();
		for (InvoiceItemDTO invoiceItemDTO : creditNoteItems.getList()) {
			invItems.add(invoiceItemDTO);
		}
		cn.setItems(invItems);
		cn.setNote(note.getText());
		cn.setPaymentType(PaymentType.values()[payment.getSelectedIndex()-1]);
		if(payment.getSelectedIndex() > 0){
			cn.setPaymentDueDate(CalcUtils.calculatePaymentDueDate(cn.getAccountingDocumentDate(), cn.getPaymentType()));  
		} else {
			cn.setPaymentDueDate(null);
		}

		cn.setPaymentNote(paymentNote.getText());
		CalcUtils.calculateTotals(invItems, cn);
		return cn;
	}


	@UiHandler("add")
	void onAddClicked(ClickEvent e){
		InvoiceItemDTO ii = CalcUtils.createInvoiceItem(item.getText(), price.getText(), 
				quantity.getText(), unitOfMeasure.getText(), tax.getValue(tax.getSelectedIndex()));
		
		if(ii == null) {
			return;
		}
		
		creditNoteItems.getList().add(ii);
		updateFields();
		itemTableScroller.scrollToBottom();
	}

	@UiHandler("modifyDocument")
	void onModifyCreditNoteClicked(ClickEvent e){

		if(!validateCreditNote()){
			return;
		}

		if(Notification.showYesNoRequest(I18N.INSTANCE.saveModificationsConfirm()) ){
			final CreditNoteDTO cn = createCreditNote(creditNote);

			ServerFacade.creditNote.update(cn, new WrappedAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					Notification.showMessage(I18N.INSTANCE.creditNoteUpdateSuccess());

					DataWatcher.getInstance().fireCreditNoteEvent();
					DataWatcher.getInstance().fireStatsEvent();

					ClientPlace cp = new ClientPlace();
					cp.setClientId(cn.getClient().getId());
					cp.setDocumentsListing(DOCUMENTS.creditNotes);
					presenter.goTo(cp);
				}

				@Override
				public void onException(Throwable caught) {
					if(caught instanceof ValidationException){
						handleServerValidationException((ValidationException) caught, true);
					} else {
						Notification.showMessage(I18N.INSTANCE.creditNoteUpdateFailure());
					}
				}
			});

		} 
	}

	@UiHandler("abort")
	void onCancelClicked(ClickEvent e){
		if(Notification.showYesNoRequest(I18N.INSTANCE.cancelModificationsConfirmation()) ){
			ClientPlace cp = new ClientPlace();
			cp.setClientId(client.getId());
			presenter.goTo(cp);
		}
	}
	
	
	public void setCreditNote(CreditNoteDTO creditNote) {
		this.creditNote = creditNote;
		this.client = creditNote.getClient();
		date.setValue(creditNote.getAccountingDocumentDate());
		clientName.setText(creditNote.getClient().getName());
		modifyDocument.setVisible(true);

		List<InvoiceItemDTO> items = null;
		items = creditNote.getItems();

		creditNoteItems.setList(items);
		number.setText(creditNote.getDocumentID().toString());
		note.setText(creditNote.getNote());
		paymentNote.setText(creditNote.getPaymentNote());
		payment.setSelectedIndex(creditNote.getPaymentType().ordinal()+1);

		updateFields();
	};
	
	@Override
	public void setDataForNewCreditNote(ClientDTO client, Long progressiveId) {
		this.client = client;

		clientName.setText(client.getName());
		date.setValue(new Date());
		number.setText(progressiveId.toString());

		createCreditNote.setVisible(true);
	}
	
	
	@Override
	public void setDataForNewCreditNote(Long progressiveId, InvoiceDTO invoice) {
		setDataForNewCreditNote(invoice.getClient(), progressiveId);
		
		List<InvoiceItemDTO> items = null;
		items = new ArrayList<InvoiceItemDTO>(invoice.getItems().size());
		for (InvoiceItemDTO i : invoice.getItems()) {
			items.add(i.clone());
		}
		creditNoteItems.setList(items);
		updateFields();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	private boolean validateCreditNote(){
		if(date.getTextBox().getText().isEmpty() || date.getValue() == null){
			return false;
		} else if(creditNoteItems.getList().isEmpty()){
			return false;
		}
		number.validate();
		payment.validate();
		if(!number.isValid() || !payment.isValid()){
			return false;
		}

		return true;
	}


	private void updateFields(){
		CalcUtils.calculateTotals(creditNoteItems.getList(), totalTax, totalBeforeTaxes, totalAfterTaxes);
		resetItemTableForm();
		creditNoteItems.refresh();
	}

	private void resetItemTableForm(){
		item.setText("");
		quantity.setText("");
		unitOfMeasure.setText("");
		price.setText("");
		tax.setSelectedIndex(0);
	}

	@Override
	public void setClean() {
		//clean internal data		
		this.creditNote = null;
		this.client = null;

		//reset widget statuses
		number.reset();
		number.setVisible(true);
		payment.setVisible(true);
		payment.reset();
		createCreditNote.setVisible(false);
		modifyDocument.setVisible(false);
		paymentNote.setVisible(true);
		creditNoteNumber.setVisible(true);
		paymentNoteLabel.setVisible(true);
		paymentLabel.setVisible(true);

		//reset widget contents		
		payment.setSelectedIndex(0);
		paymentNote.setText("");
		note.setText("");
		creditNoteItems.getList().clear();
		creditNoteItems.refresh();
		totalTax.setText("");
		totalBeforeTaxes.setText("");
		totalAfterTaxes.setText("");
		resetItemTableForm();
	}

	private void handleServerValidationException(ValidationException ex, boolean isInvoice){
		for (ErrorObject eo : ex.getErrors()) {
			switch(eo.getErrorCode()){
			case INVALID_DOCUMENT_ID:
				if(isInvoice){
					StringBuilder sb = new StringBuilder();
					List<Long> gaps = ((InvoiceErrorObject) eo).getGaps();

					if(gaps.size() > 1) {
						for (int i=0; i<gaps.size()-1; i++) {
							sb.append(gaps.get(i) +", ");
						}
						sb.append(gaps.get(gaps.size()-1));
					} else {
						sb.append(gaps.get(0));
					}

					number.showErrorMessage(I18NM.get.invalidDocumentIdError(sb.toString()));
				}
				break;

			default:
				break;
			}
		}
	}

}
