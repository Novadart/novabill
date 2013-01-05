package com.novadart.novabill.frontend.client.view.center.home;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;

public class EstimationDataProvider extends AsyncDataProvider<EstimationDTO> {

	@Override
	protected void onRangeChanged(HasData<EstimationDTO> display) {
		final int start = 0;
		final int length = display.getVisibleRange().getLength();
		
		ServerFacade.estimation.getAllInRange(Configuration.getBusinessId(), start, length, new WrappedAsyncCallback<PageDTO<EstimationDTO>>() {

			@Override
			public void onSuccess(PageDTO<EstimationDTO> result) {
				updateRowData(start, result.getItems());
			}

			@Override
			public void onException(Throwable caught) {

			}
		});
	}

}
