package com.novadart.novabill.frontend.client.widget.list.impl;

import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.presenter.Presenter;
import com.novadart.novabill.shared.client.dto.EstimationDTO;

public interface EstimationCell {

	public abstract void onPdfClicked(EstimationDTO estimation);

	public abstract void setPresenter(Presenter presenter);

	public abstract void setEventBus(EventBus eventBus);

}