package com.novadart.novabill.frontend.client.view.center.home;

import java.util.Collections;
import java.util.List;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.Const;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;

public class InvoiceDataProvider extends AsyncDataProvider<InvoiceDTO> {

	@Override
	protected void onRangeChanged(HasData<InvoiceDTO> display) {
		final int start = 0;
		final int length = display.getVisibleRange().getLength();
		
		ServerFacade.INSTANCE.getInvoiceService().getAllInRange(Configuration.getBusinessId(), start, length, new ManagedAsyncCallback<PageDTO<InvoiceDTO>>() {

			@Override
			public void onSuccess(PageDTO<InvoiceDTO> result) {
				List<InvoiceDTO> items = result.getItems();
				Collections.sort(items, Const.DOCUMENT_COMPARATOR);
				updateRowData(start, items);
			}

		});
	}

}
