package com.novadart.novabill.frontend.client.ui.widget.list.impl;

import com.novadart.novabill.frontend.client.ui.View.Presenter;
import com.novadart.novabill.frontend.client.ui.widget.list.QuickViewList;
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
