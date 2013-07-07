package com.novadart.novabill.frontend.client.widget.list.impl;

import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.presenter.Presenter;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public interface InvoiceCell {

	public abstract void setPresenter(Presenter presenter);

	public abstract void setEventBus(EventBus eventBus);

	public abstract void onPdfClicked(InvoiceDTO invoice);

}