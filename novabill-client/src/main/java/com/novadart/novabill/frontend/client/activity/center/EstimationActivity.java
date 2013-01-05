package com.novadart.novabill.frontend.client.activity.center;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.activity.BasicActivity;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.place.estimation.CloneEstimationPlace;
import com.novadart.novabill.frontend.client.place.estimation.EstimationPlace;
import com.novadart.novabill.frontend.client.place.estimation.ModifyEstimationPlace;
import com.novadart.novabill.frontend.client.place.estimation.NewEstimationPlace;
import com.novadart.novabill.frontend.client.view.MainWidget;
import com.novadart.novabill.frontend.client.view.center.EstimationView;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;

public class EstimationActivity extends BasicActivity {

	private final EstimationPlace place;


	public EstimationActivity(EstimationPlace place, ClientFactory clientFactory) {
		super(clientFactory);
		this.place = place;
	}

	@Override
	public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
		getClientFactory().getEstimationView(new AsyncCallback<EstimationView>() {
			
			@Override
			public void onSuccess(final EstimationView view) {
				view.setPresenter(EstimationActivity.this);
				view.setEventBus(eventBus);
				
				if (place instanceof ModifyEstimationPlace) {
					ModifyEstimationPlace p = (ModifyEstimationPlace) place;
					setupModifyEstimationView(panel, view, p);
				
				} else if (place instanceof CloneEstimationPlace) {
					CloneEstimationPlace p = (CloneEstimationPlace) place;
					setupCloneEstimationView(panel, view, p);
					
				} else if (place instanceof NewEstimationPlace) {
					NewEstimationPlace p = (NewEstimationPlace) place;
					setupNewEstimationView(panel, view, p);
					
				} else {
					goTo(new HomePlace());
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}
	
	
	private void setupNewEstimationView(final AcceptsOneWidget panel, final EstimationView view, final NewEstimationPlace place){
		ServerFacade.estimation.getNextEstimationId(new WrappedAsyncCallback<Long>() {

			@Override
			public void onSuccess(final Long progrId) {
				ServerFacade.client.get(place.getClientId(), new WrappedAsyncCallback<ClientDTO>() {

					@Override
					public void onSuccess(ClientDTO client) {
						view.setDataForNewEstimation(client, progrId);
						MainWidget.getInstance().setLargeView();
						panel.setWidget(view);
					}

					@Override
					public void onException(Throwable caught) {
						manageError();
					}
				});
			}

			@Override
			public void onException(Throwable caught) {
				manageError();
			}
		});
	}
	
	private void setupModifyEstimationView(final AcceptsOneWidget panel, final EstimationView view, ModifyEstimationPlace place){
		ServerFacade.estimation.get(place.getEstimationId(), new WrappedAsyncCallback<EstimationDTO>() {

			@Override
			public void onSuccess(EstimationDTO result) {
				view.setEstimation(result);
				MainWidget.getInstance().setLargeView();
				panel.setWidget(view);
			}

			@Override
			public void onException(Throwable caught) {
				manageError();
			}
		});
	}
	
	private void setupCloneEstimationView(final AcceptsOneWidget panel, final EstimationView view, final CloneEstimationPlace place){
		ServerFacade.estimation.getNextEstimationId(new WrappedAsyncCallback<Long>() {

			@Override
			public void onSuccess(final Long progrId) {
				ServerFacade.client.get(place.getClientId(), new WrappedAsyncCallback<ClientDTO>() {

					@Override
					public void onSuccess(final ClientDTO client) {
						ServerFacade.estimation.get(place.getEstimationId(), new WrappedAsyncCallback<EstimationDTO>() {

							@Override
							public void onSuccess(EstimationDTO estimationToClone) {
								view.setDataForNewEstimation(client, progrId, estimationToClone);
								MainWidget.getInstance().setLargeView();
								panel.setWidget(view);
							}

							@Override
							public void onException(Throwable caught) {
								manageError();
							}
						});
						
					}

					@Override
					public void onException(Throwable caught) {
						manageError();
					}
				});
			}

			@Override
			public void onException(Throwable caught) {
				manageError();
			}
		});
	}
	
}
