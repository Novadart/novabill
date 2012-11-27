package com.novadart.novabill.frontend.client.ui.center.home;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public class TransportDocumentDataProvider extends AsyncDataProvider<TransportDocumentDTO> {

	@Override
	protected void onRangeChanged(HasData<TransportDocumentDTO> display) {
		final int start = 0;
		final int length = display.getVisibleRange().getLength();
		
		ServerFacade.transportDocument.getAllInRange(Configuration.getBusinessId(), start, length, new WrappedAsyncCallback<PageDTO<TransportDocumentDTO>>() {

			@Override
			public void onSuccess(PageDTO<TransportDocumentDTO> result) {
				updateRowData(start, result.getItems());
			}

			@Override
			public void onException(Throwable caught) {

			}
		});
	}

}
