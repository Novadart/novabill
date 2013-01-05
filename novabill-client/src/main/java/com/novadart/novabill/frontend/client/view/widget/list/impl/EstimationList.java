package com.novadart.novabill.frontend.client.view.widget.list.impl;

import com.novadart.novabill.frontend.client.view.View.Presenter;
import com.novadart.novabill.frontend.client.view.widget.list.QuickViewList;
import com.novadart.novabill.shared.client.dto.EstimationDTO;

public class EstimationList extends QuickViewList<EstimationDTO> {

	public EstimationList() {
		super(new EstimationCell());
		addStyleName("EstimationList");
	}

	public void setPresenter(Presenter presenter) {
		((EstimationCell) getCell()).setPresenter(presenter);
	}

}
