package com.novadart.novabill.frontend.client.presenter.center.creditnote;

import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.bridge.BridgeUtils;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.view.center.creditnote.CreditNoteView;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EndpointDTO;
import com.novadart.novabill.shared.client.exception.ValidationException;

public class ModifyCreditNotePresenter extends AbstractCreditNotePresenter {


	public ModifyCreditNotePresenter(PlaceController placeController, EventBus eventBus, CreditNoteView view, JavaScriptObject callback) {
		super(placeController, eventBus, view, callback);
	}

	public void setData(CreditNoteDTO creditNote) {
		setCreditNote(creditNote);
		setClient(creditNote.getClient());
		getView().getDate().setValue(creditNote.getAccountingDocumentDate());
		getView().getClientName().setText(creditNote.getClient().getName());

		List<AccountingDocumentItemDTO> items = null;
		items = creditNote.getItems();
		
        EndpointDTO loc = creditNote.getToEndpoint();
		getView().getToAddrCity().setText(loc.getCity());
		getView().getToAddrCompanyName().setText(loc.getCompanyName());
		getView().getToAddrPostCode().setText(loc.getPostcode());
		getView().getToAddrProvince().setText(loc.getProvince());
		getView().getToAddrStreetName().setText(loc.getStreet());
		getView().getToAddrCountry().setSelectedItemByValue(loc.getCountry());
		getView().getSetToAddress().setValue(true);
		getView().getToAddressContainer().setVisible(true);

		getView().getItemInsertionForm().setItems(items);
		getView().getNumber().setText(creditNote.getDocumentID().toString());
		getView().getNote().setText(creditNote.getNote());
	}

	@Override
	protected void setPresenterInView(CreditNoteView view) {
		view.setPresenter(this);
	};


	@Override
	public void onCreateDocumentClicked() {
		if(!validateCreditNote()){
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}

		Notification.showConfirm(I18N.INSTANCE.saveModificationsConfirm(), new NotificationCallback() {

			@Override
			public void onNotificationClosed(boolean value) {
				if(value){
					final CreditNoteDTO cn = createCreditNote(getCreditNote());

					getView().setLocked(true);
					getView().getCreateDocument().showLoader(true);

					ServerFacade.INSTANCE.getCreditNoteService().update(cn, new ManagedAsyncCallback<Void>() {

						@Override
						public void onSuccess(Void result) {
							Notification.showMessage(I18N.INSTANCE.creditNoteUpdateSuccess(), new NotificationCallback() {

								@Override
								public void onNotificationClosed(boolean value) {
									getView().getCreateDocument().showLoader(false);
									BridgeUtils.invokeJSCallback(Boolean.TRUE, getCallback());
								}
							});
							getView().setLocked(false);
						}

						@Override
						public void onFailure(Throwable caught) {
							getView().getCreateDocument().showLoader(false);
							if(caught instanceof ValidationException){
								handleServerValidationException((ValidationException) caught);
							} else {
								super.onFailure(caught);
							}
							getView().setLocked(false);
						}
					});
				}
			}
		});
	}
}
