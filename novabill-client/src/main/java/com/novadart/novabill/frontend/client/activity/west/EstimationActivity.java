package com.novadart.novabill.frontend.client.activity.west;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.activity.BasicActivity;
import com.novadart.novabill.frontend.client.place.EstimationPlace;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.ui.west.WestView;

public class EstimationActivity extends BasicActivity {

	private final EstimationPlace place;

	public EstimationActivity(EstimationPlace place, ClientFactory clientFactory) {
		super(clientFactory);
		this.place = place;
	}

	@Override
	public void start(final AcceptsOneWidget panel, EventBus eventBus) {
		getClientFactory().getWestView(new AsyncCallback<WestView>() {

			@Override
			public void onSuccess(final WestView wv) {
				wv.setPresenter(EstimationActivity.this);
				
				if(place.getEstimationId() == 0){

					if(place.getClient() == null){
					
						goTo(new HomePlace());
					
					} else {
						
						wv.setClient(place.getClient());
						panel.setWidget(wv);
						
					}

				} else {
					
					panel.setWidget(wv);
					
				}

			}

			@Override
			public void onFailure(Throwable caught) {

			}
		});
	}

}
