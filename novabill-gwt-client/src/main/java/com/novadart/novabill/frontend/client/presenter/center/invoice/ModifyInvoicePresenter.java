package com.novadart.novabill.frontend.client.presenter.center.invoice;

import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.bridge.BridgeUtils;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.view.center.invoice.InvoiceView;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EndpointDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.exception.ValidationException;

public class ModifyInvoicePresenter extends AbstractInvoicePresenter {

	public ModifyInvoicePresenter(PlaceController placeController, EventBus eventBus, InvoiceView view, JavaScriptObject callback) {
		super(placeController, eventBus, view, callback);
	}
	
	@Override
	protected void setPresenterInView(InvoiceView view) {
		view.setPresenter(this);
	}

	public void setData(InvoiceDTO invoice) {
		setInvoice(invoice);
		setClient(invoice.getClient());
		getView().getDate().setValue(invoice.getAccountingDocumentDate());
		getView().getPayment().setDocumentCreationDate(invoice.getAccountingDocumentDate());
		
		getView().getPayment().init(invoice.getPaymentTypeName(), invoice.getPaymentDateGenerator(), 
				invoice.getPaymentDateDelta(), invoice.getPaymentDeltaType(), invoice.getSecondaryPaymentDateDelta(), 
				invoice.getPaymentDueDate());
		getView().getInvoiceNumberSuffix().setText(" / "+ getYearFormat().format(invoice.getAccountingDocumentDate()));
		getView().getClientName().setText(invoice.getClient().getName());

		List<AccountingDocumentItemDTO> items = null;
		items = invoice.getItems();

		getView().getItemInsertionForm().setItems(items);
		if(invoice.getDocumentID() != null){
			getView().getNumber().setText(invoice.getDocumentID().toString());
		}
		
        EndpointDTO loc = invoice.getToEndpoint();
		getView().getToAddrCity().setText(loc.getCity());
		getView().getToAddrCompanyName().setText(loc.getCompanyName());
		getView().getToAddrPostCode().setText(loc.getPostcode());
		if("IT".equalsIgnoreCase(loc.getCountry())){
			getView().getToAddrProvince().setSelectedItem(loc.getProvince());
		} else {
			getView().getToAddrProvince().setEnabled(false);
		} 
		getView().getToAddrStreetName().setText(loc.getStreet());
		getView().getToAddrCountry().setSelectedItemByValue(loc.getCountry());
		getView().getSetToAddress().setValue(true);
		getView().getToAddressContainer().setVisible(true);
		
		getView().getNote().setText(invoice.getNote());
		getView().getPaymentNote().setText(invoice.getPaymentNote());
	}
	
	@Override
	public void onCreateDocumentClicked() {
		if(!validateInvoice()){
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}

		Notification.showConfirm(I18N.INSTANCE.saveModificationsConfirm(), new NotificationCallback() {

			@Override
			public void onNotificationClosed(boolean value) {
				if(value){

					getView().getCreateDocument().showLoader(true);
					getView().setLocked(true);

					final InvoiceDTO inv = createInvoice(getInvoice());
					
					final ManagedAsyncCallback<Void> updateClientCallback = new ManagedAsyncCallback<Void>() {
						
						@Override
						public void onSuccess(Void result) {
							ServerFacade.INSTANCE.getInvoiceService().update(inv, new ManagedAsyncCallback<Void>() {

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
									Notification.showMessage(I18N.INSTANCE.invoiceUpdateSuccess(), new NotificationCallback() {

										@Override
										public void onNotificationClosed(boolean value) {
											getView().setLocked(false);
											BridgeUtils.invokeJSCallback(Boolean.TRUE, getCallback());
										}
									});
								}
							});
						}
						
						public void onFailure(Throwable caught) {
							getView().getCreateDocument().showLoader(false);
							Notification.showMessage(I18N.INSTANCE.invoiceUpdateFailure());
							super.onFailure(caught);
							getView().setLocked(false);
						};
					};

					
					
					
					if(getView().getMakePaymentAsDefault().getValue()){
						// if the user decides to make this the default payment, update this info then add the invoice
						ClientDTO client = getClient();
						client.setDefaultPaymentTypeID(getView().getPayment().getSelectedPayment().getId());

						ServerFacade.INSTANCE.getClientService().update(Configuration.getBusinessId(), client, updateClientCallback);
					} else {
						// otherwise just add the invoice
						updateClientCallback.onSuccess(null);
					}
					
					
					
				}
			}
		});
	}

}
