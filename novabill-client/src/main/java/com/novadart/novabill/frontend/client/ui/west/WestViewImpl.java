package com.novadart.novabill.frontend.client.ui.west;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.Range;
import com.novadart.novabill.frontend.client.datawatcher.DataWatchEvent.DATA;
import com.novadart.novabill.frontend.client.datawatcher.DataWatchEventHandler;
import com.novadart.novabill.frontend.client.datawatcher.DataWatcher;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.ui.center.client.dialog.ClientDialog;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class WestViewImpl extends Composite implements WestView {

	private static WestViewImplUiBinder uiBinder = GWT
			.create(WestViewImplUiBinder.class);

	interface WestViewImplUiBinder extends UiBinder<Widget, WestViewImpl> {
	}
	
	private static final Range CLIENT_LIST_RANGE = new Range(0, 20);
	
	@UiField(provided=true) CellList<ClientDTO> clientList;
	@UiField FlowPanel clientContainer;
	
	private Presenter presenter;

	public WestViewImpl() {
		clientList = createClientList();
		initWidget(uiBinder.createAndBindUi(this));
		setStyleName("WestView");
		
		ClientDataProvider provider = new ClientDataProvider();
		provider.addDataDisplay(clientList);
		
		DataWatcher.getInstance().addDataEventHandler(new DataWatchEventHandler() {
			
			@Override
			public void onDataUpdated(DATA data) {
				if(data.equals(DATA.CLIENT)){
					clientList.setVisibleRangeAndClearData(CLIENT_LIST_RANGE, true);
				}
				
			}
		});
	}
	
	@UiFactory
	I18N getI18N(){
		return I18N.get;
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
