package com.novadart.novabill.frontend.client.presenter.center.creditnote;

import java.util.List;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.view.center.creditnote.CreditNoteView;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;

public class ModifyCreditNotePresenter extends AbstractCreditNotePresenter {

	
	public ModifyCreditNotePresenter(PlaceController placeController, EventBus eventBus, CreditNoteView view) {
		super(placeController, eventBus, view);
	}

	public void setData(CreditNoteDTO creditNote) {
		setCreditNote(creditNote);
		setClient(creditNote.getClient());
		getView().getDate().setValue(creditNote.getAccountingDocumentDate());
		getView().getClientName().setText(creditNote.getClient().getName());
		getView().getModifyDocument().setVisible(true);

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
		getView().getTitleLabel().setText(I18N.INSTANCE.modifyCreditNote());
	}

	@Override
	protected void setPresenterinView(CreditNoteView view) {
		view.setPresenter(this);
	};
	
}
