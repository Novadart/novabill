package com.novadart.novabill.frontend.client.view.widget.list.impl;

import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.view.View.Presenter;
import com.novadart.novabill.frontend.client.view.widget.list.QuickViewList;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;

public class CreditNoteList extends QuickViewList<CreditNoteDTO> {

	public CreditNoteList() {
		super(new CreditNoteCell());
		addStyleName("CreditNoteList");
	}
	
	public void setPresenter(Presenter presenter) {
		((CreditNoteCell) getCell()).setPresenter(presenter);
	}
	
	public void setEventBus(EventBus eventBus){
		((CreditNoteCell) getCell()).setEventBus(eventBus);
	}
}
