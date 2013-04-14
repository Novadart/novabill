package com.novadart.novabill.frontend.client.widget.list.impl;

import com.google.gwt.core.shared.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.presenter.Presenter;
import com.novadart.novabill.frontend.client.widget.list.QuickViewCell;
import com.novadart.novabill.frontend.client.widget.list.QuickViewList;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class InvoiceList extends QuickViewList<InvoiceDTO> {

	@SuppressWarnings("unchecked")
	public InvoiceList() {
		super((QuickViewCell<InvoiceDTO>)GWT.create(InvoiceCell.class));
	}
	
	public void setPresenter(Presenter presenter) {
		((InvoiceCell) getCell()).setPresenter(presenter);
	}
	
	public void setEventBus(EventBus eventBus){
		((InvoiceCell) getCell()).setEventBus(eventBus);
	}
}
