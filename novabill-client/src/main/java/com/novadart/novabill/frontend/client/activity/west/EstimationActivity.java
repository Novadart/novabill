package com.novadart.novabill.frontend.client.activity.west;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.activity.BasicActivity;
import com.novadart.novabill.frontend.client.facade.AuthAwareAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.EstimationPlace;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.ui.west.WestView;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class EstimationActivity extends BasicActivity {

	private final EstimationPlace place;

	public EstimationActivity(EstimationPlace place, ClientFactory clientFactory) {
		super(clientFactory);
		this.place = place;
	}

	@Override
	public void start(final AcceptsOneWidget panel, EventBus eventBus) {
		final WestView cv = getClientFactory().getWestView();
		cv.setPresenter(this);

		if(place.getEstimationId() == 0){

			if(place.getClient() == null){
			
				goTo(new HomePlace());
			
			} else {
				
				cv.setClient(place.getClient());
				panel.setWidget(cv);
				
			}

		} else {

			ServerFacade.client.getFromEstimationId(place.getEstimationId(), new AuthAwareAsyncCallback<ClientDTO>() {

				@Override
				public void onSuccess(ClientDTO result) {
					cv.setClient(result);
					panel.setWidget(cv);
				}

				@Override
				public void onException(Throwable caught) {
					Window.alert(I18N.INSTANCE.errorServerCommunication());
					goTo(new HomePlace());
				}
			});

		}

	}

}
