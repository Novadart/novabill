package com.novadart.novabill.frontend.client.presenter.center.creditnote;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.event.DocumentAddEvent;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.i18n.I18NM;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.place.ClientPlace.DOCUMENTS;
import com.novadart.novabill.frontend.client.view.center.creditnote.CreditNoteView;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.exception.ValidationException;

public class NewCreditNotePresenter extends AbstractCreditNotePresenter {



	public NewCreditNotePresenter(PlaceController placeController, EventBus eventBus, CreditNoteView view) {
		super(placeController, eventBus, view);
	}


	public void setDataForNewCreditNote(ClientDTO client, Long progressiveId) {
		setClient(client);

		getView().getClientName().setText(client.getName());
		getView().getDate().setValue(new Date());
		getView().getNumber().setText(progressiveId.toString());

		getView().getCreateDocument().setVisible(true);
	}


	public void setDataForNewCreditNote(Long progressiveId, InvoiceDTO invoice) {
		setDataForNewCreditNote(invoice.getClient(), progressiveId);

		List<AccountingDocumentItemDTO> items = null;
		items = new ArrayList<AccountingDocumentItemDTO>(invoice.getItems().size());
		for (AccountingDocumentItemDTO i : invoice.getItems()) {
			items.add(i.clone());
		}
		getView().getItemInsertionForm().setItems(items);
		getView().getNote().setText(I18NM.get.generatedFromInvoice(
				invoice.getDocumentID()+"/"+ getYearFormat().format(invoice.getAccountingDocumentDate()), 
				DateTimeFormat.getFormat("dd MMMM yyyy").format(invoice.getAccountingDocumentDate())));
	}


	@Override
	public void onLoad() {
		getView().getTitleLabel().setText(I18N.INSTANCE.newCreditNoteCreation());
	}


	@Override
	protected void setPresenterInView(CreditNoteView view) {
		view.setPresenter(this);
	}


	@Override
	public void onCreateDocumentClicked() {
		if(!validateCreditNote()){
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}

		final CreditNoteDTO creditNote = createCreditNote(null);

		getView().setLocked(true);
		getView().getCreateDocument().showLoader(true);
		ServerFacade.INSTANCE.getCreditnoteService().add(creditNote, new ManagedAsyncCallback<Long>() {

			@Override
			public void onSuccess(Long result) {
				Notification.showMessage(I18N.INSTANCE.creditNoteCreationSuccess(), new NotificationCallback<Void>() {

					@Override
					public void onNotificationClosed(Void value) {
						getView().getCreateDocument().showLoader(false);
						getEventBus().fireEvent(new DocumentAddEvent(creditNote));

						ClientPlace cp = new ClientPlace();
						cp.setClientId(getClient().getId());
						cp.setDocs(DOCUMENTS.creditNotes);
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
					super.onFailure(caught);
				}
				getView().setLocked(false);
			}
		});
	}


}
