package com.novadart.novabill.frontend.client.view.center.home;

import java.util.Collections;
import java.util.List;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.SharedComparators;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;

public class CreditNoteDataProvider extends AsyncDataProvider<CreditNoteDTO> {

	@Override
	protected void onRangeChanged(HasData<CreditNoteDTO> display) {
		final int start = 0;
		final int length = display.getVisibleRange().getLength();
		
		ServerFacade.INSTANCE.getCreditNoteService().getAllInRange(Configuration.getBusinessId(), start, length, new ManagedAsyncCallback<PageDTO<CreditNoteDTO>>() {

			@Override
			public void onSuccess(PageDTO<CreditNoteDTO> result) {
				List<CreditNoteDTO> items = result.getItems();
				Collections.sort(items, SharedComparators.DOCUMENT_COMPARATOR);
				updateRowData(start, items);
			}

		});
	}

}
