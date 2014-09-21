package com.novadart.novabill.frontend.client.activity.center;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.place.estimation.CloneEstimationPlace;
import com.novadart.novabill.frontend.client.place.estimation.EstimationPlace;
import com.novadart.novabill.frontend.client.place.estimation.ModifyEstimationPlace;
import com.novadart.novabill.frontend.client.place.estimation.NewEstimationPlace;
import com.novadart.novabill.frontend.client.presenter.center.estimation.ModifyEstimationPresenter;
import com.novadart.novabill.frontend.client.presenter.center.estimation.NewEstimationPresenter;
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.view.center.estimation.EstimationView;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.tuple.Pair;
import com.novadart.novabill.shared.client.tuple.Triple;

public class EstimationActivity extends AbstractCenterActivity {

	private final EstimationPlace place;


	public EstimationActivity(EstimationPlace place, ClientFactory clientFactory, JavaScriptObject callback) {
		super(clientFactory, callback);
		this.place = place;
	}

	@Override
	public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
		super.start(panel, eventBus);

		getClientFactory().getEstimationView(new AsyncCallback<EstimationView>() {

			@Override
			public void onSuccess(final EstimationView view) {
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
					getClientFactory().getPlaceController().goTo(new HomePlace());
				}
			}

			@Override
			public void onFailure(Throwable caught) {

			}
		});
	}


	private void setupNewEstimationView(final AcceptsOneWidget panel, final EstimationView view, final NewEstimationPlace place){
		DocumentUtils.showBusinessDialogIfBusinessInformationNotComplete(new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Void vo) {
				ServerFacade.INSTANCE.getBatchfetcherService().fetchNewEstimationForClientOpData(place.getClientId(), new DocumentCallack<Pair<Long,ClientDTO>>() {

					@Override
					public void onSuccess(Pair<Long, ClientDTO> result) {
						NewEstimationPresenter p = new NewEstimationPresenter(getClientFactory().getPlaceController(), 
								getClientFactory().getEventBus(), view, getCallback());
						p.setDataForNewEstimation(result.getSecond(), result.getFirst());
						p.go(panel);
					}
				});
			}
		});
		
	}

	private void setupModifyEstimationView(final AcceptsOneWidget panel, final EstimationView view, ModifyEstimationPlace place){
		ServerFacade.INSTANCE.getEstimationService().get(place.getEstimationId(), new DocumentCallack<EstimationDTO>() {

			@Override
			public void onSuccess(EstimationDTO result) {
				ModifyEstimationPresenter p = new ModifyEstimationPresenter(getClientFactory().getPlaceController(), 
						getClientFactory().getEventBus(), view, getCallback());
				p.setData(result);
				p.go(panel);
			}
		});
	}

	private void setupCloneEstimationView(final AcceptsOneWidget panel, final EstimationView view, final CloneEstimationPlace place){
		ServerFacade.INSTANCE.getBatchfetcherService().fetchCloneEstimationOpData(place.getEstimationId(), place.getClientId(), 
				new DocumentCallack<Triple<Long,ClientDTO,EstimationDTO>>() {

			@Override
			public void onSuccess(Triple<Long, ClientDTO, EstimationDTO> result) {
				NewEstimationPresenter p = new NewEstimationPresenter(getClientFactory().getPlaceController(), 
						getClientFactory().getEventBus(), view, getCallback());
				p.setDataForNewEstimation(result.getSecond(), result.getFirst(), result.getThird());
				p.go(panel);
			}
		});
	}

}
