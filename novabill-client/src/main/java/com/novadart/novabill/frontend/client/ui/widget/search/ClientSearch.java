package com.novadart.novabill.frontend.client.ui.widget.search;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.Range;
import com.novadart.novabill.frontend.client.datawatcher.DataWatchEvent.DATA;
import com.novadart.novabill.frontend.client.datawatcher.DataWatchEventHandler;
import com.novadart.novabill.frontend.client.datawatcher.DataWatcher;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.resources.ImageResources;
import com.novadart.novabill.frontend.client.ui.west.ClientDataProvider;
import com.novadart.novabill.frontend.client.ui.west.ClientDataProvider.Watcher;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class ClientSearch implements Watcher {
	
	private static final int QUERY_MIN_LENGHT = 3; 
	private static final int MIN_WAIT_BEFORE_FIRING_SEARCH = 1200;
	private static final Range CLIENT_LIST_RANGE = new Range(0, 20000);
	
	private final Image resetFilter = new Image(ImageResources.INSTANCE.delete().getSafeUri().asString());
	private final Image loader = new Image(ImageResources.INSTANCE.loader().getSafeUri().asString());
	private final Label noClientsFound = new Label(I18N.INSTANCE.noClientsFound());
	
	private final ClientDataProvider clientDataProvider = new ClientDataProvider();
	private final CellList<ClientDTO> dataDisplay;
	private final TextBox filter = new TextBox();
	
	private final SimplePanel clientListWrapper = new SimplePanel();
	
	private boolean searchQueryComplete = true;
	private long lastTimeKeyWasPressedInFilter = 0;
	private final SearchTriggerTimer timer = new SearchTriggerTimer();
	
	public ClientSearch(CellList<ClientDTO> dataDisplay) {
		this.dataDisplay = dataDisplay;
		
		loader.setStyleName("ClientSearch-clientLoader");
		noClientsFound.setStyleName("ClientSearch-noClientsFoundMessage");
		
		clientListWrapper.setStyleName("ClientSearch-clientListWrapper");
		clientListWrapper.setWidget(loader);
		
		filter.setStyleName("ClientSearch-filter");
		filter.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				lastTimeKeyWasPressedInFilter = System.currentTimeMillis();
				
				if(!searchQueryComplete){
					return;
				}
				
				updateClientList(false);
			}
		});
		
		resetFilter.setStyleName("ClientSearch-resetFilter");
		resetFilter.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				filter.setText("");
				filter.setFocus(true);
				updateClientList(false);
			}
		});
		
		
		DataWatcher.getInstance().addDataEventHandler(new DataWatchEventHandler() {
			
			@Override
			public void onDataUpdated(DATA data) {
				if(data.equals(DATA.CLIENT)){
					updateClientList(true);
				}
				
			}
		});
		
		clientDataProvider.setWatcher(this);
		clientDataProvider.addDataDisplay(dataDisplay);
	}

	public Image getResetButton(){
		return resetFilter;
	}
	
	public TextBox getSearchInput(){
		return filter;
	}
	
	public SimplePanel getWrappedClientList(){
		return clientListWrapper;
	}
	
	
	private boolean canFireSearch(){
		return System.currentTimeMillis() - lastTimeKeyWasPressedInFilter > MIN_WAIT_BEFORE_FIRING_SEARCH;
	}
	
	@Override
	public void onServerCallComplete(boolean foundSomething) {
		searchQueryComplete = true;
		
		//it could be that the while we were contacting the server the user has further updated the query
		if(!updateClientList(false)){
			clientListWrapper.setWidget(foundSomething ? dataDisplay : noClientsFound);	
		}
	}
	
	private boolean updateClientList(boolean force){
		
		String prevQuery = clientDataProvider.getQuery();
		String nextQuery = filter.getText().trim();
		String query = null;
		
		if("".equals(prevQuery) && nextQuery.length() >= QUERY_MIN_LENGHT){
			
			query = nextQuery;
			
		} else if(!"".equals(prevQuery) && nextQuery.length() < QUERY_MIN_LENGHT){
			
			query = "";
			
		} else if(nextQuery.length() >= QUERY_MIN_LENGHT && !prevQuery.equalsIgnoreCase(nextQuery)){
			
			query = nextQuery;
			
		} else if(force){
			
			query = nextQuery.length() >= QUERY_MIN_LENGHT ? nextQuery : "";
			
		}
		
		if(query != null){
			
			clientListWrapper.setWidget(loader);
			
			if(canFireSearch() || force){

				searchQueryComplete = false;
				clientDataProvider.setQuery(query);
				dataDisplay.setVisibleRangeAndClearData(CLIENT_LIST_RANGE, true);
				
			} else {
				timer.cancel();
				timer.schedule(MIN_WAIT_BEFORE_FIRING_SEARCH);
			}
			
			return true;
		}
		
		return false;
		
}
	
	private class SearchTriggerTimer extends Timer{

		@Override
		public void run() {
			updateClientList(true);
		}
		
	}

}
