package com.novadart.novabill.frontend.client.widget.search;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.Range;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.gwtshared.client.textbox.RichTextBox;
import com.novadart.novabill.frontend.client.event.ClientAddEvent;
import com.novadart.novabill.frontend.client.event.ClientAddHandler;
import com.novadart.novabill.frontend.client.event.ClientDeleteEvent;
import com.novadart.novabill.frontend.client.event.ClientDeleteHandler;
import com.novadart.novabill.frontend.client.event.ClientUpdateEvent;
import com.novadart.novabill.frontend.client.event.ClientUpdateHandler;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.resources.ImageResources;
import com.novadart.novabill.frontend.client.widget.search.ClientDataProvider.Watcher;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class ClientSearch implements Watcher {
	
	public interface Style extends CssResource {
		String clientLoader();
		String noClientsFoundMessage();
		String clientListWrapper();
		String filter();
		String resetFilter();
	}

	private static final int QUERY_MIN_LENGHT = 1; 
	private static final int MIN_WAIT_BEFORE_FIRING_SEARCH = 1200;
	private static final Range CLIENT_LIST_RANGE = new Range(0, 20000);

	private final Image resetFilter = new Image(ImageResources.INSTANCE.clear_left().getSafeUri().asString());
	private final Image loader = new Image(ImageResources.INSTANCE.loader().getSafeUri().asString());
	private final Label noClientsFound = new Label(I18N.INSTANCE.noClientsFound());

	private final ClientDataProvider clientDataProvider = new ClientDataProvider();
	private final CellList<ClientDTO> dataDisplay;
	private final RichTextBox filter = new RichTextBox(GlobalBundle.INSTANCE.richTextBoxCss(), I18N.INSTANCE.helpSearchClient());

	private final SimplePanel clientListWrapper = new SimplePanel();

	private boolean searchQueryComplete = true;
	private long lastTimeKeyWasPressedInFilter = 0;
	private final SearchTriggerTimer timer = new SearchTriggerTimer();
	
	public ClientSearch(Style style, CellList<ClientDTO> dataDisplay) {
		style.ensureInjected();
		this.dataDisplay = dataDisplay;

		loader.setStyleName(style.clientLoader());
		noClientsFound.setStyleName(style.noClientsFoundMessage());

		clientListWrapper.setStyleName(style.clientListWrapper());
		clientListWrapper.setWidget(loader);

		filter.addStyleName(style.filter());
		filter.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				lastTimeKeyWasPressedInFilter = System.currentTimeMillis();

				resetFilter.setVisible(filter.getText().length() > 0);

				if(!searchQueryComplete){
					return;
				}

				updateClientList(false);
			}
		});

		resetFilter.addStyleName(style.resetFilter());
		resetFilter.setVisible(false);
		resetFilter.setTitle(I18N.INSTANCE.helpClearFilter());
		resetFilter.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				filter.setText("");
				resetFilter.setVisible(false);
				filter.setFocus(true);
				updateClientList(false);
			}
		});

		clientDataProvider.setWatcher(this);
		clientDataProvider.addDataDisplay(dataDisplay);
	}

	public void setEventBus(EventBus eventBus){
		eventBus.addHandler(ClientUpdateEvent.TYPE, new ClientUpdateHandler() {

			@Override
			public void onClientUpdate(ClientUpdateEvent event) {
				updateClientList(true);
			}
		});
		eventBus.addHandler(ClientAddEvent.TYPE, new ClientAddHandler() {

			@Override
			public void onClientAdd(ClientAddEvent event) {
				updateClientList(true);
			}
		});
		eventBus.addHandler(ClientDeleteEvent.TYPE, new ClientDeleteHandler() {

			@Override
			public void onClientDelete(ClientDeleteEvent event) {
				updateClientList(true);
			}
		});
	}

	public Image getResetButton(){
		return resetFilter;
	}

	public RichTextBox getSearchInput(){
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
