package com.novadart.novabill.frontend.client.widget.list.impl;

import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.presenter.Presenter;
import com.novadart.novabill.frontend.client.widget.list.QuickViewList;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class InvoiceList extends QuickViewList<InvoiceDTO> {

	public InvoiceList() {
		super(new InvoiceCell());
		addStyleName("InvoiceList");
	}
	
	public void setPresenter(Presenter presenter) {
		((InvoiceCell) getCell()).setPresenter(presenter);
	}
	
	public void setEventBus(EventBus eventBus){
		((InvoiceCell) getCell()).setEventBus(eventBus);
	}
}
