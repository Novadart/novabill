package com.novadart.novabill.frontend.client.presenter.center.creditnote;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.view.center.creditnote.CreditNoteView;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

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
	}


	@Override
	public void onLoad() {
		getView().getTitleLabel().setText(I18N.INSTANCE.newCreditNoteCreation());
	}


	@Override
	protected void setPresenterinView(CreditNoteView view) {
		view.setPresenter(this);
	}

	
	
}
