package com.novadart.novabill.frontend.client.activity.center;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.activity.BasicActivity;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.EstimationPlace;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.ui.center.InvoiceView;
import com.novadart.novabill.frontend.client.ui.widget.notification.Notification;
import com.novadart.novabill.shared.client.dto.EstimationDTO;

public class EstimationActivity extends BasicActivity {

	private final EstimationPlace estimationPlace;


	public EstimationActivity(EstimationPlace place, ClientFactory clientFactory) {
		super(clientFactory);
		this.estimationPlace = place;
	}

	@Override
	public void start(final AcceptsOneWidget panel, EventBus eventBus) {
		getClientFactory().getInvoiceView(new AsyncCallback<InvoiceView>() {
			
			@Override
			public void onSuccess(final InvoiceView iv) {
				iv.setPresenter(EstimationActivity.this);

				if(estimationPlace.getEstimationId() == 0){ //we're creating a new invoice

					if(estimationPlace.getClient() == null){
						
						goTo(new HomePlace());
						
					} else {
						
						iv.setDataForNewEstimation(estimationPlace.getClient());
						panel.setWidget(iv);
						
					}

				} else {

					ServerFacade.estimation.get(estimationPlace.getEstimationId(), new WrappedAsyncCallback<EstimationDTO>() {

						@Override
						public void onException(Throwable caught) {
							Notification.showMessage(I18N.INSTANCE.errorServerCommunication());
							goTo(new HomePlace());
						}

						@Override
						public void onSuccess(EstimationDTO result) {
							iv.setEstimation(result);
							panel.setWidget(iv);
						}
					});

				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}

}
