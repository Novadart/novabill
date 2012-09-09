package com.novadart.novabill.frontend.client.ui.widget.list.impl;

import com.novadart.novabill.frontend.client.ui.View.Presenter;
import com.novadart.novabill.frontend.client.ui.widget.list.QuickViewList;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public class TransportDocumentList extends QuickViewList<TransportDocumentDTO> {

	public TransportDocumentList() {
		super(new TransportDocumentCell());
		addStyleName("EstimationList");
	}

	public void setPresenter(Presenter presenter) {
		((TransportDocumentCell) getCell()).setPresenter(presenter);
	}

}
