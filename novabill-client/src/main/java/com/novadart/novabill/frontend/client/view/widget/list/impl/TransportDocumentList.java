package com.novadart.novabill.frontend.client.view.widget.list.impl;

import com.novadart.novabill.frontend.client.view.View.Presenter;
import com.novadart.novabill.frontend.client.view.widget.list.QuickViewList;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public class TransportDocumentList extends QuickViewList<TransportDocumentDTO> {

	public TransportDocumentList() {
		super(new TransportDocumentCell());
		addStyleName("TransportDocumentList");
	}

	public void setPresenter(Presenter presenter) {
		((TransportDocumentCell) getCell()).setPresenter(presenter);
	}

}
