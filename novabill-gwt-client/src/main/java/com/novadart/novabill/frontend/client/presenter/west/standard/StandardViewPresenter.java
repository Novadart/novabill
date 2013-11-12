package com.novadart.novabill.frontend.client.presenter.west.standard;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.presenter.AbstractPresenter;
import com.novadart.novabill.frontend.client.view.west.standard.StandardWestView;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class StandardViewPresenter extends AbstractPresenter<StandardWestView> implements StandardWestView.Presenter {

	private SingleSelectionModel<ClientDTO> selectClient = new SingleSelectionModel<ClientDTO>();

	public StandardViewPresenter(PlaceController placeController, EventBus eventBus, StandardWestView view) {
		super(placeController, eventBus, view);
	}

	@Override
	protected void setPresenterInView(StandardWestView view) {
		view.setPresenter(this);
	}


	@Override
	public void onAddClientClicked() {
//		ClientDialog.getInstance(getEventBus()).showCentered();
	}

	@Override
	public void onLoad() {
		getView().getClientSearch().setEventBus(getEventBus());
		getView().getClientList().setSelectionModel(selectClient);
		selectClient.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				ClientPlace cp = new ClientPlace();
				ClientDTO client = selectClient.getSelectedObject();
				cp.setClientId(client.getId());
				goTo(cp);
			}
		});
	}

}
