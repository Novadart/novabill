package com.novadart.novabill.frontend.client.widget.list.impl;

import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.presenter.Presenter;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public interface TransportDocumentCell {

	public abstract void onPdfClicked(TransportDocumentDTO transportDocument);

	public abstract void setPresenter(Presenter presenter);

	public abstract void setEventBus(EventBus eventBus);

}