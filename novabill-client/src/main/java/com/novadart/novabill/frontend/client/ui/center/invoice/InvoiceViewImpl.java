package com.novadart.novabill.frontend.client.ui.center.invoice;

import java.util.ArrayList;
import java.util.Date;
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
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
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
import com.novadart.novabill.frontend.client.ui.center.AccountDocument;
import com.novadart.novabill.frontend.client.ui.center.InvoiceView;
import com.novadart.novabill.frontend.client.ui.center.ItemInsertionForm;
import com.novadart.novabill.frontend.client.ui.widget.notification.Notification;
import com.novadart.novabill.frontend.client.ui.widget.validation.NumberValidation;
import com.novadart.novabill.frontend.client.util.CalcUtils;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PaymentType;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.validation.ErrorObject;
import com.novadart.novabill.shared.client.validation.InvoiceErrorObject;

public class InvoiceViewImpl extends AccountDocument implements InvoiceView {

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
	@UiField TextArea paymentNote;
	@UiField TextArea note;
	@UiField Button createInvoice;
	@UiField Button modifyDocument;

	@UiField Label totalBeforeTaxes;
	@UiField Label totalTax;
	@UiField Label totalAfterTaxes;


	private Presenter presenter;
	private InvoiceDTO invoice;
	private EstimationDTO estimation;
	private TransportDocumentDTO transportDocument;
	private ClientDTO client;

	public InvoiceViewImpl() {
		payment = new ValidatedListBox(I18N.INSTANCE.notEmptyValidationError());
		for (String item : I18N.INSTANCE.paymentItems()) {
			payment.addItem(item);
		}
		
		number = new ValidatedTextBox(new NumberValidation());
		
		itemInsertionForm = new ItemInsertionForm(new ItemInsertionForm.Handler() {
			
			@Override
			public void onItemAdded(List<AccountingDocumentItemDTO> items) {
				CalcUtils.calculateTotals(itemInsertionForm.getItems(), totalTax, totalBeforeTaxes, totalAfterTaxes);
			}
			
			@Override
			public void onItemUpdated(List<AccountingDocumentItemDTO> items) {
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

	@UiHandler("createInvoice")
	void onCreateInvoiceClicked(ClickEvent e){
		if(!validateInvoice()){
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}

		InvoiceDTO invoice = createInvoice(null);
		
		if(this.estimation != null) {
			ServerFacade.invoice.add(invoice, new WrappedAsyncCallback<Long>() {
			
						@Override
						public void onSuccess(Long result) {
							DataWatcher.getInstance().fireInvoiceEvent();
							DataWatcher.getInstance().fireStatsEvent();
							
							ClientPlace cp = new ClientPlace();
							cp.setClientId(client.getId());
							cp.setDocumentsListing(DOCUMENTS.invoices);
							presenter.goTo(cp);
						}
			
						@Override
						public void onException(Throwable caught) {
							if(caught instanceof ValidationException){
								handleServerValidationException((ValidationException) caught, true);
							} else {
								Notification.showMessage(I18N.INSTANCE.invoiceCreationFailure());
							}
						}
					});
		} else if(this.transportDocument != null) {
			
			ServerFacade.invoice.add(invoice, new WrappedAsyncCallback<Long>() {
				
				@Override
				public void onSuccess(Long result) {
					DataWatcher.getInstance().fireInvoiceEvent();
					DataWatcher.getInstance().fireStatsEvent();
					
					ClientPlace cp = new ClientPlace();
					cp.setClientId(client.getId());
					cp.setDocumentsListing(DOCUMENTS.invoices);
					presenter.goTo(cp);
				}
	
				@Override
				public void onException(Throwable caught) {
					if(caught instanceof ValidationException){
						handleServerValidationException((ValidationException) caught, true);
					} else {
						Notification.showMessage(I18N.INSTANCE.invoiceCreationFailure());
					}
				}
			});
		} else {
		
			ServerFacade.invoice.add(invoice, new WrappedAsyncCallback<Long>() {

				@Override
				public void onSuccess(Long result) {
					Notification.showMessage(I18N.INSTANCE.invoiceCreationSuccess());

					DataWatcher.getInstance().fireInvoiceEvent();
					DataWatcher.getInstance().fireStatsEvent();

					ClientPlace cp = new ClientPlace();
					cp.setClientId(client.getId());
					cp.setDocumentsListing(DOCUMENTS.invoices);
					presenter.goTo(cp);
				}

				@Override
				public void onException(Throwable caught) {
					if(caught instanceof ValidationException){
						handleServerValidationException((ValidationException) caught, true);
					} else {
						Notification.showMessage(I18N.INSTANCE.invoiceCreationFailure());
					}
				}
			});
			
		}
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
			inv.setPaymentDueDate(CalcUtils.calculatePaymentDueDate(inv.getAccountingDocumentDate(), inv.getPaymentType()));  
		} else {
			inv.setPaymentDueDate(null);
		}

		inv.setPaymentNote(paymentNote.getText());
		CalcUtils.calculateTotals(invItems, inv);
		return inv;
	}

	@UiHandler("modifyDocument")
	void onModifyInvoiceClicked(ClickEvent e){

		if(!validateInvoice()){
			return;
		}

		if(Notification.showYesNoRequest(I18N.INSTANCE.saveModificationsConfirm()) ){
			final InvoiceDTO inv = createInvoice(invoice);

			ServerFacade.invoice.update(inv, new WrappedAsyncCallback<Void>() {

				@Override
				public void onException(Throwable caught) {
					if(caught instanceof ValidationException){
						handleServerValidationException((ValidationException) caught, true);
					} else {
						Notification.showMessage(I18N.INSTANCE.invoiceUpdateFailure());
					}
				}

				@Override
				public void onSuccess(Void result) {
					Notification.showMessage(I18N.INSTANCE.invoiceUpdateSuccess());

					DataWatcher.getInstance().fireInvoiceEvent();
					DataWatcher.getInstance().fireStatsEvent();

					ClientPlace cp = new ClientPlace();
					cp.setClientId(inv.getClient().getId());
					cp.setDocumentsListing(DOCUMENTS.invoices);
					presenter.goTo(cp);
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

	private void setInvoice(InvoiceDTO invoice, boolean cloning) {
		if(!cloning) {
			this.invoice = invoice;
			this.client = invoice.getClient();
			date.setValue(invoice.getAccountingDocumentDate());
			clientName.setText(invoice.getClient().getName());
			modifyDocument.setVisible(true);
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
		this.estimation = estimation;

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
		this.transportDocument = transportDocument;

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
		this.invoice = null;
		this.client = null;
		this.estimation = null;
		this.transportDocument = null;

		//reset widget statuses
		number.reset();
		number.setVisible(true);
		payment.setVisible(true);
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
