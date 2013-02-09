package com.novadart.novabill.frontend.client.activity.center;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.place.transportdocument.CloneTransportDocumentPlace;
import com.novadart.novabill.frontend.client.place.transportdocument.ModifyTransportDocumentPlace;
import com.novadart.novabill.frontend.client.place.transportdocument.NewTransportDocumentPlace;
import com.novadart.novabill.frontend.client.place.transportdocument.TransportDocumentPlace;
import com.novadart.novabill.frontend.client.presenter.center.transportdocument.ModifyTransportDocumentPresenter;
import com.novadart.novabill.frontend.client.presenter.center.transportdocument.NewTransportDocumentPresenter;
import com.novadart.novabill.frontend.client.view.center.transportdocument.TransportDocumentView;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public class TransportDocumentActivity extends AbstractCenterActivity {

	private final TransportDocumentPlace place;

	public TransportDocumentActivity(TransportDocumentPlace place, ClientFactory clientFactory) {
		super(clientFactory);
		this.place = place;
	}

	@Override
	public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
		super.start(panel, eventBus);

		getClientFactory().getTransportDocumentView(new AsyncCallback<TransportDocumentView>() {

			@Override
			public void onSuccess(final TransportDocumentView view) {

				if (place instanceof ModifyTransportDocumentPlace) {
					ModifyTransportDocumentPlace p = (ModifyTransportDocumentPlace) place;
					setupModifyTransportDocumentView(panel, view, p);

				} else if (place instanceof CloneTransportDocumentPlace) {
					CloneTransportDocumentPlace p = (CloneTransportDocumentPlace) place;
					setupCloneTransportDocumentView(panel, view, p);

				} else if (place instanceof NewTransportDocumentPlace) {
					NewTransportDocumentPlace p = (NewTransportDocumentPlace) place;
					setupNewTransportDocumentView(panel, view, p);

				} else {
					getClientFactory().getPlaceController().goTo(new HomePlace());
				}

			}

			@Override
			public void onFailure(Throwable caught) {
				manageError();
			}
		});
	}


	private void setupNewTransportDocumentView(final AcceptsOneWidget panel, final TransportDocumentView view, final NewTransportDocumentPlace place){
		ServerFacade.transportDocument.getNextTransportDocId(new DocumentCallack<Long>() {

			@Override
			public void onSuccess(final Long progrId) {
				ServerFacade.client.get(place.getClientId(), new DocumentCallack<ClientDTO>() {

					@Override
					public void onSuccess(ClientDTO client) {
						NewTransportDocumentPresenter p = new NewTransportDocumentPresenter(getClientFactory().getPlaceController(), 
								getClientFactory().getEventBus(), view);
						p.setDataForNewTransportDocument(client, progrId);
						p.go(panel);
					}

				});
			}

		});
	}

	private void setupModifyTransportDocumentView(final AcceptsOneWidget panel, final TransportDocumentView view, ModifyTransportDocumentPlace place){
		ServerFacade.transportDocument.get(place.getTransportDocumentId(), new DocumentCallack<TransportDocumentDTO>() {

			@Override
			public void onSuccess(TransportDocumentDTO result) {
				ModifyTransportDocumentPresenter p = new ModifyTransportDocumentPresenter(getClientFactory().getPlaceController(), 
						getClientFactory().getEventBus(), view);
				p.setData(result);
				p.go(panel);
			}

		});
	}

	private void setupCloneTransportDocumentView(final AcceptsOneWidget panel, final TransportDocumentView view, final CloneTransportDocumentPlace place){
		ServerFacade.transportDocument.getNextTransportDocId(new DocumentCallack<Long>() {

			@Override
			public void onSuccess(final Long progrId) {
				ServerFacade.client.get(place.getClientId(), new DocumentCallack<ClientDTO>() {

					@Override
					public void onSuccess(final ClientDTO client) {
						ServerFacade.transportDocument.get(place.getTransportDocumentId(), new DocumentCallack<TransportDocumentDTO>() {

							@Override
							public void onSuccess(TransportDocumentDTO toClone) {
								NewTransportDocumentPresenter p = new NewTransportDocumentPresenter(getClientFactory().getPlaceController(), 
										getClientFactory().getEventBus(), view);
								p.setDataForNewTransportDocument(client, progrId, toClone);
								p.go(panel);
							}

						});

					}
				});
			}

		});
	}
}
