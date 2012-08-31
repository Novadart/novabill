package com.novadart.novabill.frontend.client.ui.widget.list.impl;

import com.novadart.novabill.frontend.client.ui.View.Presenter;
import com.novadart.novabill.frontend.client.ui.widget.list.QuickViewList;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;

public class CreditNoteList extends QuickViewList<CreditNoteDTO> {

	public CreditNoteList() {
		super(new CreditNoteCell());
		addStyleName("CreditNoteList");
	}
	
	public void setPresenter(Presenter presenter) {
		((CreditNoteCell) getCell()).setPresenter(presenter);
	}
	
}
