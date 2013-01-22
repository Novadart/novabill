package com.novadart.novabill.frontend.client.view.center.invoice;

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
import com.novadart.gwtshared.client.LoaderButton;
import com.novadart.gwtshared.client.validation.widget.ValidatedListBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.event.DocumentAddEvent;
import com.novadart.novabill.frontend.client.event.DocumentUpdateEvent;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.i18n.I18NM;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.place.ClientPlace.DOCUMENTS;
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.view.HasUILocking;
import com.novadart.novabill.frontend.client.view.center.AccountDocument;
import com.novadart.novabill.frontend.client.view.center.InvoiceView;
import com.novadart.novabill.frontend.client.view.center.ItemInsertionForm;
import com.novadart.novabill.frontend.client.widget.ValidatedTextArea;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.frontend.client.widget.validation.ValidationKit;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PaymentType;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.ValidationException;

public class InvoiceViewImpl extends AccountDocument implements InvoiceView, HasUILocking {

	private static InvoiceViewImplUiBinder uiBinder = GWT
			.create(InvoiceViewImplUiBinder.class);

	interface InvoiceViewImplUiBinder extends UiBinder<Widget, InvoiceViewImpl> {
	}
	
	@UiField FlowPanel docControls;
	@UiField ScrollPanel docScroll;

	@UiField Label titleLabel;
	@UiField Label paymentLabel;
	@UiField(provided=true) ValidatedListBox payment;
	@UiField(provided=true) ItemInsertionForm itemInsertionForm;
	@UiField Label clientName;
	@UiField(provided=true) DateBox date;
	@UiField Label invoiceNumber;
	@UiField(provided=true) ValidatedTextBox number;
	@UiField Label paymentNoteLabel;
	@UiField ValidatedTextArea paymentNote;
	@UiField ValidatedTextArea note;

	@UiField Label totalBeforeTaxes;
	@UiField Label totalTax;
	@UiField Label totalAfterTaxes;

	@UiField LoaderButton modifyDocument;
	@UiField LoaderButton createInvoice;
	@UiField Button abort;

	private Presenter presenter;
	private EventBus eventBus;
	private InvoiceDTO invoice;
	private ClientDTO client;

	public InvoiceViewImpl() {
		payment = new ValidatedListBox(I18N.INSTANCE.notEmptyValidationError());
		for (String item : I18N.INSTANCE.paymentItems()) {
			payment.addItem(item);
		}
		
		number = new ValidatedTextBox(ValidationKit.NUMBER);
		
		itemInsertionForm = new ItemInsertionForm(new ItemInsertionForm.Handler() {
			
			@Override
			public void onItemListUpdated(List<AccountingDocumentItemDTO> items) {
				DocumentUtils.calculateTotals(itemInsertionForm.getItems(), totalTax, totalBeforeTaxes, totalAfterTaxes);
			}

		});
		
		date = new DateBox();
		date.setFormat(new DateBox.DefaultFormat
				(DateTimeFormat.getFormat("dd MMMM yyyy")));
		initWidget(uiBinder.createAndBindUi(this));
		setStyleName("AccountDocumentView");
		
		modifyDocument.getButton().setStyleName("modifyButton button");
		createInvoice.getButton().setStyleName("createButton button");
	}
	
	@Override
	protected Element getBody() {
		return docScroll.getElement();
	}
	
	@Override
	protected Element[] getNonBodyElements() {
		return new Element[]{titleLabel.getElement(), docControls.getElement()};
	}
	
	@Override
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
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

	@UiHandler("createInvoice")
	void onCreateInvoiceClicked(ClickEvent e){
		if(!validateInvoice()){
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}
		
		createInvoice.showLoader(true);
		setLocked(true);

		final InvoiceDTO invoice = createInvoice(null);
		
		ServerFacade.invoice.add(invoice, new ManagedAsyncCallback<Long>() {

			@Override
			public void onSuccess(Long result) {
				createInvoice.showLoader(false);
				Notification.showMessage(I18N.INSTANCE.invoiceCreationSuccess(), new NotificationCallback<Void>() {
					
					@Override
					public void onNotificationClosed(Void value) {
						eventBus.fireEvent(new DocumentAddEvent(invoice));

						ClientPlace cp = new ClientPlace();
						cp.setClientId(client.getId());
						cp.setDocs(DOCUMENTS.invoices);
						presenter.goTo(cp);
						
						setLocked(false);
					}
				});
				
			}

			@Override
			public void onFailure(Throwable caught) {
				createInvoice.showLoader(false);
				if(caught instanceof ValidationException){
					handleServerValidationException((ValidationException) caught);
				} else {
					Notification.showMessage(I18N.INSTANCE.invoiceCreationFailure());
					super.onFailure(caught);
				}
				setLocked(false);
			}
		});

	}

	private InvoiceDTO createInvoice(InvoiceDTO invoice){
		InvoiceDTO inv;

		if(invoice != null){
			inv = invoice;
		} else {
			inv = new InvoiceDTO();
			inv.setBusiness(Configuration.getBusiness());
			inv.setClient(client);
		}


		inv.setDocumentID(Long.parseLong(number.getText()));
		inv.setAccountingDocumentDate(date.getValue());
		List<AccountingDocumentItemDTO> invItems = new ArrayList<AccountingDocumentItemDTO>();
		for (AccountingDocumentItemDTO itemDTO : itemInsertionForm.getItems()) {
			invItems.add(itemDTO);
		}
		inv.setItems(invItems);
		inv.setNote(note.getText());
		inv.setPaymentType(PaymentType.values()[payment.getSelectedIndex()-1]);
		if(payment.getSelectedIndex() > 0){
			inv.setPaymentDueDate(DocumentUtils.calculatePaymentDueDate(inv.getAccountingDocumentDate(), inv.getPaymentType()));  
		} else {
			inv.setPaymentDueDate(null);
		}

		inv.setPaymentNote(paymentNote.getText());
		DocumentUtils.calculateTotals(invItems, inv);
		return inv;
	}

	@UiHandler("modifyDocument")
	void onModifyInvoiceClicked(ClickEvent e){

		if(!validateInvoice()){
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}

		Notification.showConfirm(I18N.INSTANCE.saveModificationsConfirm(), new NotificationCallback<Boolean>() {
			
			@Override
			public void onNotificationClosed(Boolean value) {
				if(value){
					
					modifyDocument.showLoader(true);
					setLocked(true);
					
					final InvoiceDTO inv = createInvoice(invoice);

					ServerFacade.invoice.update(inv, new ManagedAsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							modifyDocument.showLoader(false);
							if(caught instanceof ValidationException){
								handleServerValidationException((ValidationException) caught);
							} else {
								Notification.showMessage(I18N.INSTANCE.invoiceUpdateFailure());
								super.onFailure(caught);
							}
							setLocked(false);
						}

						@Override
						public void onSuccess(Void result) {
							modifyDocument.showLoader(false);
							Notification.showMessage(I18N.INSTANCE.invoiceUpdateSuccess(), new NotificationCallback<Void>() {
								
								@Override
								public void onNotificationClosed(Void value) {
									eventBus.fireEvent(new DocumentUpdateEvent(inv));

									ClientPlace cp = new ClientPlace();
									cp.setClientId(inv.getClient().getId());
									cp.setDocs(DOCUMENTS.invoices);
									presenter.goTo(cp);
									setLocked(false);
								}
							});
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
					cp.setDocs(DOCUMENTS.invoices);
					presenter.goTo(cp);
				}
			}
		});
	}

	private void setInvoice(InvoiceDTO invoice, boolean cloning) {
		if(!cloning) {
			this.invoice = invoice;
			this.client = invoice.getClient();
			date.setValue(invoice.getAccountingDocumentDate());
			clientName.setText(invoice.getClient().getName());
			modifyDocument.setVisible(true);
			titleLabel.setText(I18N.INSTANCE.modifyInvoice());
		}

		List<AccountingDocumentItemDTO> items = null;
		if(cloning){
			items = new ArrayList<AccountingDocumentItemDTO>(invoice.getItems().size());
			for (AccountingDocumentItemDTO i : invoice.getItems()) {
				items.add(i.clone());
			}
		} else {
			items = invoice.getItems();
		}

		
		itemInsertionForm.setItems(items);
		if(!cloning && invoice.getDocumentID() != null){
			number.setText(invoice.getDocumentID().toString());
		} 
		note.setText(invoice.getNote());
		paymentNote.setText(invoice.getPaymentNote());
		if(invoice.getPaymentType() != null) { //can be null if the invoice is derived from an estimation
			payment.setSelectedIndex(invoice.getPaymentType().ordinal()+1);
		}

	}


	@Override
	public void setInvoice(InvoiceDTO invoice) {
		setInvoice(invoice, false);
	}


	@Override
	public void setDataForNewInvoice(ClientDTO client, Long progressiveId) {
		this.client = client;

		clientName.setText(client.getName());
		date.setValue(new Date());
		number.setText(progressiveId.toString());

		createInvoice.setVisible(true);
	}

	@Override
	public void setDataForNewInvoice(ClientDTO client, Long progressiveId,
			InvoiceDTO invoice) {
		setDataForNewInvoice(client, progressiveId);
		setInvoice(invoice, true);
	}

	@Override
	public void setDataForNewInvoice(Long progressiveId, EstimationDTO estimation) {
		setDataForNewInvoice(estimation.getClient(), progressiveId);

		List<AccountingDocumentItemDTO> items = new ArrayList<AccountingDocumentItemDTO>(estimation.getItems().size());
		for (AccountingDocumentItemDTO i : estimation.getItems()) {
			items.add(i.clone());
		}

		itemInsertionForm.setItems(items);
		note.setText(I18NM.get.generatedFromEstimation(estimation.getDocumentID(), 
				DateTimeFormat.getFormat("dd MMMM yyyy").format(estimation.getAccountingDocumentDate())));
	}

	@Override
	public void setDataForNewInvoice(Long progressiveId,
			TransportDocumentDTO transportDocument) {
		setDataForNewInvoice(transportDocument.getClient(), progressiveId);

		List<AccountingDocumentItemDTO> items = new ArrayList<AccountingDocumentItemDTO>(transportDocument.getItems().size());
		for (AccountingDocumentItemDTO i : transportDocument.getItems()) {
			items.add(i.clone());
		}

		itemInsertionForm.setItems(items);
		note.setText(transportDocument.getNote());
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	private boolean validateInvoice(){
		if(date.getTextBox().getText().isEmpty() || date.getValue() == null){
			return false;
		} 
		
		if(itemInsertionForm.getItems().isEmpty()){
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
		this.invoice = null;
		this.client = null;

		//reset widget statuses
		number.reset();
		payment.reset();
		createInvoice.setVisible(false);
		modifyDocument.setVisible(false);
		paymentNote.setVisible(true);
		invoiceNumber.setVisible(true);
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
		titleLabel.setText(I18N.INSTANCE.newInvoiceCreation());
		
		createInvoice.reset();
		modifyDocument.reset();
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

		abort.setEnabled(!value);
	}

}
