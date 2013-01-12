package com.novadart.novabill.frontend.client.view.center.creditnote;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
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
import com.novadart.gwtshared.client.validation.widget.ValidatedListBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.event.DocumentAddEvent;
import com.novadart.novabill.frontend.client.event.DocumentUpdateEvent;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.place.ClientPlace.DOCUMENTS;
import com.novadart.novabill.frontend.client.util.CalcUtils;
import com.novadart.novabill.frontend.client.view.center.AccountDocument;
import com.novadart.novabill.frontend.client.view.center.CreditNoteView;
import com.novadart.novabill.frontend.client.view.center.ItemInsertionForm;
import com.novadart.novabill.frontend.client.view.widget.ValidatedTextArea;
import com.novadart.novabill.frontend.client.view.widget.notification.Notification;
import com.novadart.novabill.frontend.client.view.widget.notification.NotificationCallback;
import com.novadart.novabill.frontend.client.view.widget.validation.ValidationKit;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PaymentType;
import com.novadart.novabill.shared.client.exception.ValidationException;

public class CreditNoteViewImpl extends AccountDocument implements CreditNoteView {

	private static CreditNoteViewImplUiBinder uiBinder = GWT
			.create(CreditNoteViewImplUiBinder.class);

	interface CreditNoteViewImplUiBinder extends UiBinder<Widget, CreditNoteViewImpl> {
	}

	@UiField Label titleLabel;
	
	@UiField FlowPanel docControls;
	@UiField ScrollPanel docScroll;
	
	@UiField Label paymentLabel;
	@UiField(provided=true) ValidatedListBox payment;
	@UiField(provided=true) ItemInsertionForm itemInsertionForm;

	@UiField Label clientName;
	@UiField(provided=true) DateBox date;
	@UiField Label creditNoteNumber;
	@UiField(provided=true) ValidatedTextBox number;
	@UiField Label paymentNoteLabel;
	@UiField ValidatedTextArea paymentNote;
	@UiField ValidatedTextArea note;
	@UiField Button createCreditNote;
	@UiField Button modifyDocument;

	@UiField Label totalBeforeTaxes;
	@UiField Label totalTax;
	@UiField Label totalAfterTaxes;


	private Presenter presenter;
	private CreditNoteDTO creditNote;
	private ClientDTO client;
	private EventBus eventBus;

	public CreditNoteViewImpl() {
		payment = new ValidatedListBox(I18N.INSTANCE.notEmptyValidationError());
		for (String item : I18N.INSTANCE.paymentItems()) {
			payment.addItem(item);
		}
		number = new ValidatedTextBox(ValidationKit.NUMBER);

		itemInsertionForm = new ItemInsertionForm(new ItemInsertionForm.Handler() {
			
			@Override
			public void onItemListUpdated(List<AccountingDocumentItemDTO> items) {
				CalcUtils.calculateTotals(itemInsertionForm.getItems(), totalTax, totalBeforeTaxes, totalAfterTaxes);
			}
			
		});
		date = new DateBox();
		date.setFormat(new DateBox.DefaultFormat
				(DateTimeFormat.getFormat("dd MMMM yyyy")));
		initWidget(uiBinder.createAndBindUi(this));
		setStyleName("AccountDocumentView");
	}

	@Override
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;	
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
	protected ScrollPanel getDocScroll() {
		return docScroll;
	}
	
	@Override
	protected ValidatedTextBox getNumber() {
		return number;
	}

	@UiHandler("createCreditNote")
	void onCreateCreditNoteClicked(ClickEvent e){
		if(!validateCreditNote()){
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}

		final CreditNoteDTO creditNote = createCreditNote(null);
		

		ServerFacade.creditNote.add(creditNote, new WrappedAsyncCallback<Long>() {

			@Override
			public void onSuccess(Long result) {
				Notification.showMessage(I18N.INSTANCE.creditNoteCreationSuccess(), new NotificationCallback<Void>() {
					
					@Override
					public void onNotificationClosed(Void value) {
						eventBus.fireEvent(new DocumentAddEvent(creditNote));

						ClientPlace cp = new ClientPlace();
						cp.setClientId(client.getId());
						cp.setDocs(DOCUMENTS.creditNotes);
						presenter.goTo(cp);
					}
				});
			}

			@Override
			public void onException(Throwable caught) {
				if(caught instanceof ValidationException){
					handleServerValidationException((ValidationException) caught);
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
		List<AccountingDocumentItemDTO> invItems = new ArrayList<AccountingDocumentItemDTO>();
		for (AccountingDocumentItemDTO itemDTO : itemInsertionForm.getItems()) {
			invItems.add(itemDTO);
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


	@UiHandler("modifyDocument")
	void onModifyCreditNoteClicked(ClickEvent e){

		if(!validateCreditNote()){
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}
		
		Notification.showConfirm(I18N.INSTANCE.saveModificationsConfirm(), new NotificationCallback<Boolean>() {
			
			@Override
			public void onNotificationClosed(Boolean value) {
				if(value){
					final CreditNoteDTO cn = createCreditNote(creditNote);
	
					ServerFacade.creditNote.update(cn, new WrappedAsyncCallback<Void>() {
	
						@Override
						public void onSuccess(Void result) {
							Notification.showMessage(I18N.INSTANCE.creditNoteUpdateSuccess(), new NotificationCallback<Void>() {
								
								@Override
								public void onNotificationClosed(Void value) {
									eventBus.fireEvent(new DocumentUpdateEvent(creditNote));
									
									ClientPlace cp = new ClientPlace();
									cp.setClientId(cn.getClient().getId());
									cp.setDocs(DOCUMENTS.creditNotes);
									presenter.goTo(cp);
								}
							});
							
						}
	
						@Override
						public void onException(Throwable caught) {
							if(caught instanceof ValidationException){
								handleServerValidationException((ValidationException) caught);
							} else {
								Notification.showMessage(I18N.INSTANCE.creditNoteUpdateFailure());
							}
						}
					});
				}
			}
		});

	}

	@UiHandler("abort")
	void onCancelClicked(ClickEvent e){
		Notification.showConfirm(I18N.INSTANCE.cancelModificationsConfirmation(), new NotificationCallback<Boolean>() {
			
			@Override
			public void onNotificationClosed(Boolean value) {
				if(value){
					ClientPlace cp = new ClientPlace();
					cp.setClientId(client.getId());
					cp.setDocs(DOCUMENTS.creditNotes);
					presenter.goTo(cp);
				}
			}
		});
		
	}
	
	
	public void setCreditNote(CreditNoteDTO creditNote) {
		this.creditNote = creditNote;
		this.client = creditNote.getClient();
		date.setValue(creditNote.getAccountingDocumentDate());
		clientName.setText(creditNote.getClient().getName());
		modifyDocument.setVisible(true);

		List<AccountingDocumentItemDTO> items = null;
		items = creditNote.getItems();

		itemInsertionForm.setItems(items);
		number.setText(creditNote.getDocumentID().toString());
		note.setText(creditNote.getNote());
		paymentNote.setText(creditNote.getPaymentNote());
		payment.setSelectedIndex(creditNote.getPaymentType().ordinal()+1);
		titleLabel.setText(I18N.INSTANCE.modifyCreditNote());
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
		
		List<AccountingDocumentItemDTO> items = null;
		items = new ArrayList<AccountingDocumentItemDTO>(invoice.getItems().size());
		for (AccountingDocumentItemDTO i : invoice.getItems()) {
			items.add(i.clone());
		}
		itemInsertionForm.setItems(items);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	private boolean validateCreditNote(){
		if(date.getTextBox().getText().isEmpty() || date.getValue() == null){
			return false;
		} else if(itemInsertionForm.getItems().isEmpty()){
			return false;
		}
		number.validate();
		payment.validate();
		if(!number.isValid() || !payment.isValid()){
			return false;
		}

		return true;
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
		totalTax.setText("");
		totalBeforeTaxes.setText("");
		totalAfterTaxes.setText("");
		itemInsertionForm.reset();
		
		titleLabel.setText(I18N.INSTANCE.newCreditNoteCreation());
	}

}
