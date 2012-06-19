package com.novadart.novabill.frontend.client.ui.widget.search;

import java.util.List;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.shared.client.dto.ClientDTO;

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
			
			ServerFacade.client.getAll(new WrappedAsyncCallback<List<ClientDTO>>() {
				
				@Override
				public void onSuccess(List<ClientDTO> result) {
					updateRowData(start, result);
					watcher.onServerCallComplete(result.size()>0);
				}
				
				@Override
				public void onException(Throwable caught) {
					watcher.onServerCallComplete(false);
				}
			});
			
		} else {
			
			ServerFacade.client.searchClients(query, new WrappedAsyncCallback<List<ClientDTO>>() {

				@Override
				public void onSuccess(List<ClientDTO> result) {
					updateRowData(start, result);
					watcher.onServerCallComplete(result.size()>0);
				}

				@Override
				public void onException(Throwable caught) {
					watcher.onServerCallComplete(false);
				}
			});
			
		}
		
        
	}

}