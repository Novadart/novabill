package com.novadart.novabill.frontend.client.presenter.center.invoice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.event.DocumentAddEvent;
import com.novadart.novabill.frontend.client.event.DocumentUpdateEvent;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.place.ClientPlace.DOCUMENTS;
import com.novadart.novabill.frontend.client.presenter.center.DocumentPresenter;
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.view.center.invoice.InvoiceView;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PaymentType;
import com.novadart.novabill.shared.client.exception.ValidationException;

public abstract class AbstractInvoicePresenter extends DocumentPresenter<InvoiceView> implements InvoiceView.Presenter {

	private InvoiceDTO invoice;
	
	public AbstractInvoicePresenter(PlaceController placeController, EventBus eventBus,	InvoiceView view) {
		super(placeController, eventBus, view);
	}

	@Override
	public void onDateChanged(Date date){
		getView().getInvoiceNumberSuffix().setText(" / "+ getYearFormat().format(date));
	}

	@Override
	public void onCreateDocumentClicked() {
		if(!validateInvoice()){
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}

		getView().getCreateDocument().showLoader(true);
		getView().setLocked(true);

		final InvoiceDTO invoice = createInvoice(null);

		ServerFacade.invoice.add(invoice, new ManagedAsyncCallback<Long>() {

			@Override
			public void onSuccess(Long result) {
				getView().getCreateDocument().showLoader(false);
				Notification.showMessage(I18N.INSTANCE.invoiceCreationSuccess(), new NotificationCallback<Void>() {

					@Override
					public void onNotificationClosed(Void value) {
						getEventBus().fireEvent(new DocumentAddEvent(invoice));

						ClientPlace cp = new ClientPlace();
						cp.setClientId(getClient().getId());
						cp.setDocs(DOCUMENTS.invoices);
						goTo(cp);

						getView().setLocked(false);
					}
				});

			}

			@Override
			public void onFailure(Throwable caught) {
				getView().getCreateDocument().showLoader(false);
				if(caught instanceof ValidationException){
					handleServerValidationException((ValidationException) caught);
				} else {
					Notification.showMessage(I18N.INSTANCE.invoiceCreationFailure());
					super.onFailure(caught);
				}
				getView().setLocked(false);
			}
		});

	}

	@Override
	public void onModifyDocumentClicked() {
		if(!validateInvoice()){
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}

		Notification.showConfirm(I18N.INSTANCE.saveModificationsConfirm(), new NotificationCallback<Boolean>() {

			@Override
			public void onNotificationClosed(Boolean value) {
				if(value){

					getView().getModifyDocument().showLoader(true);
					getView().setLocked(true);

					final InvoiceDTO inv = createInvoice(invoice);

					ServerFacade.invoice.update(inv, new ManagedAsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							getView().getModifyDocument().showLoader(false);
							if(caught instanceof ValidationException){
								handleServerValidationException((ValidationException) caught);
							} else {
								Notification.showMessage(I18N.INSTANCE.invoiceUpdateFailure());
								super.onFailure(caught);
							}
							getView().setLocked(false);
						}

						@Override
						public void onSuccess(Void result) {
							getView().getModifyDocument().showLoader(false);
							Notification.showMessage(I18N.INSTANCE.invoiceUpdateSuccess(), new NotificationCallback<Void>() {

								@Override
								public void onNotificationClosed(Void value) {
									getEventBus().fireEvent(new DocumentUpdateEvent(inv));

									ClientPlace cp = new ClientPlace();
									cp.setClientId(inv.getClient().getId());
									cp.setDocs(DOCUMENTS.invoices);
									goTo(cp);
									getView().setLocked(false);
								}
							});
						}
					});
				}
			}
		});
	}

	@Override
	public void onCancelClicked() {
		Notification.showConfirm(I18N.INSTANCE.cancelModificationsConfirmation(), new NotificationCallback<Boolean>() {

			@Override
			public void onNotificationClosed(Boolean value) {
				if(value){
					ClientPlace cp = new ClientPlace();
					cp.setClientId(getClient().getId());
					cp.setDocs(DOCUMENTS.invoices);
					goTo(cp);
				}
			}
		});
	}
	
	
	protected void setInvoice(InvoiceDTO invoice) {
		this.invoice = invoice;
	}
	
	private boolean validateInvoice(){
		if(getView().getDate().getTextBox().getText().isEmpty() || getView().getDate().getValue() == null){
			return false;
		} 
		
		if(getView().getItemInsertionForm().getItems().isEmpty()){
			return false;
		}
		
		getView().getNumber().validate();
		getView().getPayment().validate();
		if(!getView().getNumber().isValid() || !getView().getPayment().isValid()){
			return false;
		}

		return true;
	}
	

	private InvoiceDTO createInvoice(InvoiceDTO invoice){
		InvoiceDTO inv;

		if(invoice != null){
			inv = invoice;
		} else {
			inv = new InvoiceDTO();
			inv.setBusiness(Configuration.getBusiness());
			inv.setClient(getClient());
		}


		inv.setDocumentID(Long.parseLong(getView().getNumber().getText()));
		inv.setAccountingDocumentDate(getView().getDate().getValue());
		List<AccountingDocumentItemDTO> invItems = new ArrayList<AccountingDocumentItemDTO>();
		for (AccountingDocumentItemDTO itemDTO : getView().getItemInsertionForm().getItems()) {
			invItems.add(itemDTO);
		}
		inv.setItems(invItems);
		inv.setNote(getView().getNote().getText());
		inv.setPaymentType(PaymentType.values()[getView().getPayment().getSelectedIndex()-1]);
		if(getView().getPayment().getSelectedIndex() > 0){
			inv.setPaymentDueDate(DocumentUtils.calculatePaymentDueDate(inv.getAccountingDocumentDate(), inv.getPaymentType()));  
		} else {
			inv.setPaymentDueDate(null);
		}

		inv.setPaymentNote(getView().getPaymentNote().getText());
		DocumentUtils.calculateTotals(invItems, inv);
		return inv;
	}

}
