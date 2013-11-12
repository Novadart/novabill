package com.novadart.novabill.frontend.client.view.center.home;

import java.util.Collections;
import java.util.List;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.SharedComparators;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;

public class EstimationDataProvider extends AsyncDataProvider<EstimationDTO> {

	@Override
	protected void onRangeChanged(HasData<EstimationDTO> display) {
		final int start = 0;
		final int length = display.getVisibleRange().getLength();
		
		ServerFacade.INSTANCE.getEstimationService().getAllInRange(Configuration.getBusinessId(), start, length, new ManagedAsyncCallback<PageDTO<EstimationDTO>>() {

			@Override
			public void onSuccess(PageDTO<EstimationDTO> result) {
				List<EstimationDTO> items = result.getItems();
				Collections.sort(items, SharedComparators.DOCUMENT_COMPARATOR);
				updateRowData(start, items);
			}

		});
	}

}
