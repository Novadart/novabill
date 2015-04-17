package com.novadart.novabill.frontend.client.presenter.center.invoice;

import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.ListBox;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.bridge.BridgeUtils;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.view.center.invoice.InvoiceView;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.dto.*;
import com.novadart.novabill.shared.client.exception.ValidationException;

public class ModifyInvoicePresenter extends AbstractInvoicePresenter {

	public ModifyInvoicePresenter(PlaceController placeController, EventBus eventBus, InvoiceView view,
								  List<DocumentIDClassDTO> documentIDClassesDTOs, JavaScriptObject callback) {
		super(placeController, eventBus, view, documentIDClassesDTOs, callback);
	}
	
	@Override
	protected void setPresenterInView(InvoiceView view) {
		view.setPresenter(this);
	}

	public void setData(InvoiceDTO invoice) {
		setInvoice(invoice);

		ClientDTO client = invoice.getClient();
		setClient(client);

		ListBox listBox = getView().getDocumentIDClassListBox();
		listBox.clear();
		listBox.addItem(I18N.INSTANCE.invoiceDefaultNumberClass());

		int selectedIndex = -1;
		String docIdClass = invoice.getDocumentIDSuffix();
		if(docIdClass == null) { // using the default doc id class

			selectedIndex = 0;

		} else { // the document has a non default document class id

			// let's try to find it among the ones we loaded from the db
			int i = 0;
			for (DocumentIDClassDTO documentIDClassDTO : getDocumentIDClasses()) {
				i++;
				if (docIdClass.equals(documentIDClassDTO.getSuffix())) {
					selectedIndex = i;
					break;
				}
			}

			// in his case a docid class has been set but it does not correspond to any of the existing doc id classes in the DB.
			// This means that the docid class does not exist anymore. We need to manually add it
			if(selectedIndex == -1){
				listBox.addItem(docIdClass);
				selectedIndex = 1;
			}
		}

		// add the other doc id classes
		for (DocumentIDClassDTO dcd : getDocumentIDClasses()) {
			listBox.addItem(dcd.getSuffix());
		}

		listBox.setSelectedIndex(selectedIndex);


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
		getView().getToAddrProvince().setText(loc.getProvince());
		getView().getToAddrStreetName().setText(loc.getStreet());
		getView().getToAddrCountry().setSelectedItemByValue(loc.getCountry());
		getView().getSetToAddress().setValue(true);
		getView().getToAddressContainer().setVisible(true);
		
		getView().getNote().setText(invoice.getNote());
		getView().getPaymentNote().setText(invoice.getPaymentNote());

		getView().getSelectSplitPayment().setSelectedIndex(invoice.isSplitPayment() ? 1 : 0);
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
