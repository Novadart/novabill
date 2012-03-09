package com.novadart.novabill.frontend.client.ui.west;

import java.util.List;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.novadart.novabill.frontend.client.facade.AuthAwareAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class ClientDataProvider extends AsyncDataProvider<ClientDTO> {

	@Override
	protected void onRangeChanged(HasData<ClientDTO> display) {
		final int start = display.getVisibleRange().getStart();
		
        ServerFacade.client.getAll(new AuthAwareAsyncCallback<List<ClientDTO>>() {
			
			@Override
			public void onSuccess(List<ClientDTO> result) {
				updateRowData(start, result);
			}
			
			@Override
			public void onException(Throwable caught) {
				
			}
		});
	}

}
