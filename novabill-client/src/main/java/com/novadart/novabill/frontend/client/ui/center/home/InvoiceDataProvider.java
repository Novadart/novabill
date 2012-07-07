package com.novadart.novabill.frontend.client.ui.center.home;

import java.util.List;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class InvoiceDataProvider extends AsyncDataProvider<InvoiceDTO> {

	@Override
	protected void onRangeChanged(HasData<InvoiceDTO> display) {
		final int start = 0;
		final int length = display.getVisibleRange().getLength();
		
		ServerFacade.invoice.getAllInRange(start, length, new WrappedAsyncCallback<List<InvoiceDTO>>() {

			@Override
			public void onSuccess(List<InvoiceDTO> result) {
				updateRowData(start, result);
			}

			@Override
			public void onException(Throwable caught) {

			}
		});
	}

}
