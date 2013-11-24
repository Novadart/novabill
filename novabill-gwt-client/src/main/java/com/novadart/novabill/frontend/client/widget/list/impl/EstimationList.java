package com.novadart.novabill.frontend.client.widget.list.impl;

import com.google.gwt.core.shared.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.presenter.Presenter;
import com.novadart.novabill.frontend.client.widget.list.QuickViewCell;
import com.novadart.novabill.frontend.client.widget.list.QuickViewList;
import com.novadart.novabill.frontend.client.widget.list.resources.QuickViewListBundle;
import com.novadart.novabill.shared.client.dto.EstimationDTO;

public class EstimationList extends QuickViewList<EstimationDTO> {

	@SuppressWarnings("unchecked")
	public EstimationList() {
		super((QuickViewCell<EstimationDTO>)GWT.create(EstimationCell.class));
		addStyleName(QuickViewListBundle.INSTANCE.quickViewListCss().estimationList());
	}

	public void setPresenter(Presenter presenter) {
		((EstimationCell) getCell()).setPresenter(presenter);
	}
	
	public void setEventBus(EventBus eventBus){
		((EstimationCell) getCell()).setEventBus(eventBus);
	}

}