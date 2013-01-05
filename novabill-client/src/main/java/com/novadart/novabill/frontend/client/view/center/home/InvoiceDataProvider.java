package com.novadart.novabill.frontend.client.view.center.home;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;

public class InvoiceDataProvider extends AsyncDataProvider<InvoiceDTO> {

	@Override
	protected void onRangeChanged(HasData<InvoiceDTO> display) {
		final int start = 0;
		final int length = display.getVisibleRange().getLength();
		
		ServerFacade.invoice.getAllInRange(Configuration.getBusinessId(), start, length, new WrappedAsyncCallback<PageDTO<InvoiceDTO>>() {

			@Override
			public void onSuccess(PageDTO<InvoiceDTO> result) {
				updateRowData(start, result.getItems());
			}

			@Override
			public void onException(Throwable caught) {

			}
		});
	}

}
