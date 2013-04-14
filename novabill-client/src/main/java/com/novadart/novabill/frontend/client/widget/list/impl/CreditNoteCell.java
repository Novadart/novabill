package com.novadart.novabill.frontend.client.widget.list.impl;

import com.google.gwt.cell.client.Cell;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.presenter.Presenter;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;

public interface CreditNoteCell extends Cell<CreditNoteDTO> {

	public abstract void setPresenter(Presenter presenter);

	public abstract void setEventBus(EventBus eventBus);

	public abstract void onPdfClicked(CreditNoteDTO creditNote);

}