package com.novadart.novabill.frontend.client.ui.west;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.Range;
import com.novadart.novabill.frontend.client.datawatcher.DataWatchEvent.DATA;
import com.novadart.novabill.frontend.client.datawatcher.DataWatchEventHandler;
import com.novadart.novabill.frontend.client.datawatcher.DataWatcher;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.resources.ImageResources;
import com.novadart.novabill.frontend.client.ui.center.client.dialog.ClientDialog;
import com.novadart.novabill.frontend.client.ui.west.ClientDataProvider.Watcher;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class WestViewImpl extends Composite implements WestView, Watcher {

	private static WestViewImplUiBinder uiBinder = GWT
			.create(WestViewImplUiBinder.class);

	interface WestViewImplUiBinder extends UiBinder<Widget, WestViewImpl> {
	}
	
	private static final Range CLIENT_LIST_RANGE = new Range(0, 20000);
	private static final int QUERY_MIN_LENGHT = 3; 
	private static final int MIN_WAIT_BEFORE_FIRING_SEARCH = 1200; 
	
	@UiField(provided=true) CellList<ClientDTO> clientList;
	@UiField FlowPanel clientContainer;
	@UiField SimplePanel clientListContainer;
	@UiField TextBox clientFilter;
	
	private boolean searchQueryComplete = true;
	private final ClientDataProvider clientDataProvider = new ClientDataProvider();
	private long lastTimeKeyWasPressedInFilter = 0;
	private final SearchTriggerTimer timer = new SearchTriggerTimer();
	private final Image loader = new Image(ImageResources.INSTANCE.loader().getSafeUri().asString());
	private final Label noClientsFound = new Label(I18N.INSTANCE.noClientsFound());
	
	private Presenter presenter;

	public WestViewImpl() {
		clientList = createClientList();
		initWidget(uiBinder.createAndBindUi(this));
		setStyleName("WestView");
		
		clientDataProvider.setWatcher(this);
		clientDataProvider.addDataDisplay(clientList);
		
		loader.setStyleName("clientLoader");
		noClientsFound.setStyleName("noClientsFoundMessage");
		clientListContainer.setWidget(loader);
		
		DataWatcher.getInstance().addDataEventHandler(new DataWatchEventHandler() {
			
			@Override
			public void onDataUpdated(DATA data) {
				if(data.equals(DATA.CLIENT)){
					updateClientList(true);
				}
				
			}
		});
	}
	
	@UiFactory
	I18N getI18N(){
		return I18N.INSTANCE;
	}
	
	private CellList<ClientDTO> createClientList(){
		SimpleClientCell cell = new SimpleClientCell();
		CellList<ClientDTO> list = new CellList<ClientDTO>(cell);
		list.setStyleName("cellList");
		list.setVisibleRange(CLIENT_LIST_RANGE);
		
		cell.setHandler(new SimpleClientCell.Handler() {
			
			@Override
			public void onClientSelected(ClientDTO client) {
				ClientPlace cp = new ClientPlace();
				cp.setClientId(client.getId());
				presenter.goTo(cp);
			}
		});
		
		return list;
	}

	@UiHandler("clientFilter")
	void onFilterKeyUp(KeyUpEvent e){
		lastTimeKeyWasPressedInFilter = System.currentTimeMillis();
		
		if(!searchQueryComplete){
			return;
		}
		
		updateClientList(false);
	}
	
	
	private boolean canFireSearch(){
		return System.currentTimeMillis() - lastTimeKeyWasPressedInFilter > MIN_WAIT_BEFORE_FIRING_SEARCH;
	}
	
	@Override
	public void onServerCallComplete(boolean foundSomething) {
		searchQueryComplete = true;
		
		//it could be that the while we were contacting the server the user has further updated the query
		if(!updateClientList(false)){
			clientListContainer.setWidget(foundSomething ? clientList : noClientsFound);	
		}
	}
	
	
	private boolean updateClientList(boolean force){
			
			String prevQuery = clientDataProvider.getQuery();
			String nextQuery = clientFilter.getText().trim();
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
				
				clientListContainer.setWidget(loader);
				
				if(canFireSearch() || force){

					searchQueryComplete = false;
					clientDataProvider.setQuery(query);
					clientList.setVisibleRangeAndClearData(CLIENT_LIST_RANGE, true);
					
				} else {
					timer.cancel();
					timer.schedule(MIN_WAIT_BEFORE_FIRING_SEARCH);
				}
				
				return true;
			}
			
			return false;
			
	}
	
	
	@UiHandler("addClient")
	void onAddClientClicked(ClickEvent e){
		ClientDialog.getInstance().showCentered();
	}
	
	@UiHandler("cleanClientFilter")
	void onCleanClientFilterClicked(ClickEvent e){
		clientFilter.setText("");
		clientFilter.setFocus(true);
		updateClientList(false);
	}
	
	@Override
	public void setClient(ClientDTO client) {
		clientContainer.setVisible(false);
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setClean() {
		clientContainer.setVisible(true);
	}

	
	private class SearchTriggerTimer extends Timer{

		@Override
		public void run() {
			updateClientList(true);
		}
		
	}

}
