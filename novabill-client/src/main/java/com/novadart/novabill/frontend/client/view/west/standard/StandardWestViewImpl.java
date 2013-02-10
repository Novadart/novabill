package com.novadart.novabill.frontend.client.view.west.standard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.util.WidgetUtils;
import com.novadart.novabill.frontend.client.widget.search.ClientSearch;
import com.novadart.novabill.frontend.client.widget.tip.TipFactory;
import com.novadart.novabill.frontend.client.widget.tip.Tips;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class StandardWestViewImpl extends Composite implements StandardWestView  {
	
	interface ClientSearchStyle extends ClientSearch.Style {}
	interface ClientCellStyle extends ClientCell.Style {}
	
	interface Style extends CssResource {
		String clientListWrapper();
		String clientContainer();
		String clientListContainerWrapper();
		String addClient();
		String clients();
		String clientsHeader();
		String cellList();
	}

	private static StandardWestViewImplUiBinder uiBinder = GWT
			.create(StandardWestViewImplUiBinder.class);

	interface StandardWestViewImplUiBinder extends UiBinder<Widget, StandardWestViewImpl> {
	}

	@UiField FlowPanel clientContainer;

	@UiField HorizontalPanel clientsHeader;
	@UiField HorizontalPanel clientFilterContainer;
	@UiField ScrollPanel clientListContainerWrapper;

	@UiField SimplePanel tip;
	
	@UiField Style s;
	@UiField ClientSearchStyle cs;
	@UiField ClientCellStyle ccs;
	
	private Presenter presenter;
	private final ClientSearch clientSearch;

	public StandardWestViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		
		clientSearch = new ClientSearch(cs, createClientList());
		clientFilterContainer.add(clientSearch.getSearchInput());
		clientFilterContainer.add(clientSearch.getResetButton());
		clientListContainerWrapper.setWidget(clientSearch.getWrappedClientList());

		TipFactory.show(Tips.west_home_no_clients, tip);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		presenter.onLoad();
		
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
		ClientCell cell = new ClientCell(ccs);
		CellList<ClientDTO> list = new CellList<ClientDTO>(cell);
		list.setStyleName(s.cellList());

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
		presenter.onAddClientClicked();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void reset() {}

	@Override
	public ClientSearch getClientSearch() {
		return clientSearch;
	}

}
