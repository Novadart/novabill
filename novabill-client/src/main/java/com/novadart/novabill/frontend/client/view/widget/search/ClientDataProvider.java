package com.novadart.novabill.frontend.client.view.widget.search;

import java.util.Collections;
import java.util.List;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.Const;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;

public class ClientDataProvider extends AsyncDataProvider<ClientDTO> {
	
	public static interface Watcher{
		void onServerCallComplete(boolean foundSomething);
	}
	
	private String query = "";
	private Watcher watcher;
	
	public void setQuery(String query) {
		this.query = query;
	}

	public String getQuery() {
		return query;
	}
	
	public void setWatcher(Watcher watcher) {
		this.watcher = watcher;
	}
	
	@Override
	protected void onRangeChanged(HasData<ClientDTO> display) {
		final int start = display.getVisibleRange().getStart();
		
		if(query == null || query.isEmpty()){
			
			ServerFacade.business.getClients(Configuration.getBusinessId(), new WrappedAsyncCallback<List<ClientDTO>>() {
				
				@Override
				public void onSuccess(List<ClientDTO> result) {
					Collections.sort(result, Const.CLIENT_COMPARATOR);
					updateRowData(start, result);
					watcher.onServerCallComplete(result.size()>0);
				}
				
				@Override
				public void onException(Throwable caught) {
					watcher.onServerCallComplete(false);
				}
			});
			
		} else {
			
			ServerFacade.client.searchClients(Configuration.getBusinessId(), query, 0, 99999, new WrappedAsyncCallback<PageDTO<ClientDTO>>() {

				@Override
				public void onSuccess(PageDTO<ClientDTO> result) {
					updateRowData(start, result.getItems());
					watcher.onServerCallComplete(result.getItems().size()>0);
				}

				@Override
				public void onException(Throwable caught) {
					watcher.onServerCallComplete(false);
				}
			});
			
		}
		
        
	}

}
