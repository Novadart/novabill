package com.novadart.novabill.frontend.client.presenter.center.invoice;

import java.util.List;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.event.DocumentUpdateEvent;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.place.ClientPlace.DOCUMENTS;
import com.novadart.novabill.frontend.client.view.center.invoice.InvoiceView;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.exception.ValidationException;

public class ModifyInvoicePresenter extends AbstractInvoicePresenter {

	public ModifyInvoicePresenter(PlaceController placeController, EventBus eventBus, InvoiceView view) {
		super(placeController, eventBus, view);
	}
	
	@Override
	protected void setPresenterInView(InvoiceView view) {
		view.setPresenter(this);
	}

	@Override
	public void onLoad() {
		getView().getCreateDocument().setText(I18N.INSTANCE.saveModifications());
		getView().getTitleLabel().setText(I18N.INSTANCE.modifyInvoice());
	}

	public void setData(InvoiceDTO invoice) {
		setInvoice(invoice);
		setClient(invoice.getClient());
		getView().getDate().setValue(invoice.getAccountingDocumentDate());
		getView().getPayment().setDocumentCreationDate(invoice.getAccountingDocumentDate());
		
		getView().getPayment().init(invoice.getPaymentTypeName(), invoice.getPaymentDateGenerator(), invoice.getPaymentDateDelta(), invoice.getPaymentDueDate());
		getView().getInvoiceNumberSuffix().setText(" / "+ getYearFormat().format(invoice.getAccountingDocumentDate()));
		getView().getClientName().setText(invoice.getClient().getName());

		List<AccountingDocumentItemDTO> items = null;
		items = invoice.getItems();


		getView().getItemInsertionForm().setItems(items);
		if(invoice.getDocumentID() != null){
			getView().getNumber().setText(invoice.getDocumentID().toString());
		} 
		getView().getNote().setText(invoice.getNote());
		getView().getPaymentNote().setText(invoice.getPaymentNote());
	}
	
	@Override
	public void onCreateDocumentClicked() {
		if(!validateInvoice()){
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}

		Notification.showConfirm(I18N.INSTANCE.saveModificationsConfirm(), new NotificationCallback<Boolean>() {

			@Override
			public void onNotificationClosed(Boolean value) {
				if(value){

					getView().getCreateDocument().showLoader(true);
					getView().setLocked(true);

					final InvoiceDTO inv = createInvoice(getInvoice());

					ServerFacade.invoice.update(inv, new ManagedAsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							getView().getCreateDocument().showLoader(false);
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
							getView().getCreateDocument().showLoader(false);
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

}