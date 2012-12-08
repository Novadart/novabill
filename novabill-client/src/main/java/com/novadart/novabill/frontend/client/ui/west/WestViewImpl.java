package com.novadart.novabill.frontend.client.ui.west;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.gwtshared.client.textbox.RichTextBox;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.datawatcher.DataWatchEventHandler;
import com.novadart.novabill.frontend.client.datawatcher.DataWatcher;
import com.novadart.novabill.frontend.client.datawatcher.DataWatchEvent.DATA;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.ui.center.client.dialog.ClientDialog;
import com.novadart.novabill.frontend.client.ui.widget.search.ClientSearch;
import com.novadart.novabill.frontend.client.util.WidgetUtils;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class WestViewImpl extends Composite implements WestView  {

	private static WestViewImplUiBinder uiBinder = GWT
			.create(WestViewImplUiBinder.class);

	interface WestViewImplUiBinder extends UiBinder<Widget, WestViewImpl> {
	}
	
	@UiField FlowPanel clientContainer;
	@UiField(provided=true) SimplePanel clientListContainer;
	@UiField(provided=true) RichTextBox clientFilter;
	@UiField(provided=true) Image cleanClientFilter;
	
	@UiField HorizontalPanel clientsHeader;
	@UiField HorizontalPanel clientFilterContainer;
	@UiField ScrollPanel clientListContainerWrapper;
	
	@UiField Label welcomeMessage;
	
	private Presenter presenter;
	private final ClientSearch clientSearch;
	
	public WestViewImpl() {
		clientSearch = new ClientSearch(createClientList());
		clientListContainer = clientSearch.getWrappedClientList();
		clientFilter = clientSearch.getSearchInput();
		cleanClientFilter = clientSearch.getResetButton();
		initWidget(uiBinder.createAndBindUi(this));
		setStyleName("WestView");
		
		DataWatcher.getInstance().addDataEventHandler(new DataWatchEventHandler() {
			
			@Override
			public void onDataUpdated(DATA data) {
				switch (data) {
				case STATS:
					ServerFacade.business.getStats(Configuration.getBusinessId(), new WrappedAsyncCallback<BusinessStatsDTO>() {

						@Override
						public void onSuccess(BusinessStatsDTO result) {
							if(result == null){
								return;
							}
							if(result.getClientsCount() > 0){
								welcomeMessage.setVisible(false);
								clientListContainer.setVisible(true);
								clientFilterContainer.setVisible(true);
							}
						}

						@Override
						public void onException(Throwable caught) {
						}
					});
					break;

				default:
					break;
				}
				
			}
		});
	}
	
	
	@Override
	protected void onLoad() {
		super.onLoad();
		if(Configuration.getStats().getClientsCount() == 0){
			welcomeMessage.setVisible(true);
			clientListContainer.setVisible(false);
			clientFilterContainer.setVisible(false);
		}
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
		    @Override
		    public void execute() {
		    	WidgetUtils.setElementHeightToFillSpace(clientListContainerWrapper.getElement(), 
						clientContainer.getElement(), 
						clientsHeader.getElement(), clientFilterContainer.getElement());
		    }
		  });
	}
	
	@UiFactory
	I18N getI18N(){
		return I18N.INSTANCE;
	}
	
	private CellList<ClientDTO> createClientList(){
		ClientCell cell = new ClientCell();
		CellList<ClientDTO> list = new CellList<ClientDTO>(cell);
		list.setStyleName("cellList");
		
		cell.setHandler(new ClientCell.Handler() {
			
			@Override
			public void onClientSelected(ClientDTO client) {
				ClientPlace cp = new ClientPlace();
				cp.setClientId(client.getId());
				presenter.goTo(cp);
			}
		});
		
		return list;
	}

	@UiHandler("addClient")
	void onAddClientClicked(ClickEvent e){
		ClientDialog.getInstance().showCentered();
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

}
