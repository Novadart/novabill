package com.novadart.novabill.frontend.client.ui.center.home;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;

public class CreditNoteDataProvider extends AsyncDataProvider<CreditNoteDTO> {

	@Override
	protected void onRangeChanged(HasData<CreditNoteDTO> display) {
		final int start = 0;
		final int length = display.getVisibleRange().getLength();
		
		ServerFacade.creditNote.getAllInRange(start, length, new WrappedAsyncCallback<PageDTO<CreditNoteDTO>>() {

			@Override
			public void onSuccess(PageDTO<CreditNoteDTO> result) {
				updateRowData(start, result.getItems());
			}

			@Override
			public void onException(Throwable caught) {

			}
		});
	}

}
