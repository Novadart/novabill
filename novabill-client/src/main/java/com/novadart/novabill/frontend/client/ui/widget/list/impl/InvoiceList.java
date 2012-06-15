package com.novadart.novabill.frontend.client.ui.widget.list.impl;

import com.novadart.novabill.frontend.client.ui.View.Presenter;
import com.novadart.novabill.frontend.client.ui.widget.list.QuickViewList;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class InvoiceList extends QuickViewList<InvoiceDTO> {

	public InvoiceList() {
		super(new InvoiceCell());
		addStyleName("InvoiceList");
	}
	
	public void setPresenter(Presenter presenter) {
		((InvoiceCell) getCell()).setPresenter(presenter);
	}
	
}
