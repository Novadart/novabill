package com.novadart.novabill.frontend.client.presenter.center.creditnote;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.place.ClientPlace.DOCUMENTS;
import com.novadart.novabill.frontend.client.presenter.center.DocumentPresenter;
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.view.center.creditnote.CreditNoteView;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;

public abstract class AbstractCreditNotePresenter extends DocumentPresenter<CreditNoteView> implements CreditNoteView.Presenter {

	private CreditNoteDTO creditNote;
	
	public AbstractCreditNotePresenter(PlaceController placeController, EventBus eventBus, CreditNoteView view) {
		super(placeController, eventBus, view);
	}
	
	protected void setCreditNote(CreditNoteDTO creditNote) {
		this.creditNote = creditNote;
	}
	
	protected CreditNoteDTO getCreditNote() {
		return creditNote;
	}

	@Override
	public void onCancelClicked() {
		Notification.showConfirm(I18N.INSTANCE.cancelModificationsConfirmation(), new NotificationCallback<Boolean>() {

			@Override
			public void onNotificationClosed(Boolean value) {
				if(value){
					ClientPlace cp = new ClientPlace();
					cp.setClientId(getClient().getId());
					cp.setDocs(DOCUMENTS.creditNotes);
					goTo(cp);
				}
			}
		});
	}


	protected CreditNoteDTO createCreditNote(CreditNoteDTO creditNote){
		CreditNoteDTO cn;

		if(creditNote != null){
			cn = creditNote;
		} else {
			cn = new CreditNoteDTO();
			cn.setBusiness(Configuration.getBusiness());
			cn.setClient(getClient());
		}


		cn.setDocumentID(Long.parseLong(getView().getNumber().getText()));
		cn.setAccountingDocumentDate(getView().getDate().getValue());
		List<AccountingDocumentItemDTO> invItems = new ArrayList<AccountingDocumentItemDTO>();
		for (AccountingDocumentItemDTO itemDTO : getView().getItemInsertionForm().getItems()) {
			invItems.add(itemDTO);
		}
		cn.setItems(invItems);
		cn.setNote(getView().getNote().getText());
		DocumentUtils.calculateTotals(invItems, cn);
		return cn;
	}
	
	
	protected boolean validateCreditNote(){
		getView().getDate().validate();
		getView().getNumber().validate();
		
		if(getView().getItemInsertionForm().getItems().isEmpty()){
			return false;
		}
		
		return getView().getNumber().isValid() && getView().getDate().isValid();
	}

}