package com.novadart.novabill.frontend.client.view.center.home;

import java.util.Collections;
import java.util.List;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.SharedComparators;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public class TransportDocumentDataProvider extends AsyncDataProvider<TransportDocumentDTO> {

	@Override
	protected void onRangeChanged(HasData<TransportDocumentDTO> display) {
		final int start = 0;
		final int length = display.getVisibleRange().getLength();
		
		ServerFacade.INSTANCE.getTransportdocumentService().getAllInRange(Configuration.getBusinessId(), start, length, new ManagedAsyncCallback<PageDTO<TransportDocumentDTO>>() {

			@Override
			public void onSuccess(PageDTO<TransportDocumentDTO> result) {
				List<TransportDocumentDTO> items = result.getItems();
				Collections.sort(items, SharedComparators.DOCUMENT_COMPARATOR);
				updateRowData(start, items);
			}

		});
	}

}
