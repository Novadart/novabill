package com.novadart.novabill.frontend.client.widget.list.impl;

import com.google.gwt.core.shared.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.presenter.Presenter;
import com.novadart.novabill.frontend.client.widget.list.QuickViewCell;
import com.novadart.novabill.frontend.client.widget.list.QuickViewList;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;

public class CreditNoteList extends QuickViewList<CreditNoteDTO> {

	@SuppressWarnings("unchecked")
	public CreditNoteList() {
		super((QuickViewCell<CreditNoteDTO>)GWT.create(CreditNoteCell.class));
	}
	
	public void setPresenter(Presenter presenter) {
		((CreditNoteCell) getCell()).setPresenter(presenter);
	}
	
	public void setEventBus(EventBus eventBus){
		((CreditNoteCell) getCell()).setEventBus(eventBus);
	}
}
