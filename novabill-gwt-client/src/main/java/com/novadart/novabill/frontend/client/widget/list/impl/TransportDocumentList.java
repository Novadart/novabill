package com.novadart.novabill.frontend.client.widget.list.impl;

import com.google.gwt.core.shared.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.presenter.Presenter;
import com.novadart.novabill.frontend.client.widget.list.QuickViewCell;
import com.novadart.novabill.frontend.client.widget.list.QuickViewList;
import com.novadart.novabill.frontend.client.widget.list.resources.QuickViewListBundle;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public class TransportDocumentList extends QuickViewList<TransportDocumentDTO> {

	@SuppressWarnings("unchecked")
	public TransportDocumentList() {
		super((QuickViewCell<TransportDocumentDTO>)GWT.create(TransportDocumentCell.class));
		addStyleName(QuickViewListBundle.INSTANCE.quickViewListCss().transportDocumentList());
	}

	public void setPresenter(Presenter presenter) {
		((TransportDocumentCell) getCell()).setPresenter(presenter);
	}
	
	public void setEventBus(EventBus eventBus){
		((TransportDocumentCell) getCell()).setEventBus(eventBus);
	}

}
