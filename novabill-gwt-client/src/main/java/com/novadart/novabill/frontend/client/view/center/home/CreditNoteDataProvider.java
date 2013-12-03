package com.novadart.novabill.frontend.client.view.center.home;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;

public class CreditNoteDataProvider extends AsyncDataProvider<CreditNoteDTO> {

	@Override
	protected void onRangeChanged(HasData<CreditNoteDTO> display) {
//		final int start = 0;
//		final int length = display.getVisibleRange().getLength();
//		
//		ServerFacade.INSTANCE.getCreditNoteService().getAllInRange(Configuration.getBusinessId(), start, length, new ManagedAsyncCallback<PageDTO<CreditNoteDTO>>() {
//
//			@Override
//			public void onSuccess(PageDTO<CreditNoteDTO> result) {
//				List<CreditNoteDTO> items = result.getItems();
//				Collections.sort(items, SharedComparators.DOCUMENT_COMPARATOR);
//				updateRowData(start, items);
//			}
//
//		});
	}

}
