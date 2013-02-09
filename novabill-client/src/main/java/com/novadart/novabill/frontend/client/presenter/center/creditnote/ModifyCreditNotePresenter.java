package com.novadart.novabill.frontend.client.presenter.center.creditnote;

import java.util.List;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.event.DocumentUpdateEvent;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.place.ClientPlace.DOCUMENTS;
import com.novadart.novabill.frontend.client.view.center.creditnote.CreditNoteView;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.exception.ValidationException;

public class ModifyCreditNotePresenter extends AbstractCreditNotePresenter {


	public ModifyCreditNotePresenter(PlaceController placeController, EventBus eventBus, CreditNoteView view) {
		super(placeController, eventBus, view);
	}

	public void setData(CreditNoteDTO creditNote) {
		setCreditNote(creditNote);
		setClient(creditNote.getClient());
		getView().getDate().setValue(creditNote.getAccountingDocumentDate());
		getView().getClientName().setText(creditNote.getClient().getName());

		List<AccountingDocumentItemDTO> items = null;
		items = creditNote.getItems();

		getView().getItemInsertionForm().setItems(items);
		getView().getNumber().setText(creditNote.getDocumentID().toString());
		getView().getNote().setText(creditNote.getNote());
		getView().getPaymentNote().setText(creditNote.getPaymentNote());
		getView().getPayment().setSelectedIndex(creditNote.getPaymentType().ordinal()+1);

	}

	@Override
	public void onLoad() {
		getView().getCreateDocument().setText(I18N.INSTANCE.saveModifications());
		getView().getTitleLabel().setText(I18N.INSTANCE.modifyCreditNote());
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

		Notification.showConfirm(I18N.INSTANCE.saveModificationsConfirm(), new NotificationCallback<Boolean>() {

			@Override
			public void onNotificationClosed(Boolean value) {
				if(value){
					final CreditNoteDTO cn = createCreditNote(getCreditNote());

					getView().setLocked(true);
					getView().getCreateDocument().showLoader(true);

					ServerFacade.creditNote.update(cn, new ManagedAsyncCallback<Void>() {

						@Override
						public void onSuccess(Void result) {
							Notification.showMessage(I18N.INSTANCE.creditNoteUpdateSuccess(), new NotificationCallback<Void>() {

								@Override
								public void onNotificationClosed(Void value) {
									getView().getCreateDocument().showLoader(false);
									getEventBus().fireEvent(new DocumentUpdateEvent(cn));

									ClientPlace cp = new ClientPlace();
									cp.setClientId(cn.getClient().getId());
									cp.setDocs(DOCUMENTS.creditNotes);
									goTo(cp);
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
