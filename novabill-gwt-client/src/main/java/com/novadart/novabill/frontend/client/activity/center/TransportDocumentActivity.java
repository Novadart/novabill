package com.novadart.novabill.frontend.client.activity.center;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.bridge.BridgeUtils;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.place.transportdocument.CloneTransportDocumentPlace;
import com.novadart.novabill.frontend.client.place.transportdocument.FromEstimationTransportDocumentPlace;
import com.novadart.novabill.frontend.client.place.transportdocument.ModifyTransportDocumentPlace;
import com.novadart.novabill.frontend.client.place.transportdocument.NewTransportDocumentPlace;
import com.novadart.novabill.frontend.client.place.transportdocument.TransportDocumentPlace;
import com.novadart.novabill.frontend.client.presenter.center.transportdocument.ModifyTransportDocumentPresenter;
import com.novadart.novabill.frontend.client.presenter.center.transportdocument.NewTransportDocumentPresenter;
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.view.center.transportdocument.TransportDocumentView;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.tuple.Pair;
import com.novadart.novabill.shared.client.tuple.Triple;

public class TransportDocumentActivity extends AbstractCenterActivity {

	private final TransportDocumentPlace place;

	public TransportDocumentActivity(TransportDocumentPlace place, ClientFactory clientFactory, JavaScriptObject callback) {
		super(clientFactory, callback);
		this.place = place;
	}

	@Override
	public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
		super.start(panel, eventBus);
		

		if (place instanceof ModifyTransportDocumentPlace) {
			ModifyTransportDocumentPlace p = (ModifyTransportDocumentPlace) place;
			setupModifyTransportDocumentView(panel, p);

		} else {

			getClientFactory().getTransportDocumentView(false, new AsyncCallback<TransportDocumentView>() {
	
				@Override
				public void onSuccess(final TransportDocumentView view) {
					if (place instanceof CloneTransportDocumentPlace) {
						CloneTransportDocumentPlace p = (CloneTransportDocumentPlace) place;
						setupCloneTransportDocumentView(panel, view, p);
	
					} else if (place instanceof FromEstimationTransportDocumentPlace) {
						FromEstimationTransportDocumentPlace p = (FromEstimationTransportDocumentPlace) place;
						setupFromEstimationTransportDocumentView(panel, view, p);
	
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
	}


	private void setupNewTransportDocumentView(final AcceptsOneWidget panel, final TransportDocumentView view, final NewTransportDocumentPlace place){
		ServerFacade.INSTANCE.getBatchfetcherService().fetchNewTransportDocumentForClientOpData(place.getClientId(), new DocumentCallack<Pair<Long,ClientDTO>>() {

			@Override
			public void onSuccess(final Pair<Long, ClientDTO> result) {
				DocumentUtils.showClientDialogIfClientInformationNotComplete(result.getSecond(), new AsyncCallback<ClientDTO>() {

					@Override
					public void onFailure(Throwable caught) {
						BridgeUtils.invokeJSCallbackOnException(caught.getClass().getName(), "", getCallback());
					}

					@Override
					public void onSuccess(ClientDTO newClient) {
						NewTransportDocumentPresenter p = new NewTransportDocumentPresenter(getClientFactory().getPlaceController(), 
								getClientFactory().getEventBus(), view, getCallback());
						p.setDataForNewTransportDocument(newClient, result.getFirst());
						p.go(panel);
						
					}
				});
			}
		});
	}
	
	
	private void setupFromEstimationTransportDocumentView(final AcceptsOneWidget panel, final TransportDocumentView view, final FromEstimationTransportDocumentPlace place){
		ServerFacade.INSTANCE.getBatchfetcherService().fetchNewTransportDocumentFromEstimationOpData(place.getEstimationId(), new DocumentCallack<Pair<Long,EstimationDTO>>() {

			@Override
			public void onSuccess(final Pair<Long, EstimationDTO> result) {
				DocumentUtils.showClientDialogIfClientInformationNotComplete(result.getSecond().getClient(), new AsyncCallback<ClientDTO>() {

					@Override
					public void onFailure(Throwable caught) {
						BridgeUtils.invokeJSCallbackOnException(caught.getClass().getName(), "", getCallback());
					}

					@Override
					public void onSuccess(final ClientDTO newClient) {
						result.getSecond().setClient(newClient);
						NewTransportDocumentPresenter p = new NewTransportDocumentPresenter(getClientFactory().getPlaceController(), 
								getClientFactory().getEventBus(), view, getCallback());
						p.setDataForNewTransportDocument(newClient, result.getFirst(), result.getSecond());
						p.go(panel);
					}
				});
			}
		});
	}
	

	private void setupModifyTransportDocumentView(final AcceptsOneWidget panel, ModifyTransportDocumentPlace place){
		ServerFacade.INSTANCE.getTransportdocumentService().get(place.getTransportDocumentId(), new DocumentCallack<TransportDocumentDTO>() {

			@Override
			public void onSuccess(final TransportDocumentDTO result) {
				
				getClientFactory().getTransportDocumentView(result.getInvoice()!=null, new AsyncCallback<TransportDocumentView>() {
					
					@Override
					public void onSuccess(final TransportDocumentView view) {
						ModifyTransportDocumentPresenter p = new ModifyTransportDocumentPresenter(getClientFactory().getPlaceController(), 
								getClientFactory().getEventBus(), view, getCallback());
						p.setData(result);
						p.go(panel);
					}
		
					@Override
					public void onFailure(Throwable caught) {
						manageError();
					}
				});
				
			}

		});
	}

	private void setupCloneTransportDocumentView(final AcceptsOneWidget panel, final TransportDocumentView view, final CloneTransportDocumentPlace place){
		ServerFacade.INSTANCE.getBatchfetcherService().fetchCloneTransportDocumentOpData(place.getTransportDocumentId(), place.getClientId(), 
				new DocumentCallack<Triple<Long,ClientDTO,TransportDocumentDTO>>() {

					@Override
					public void onSuccess(final Triple<Long, ClientDTO, TransportDocumentDTO> result) {
						DocumentUtils.showClientDialogIfClientInformationNotComplete(result.getSecond(), new AsyncCallback<ClientDTO>() {

							@Override
							public void onFailure(Throwable caught) {
								BridgeUtils.invokeJSCallbackOnException(caught.getClass().getName(), "", getCallback());
							}

							@Override
							public void onSuccess(ClientDTO newClient) {
								NewTransportDocumentPresenter p = new NewTransportDocumentPresenter(getClientFactory().getPlaceController(), 
										getClientFactory().getEventBus(), view, getCallback());
								p.setDataForNewTransportDocument(newClient, result.getFirst(), result.getThird());
								p.go(panel);
								
							}
						});
					}
		});
	}
}
