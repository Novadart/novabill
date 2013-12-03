package com.novadart.novabill.frontend.client.view.center.home;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.novadart.novabill.shared.client.dto.EstimationDTO;

public class EstimationDataProvider extends AsyncDataProvider<EstimationDTO> {

	@Override
	protected void onRangeChanged(HasData<EstimationDTO> display) {
//		final int start = 0;
//		final int length = display.getVisibleRange().getLength();
//		
//		ServerFacade.INSTANCE.getEstimationService().getAllInRange(Configuration.getBusinessId(), start, length, new ManagedAsyncCallback<PageDTO<EstimationDTO>>() {
//
//			@Override
//			public void onSuccess(PageDTO<EstimationDTO> result) {
//				List<EstimationDTO> items = result.getItems();
//				Collections.sort(items, SharedComparators.DOCUMENT_COMPARATOR);
//				updateRowData(start, items);
//			}
//
//		});
	}

}
