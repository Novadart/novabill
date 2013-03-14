package com.novadart.novabill.frontend.client.activity.center;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.place.invoice.CloneInvoicePlace;
import com.novadart.novabill.frontend.client.place.invoice.FromEstimationInvoicePlace;
import com.novadart.novabill.frontend.client.place.invoice.FromTransportDocumentInvoicePlace;
import com.novadart.novabill.frontend.client.place.invoice.InvoicePlace;
import com.novadart.novabill.frontend.client.place.invoice.ModifyInvoicePlace;
import com.novadart.novabill.frontend.client.place.invoice.NewInvoicePlace;
import com.novadart.novabill.frontend.client.presenter.center.invoice.ModifyInvoicePresenter;
import com.novadart.novabill.frontend.client.presenter.center.invoice.NewInvoicePresenter;
import com.novadart.novabill.frontend.client.view.center.invoice.InvoiceView;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.tuple.Pair;
import com.novadart.novabill.shared.client.tuple.Triple;

public class InvoiceActivity extends AbstractCenterActivity {

	private final InvoicePlace place;


	public InvoiceActivity(InvoicePlace place, ClientFactory clientFactory) {
		super(clientFactory);
		this.place = place;
	}

	@Override
	public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
		super.start(panel, eventBus);

		getClientFactory().getInvoiceView(new AsyncCallback<InvoiceView>() {

			@Override
			public void onSuccess(final InvoiceView view) {

				if (place instanceof ModifyInvoicePlace) {
					ModifyInvoicePlace p = (ModifyInvoicePlace) place;
					setupModifyInvoiceView(panel, view, p);

				} else if (place instanceof CloneInvoicePlace) {
					CloneInvoicePlace p = (CloneInvoicePlace) place;
					setupCloneInvoiceView(panel, view, p);

				} else if (place instanceof FromEstimationInvoicePlace) {
					FromEstimationInvoicePlace p = (FromEstimationInvoicePlace) place;
					setupFromEstimationInvoiceView(panel, view, p);

				} else if (place instanceof FromTransportDocumentInvoicePlace) {
					FromTransportDocumentInvoicePlace p = (FromTransportDocumentInvoicePlace) place;
					setupFromTransportDocumentInvoiceView(panel, view, p);

				} else if (place instanceof NewInvoicePlace) {
					NewInvoicePlace p = (NewInvoicePlace) place;
					setupNewInvoiceView(panel, view, p);

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

	private void setupNewInvoiceView(final AcceptsOneWidget panel, final InvoiceView view, final NewInvoicePlace place){
		ServerFacade.batchFetcher.fetchNewInvoiceForClientOpData(place.getClientId(), new DocumentCallack<Pair<Long,ClientDTO>>() {

			@Override
			public void onSuccess(Pair<Long, ClientDTO> result) {
				NewInvoicePresenter p = new NewInvoicePresenter(getClientFactory().getPlaceController(), 
						getClientFactory().getEventBus(), view);
				p.setDataForNewInvoice(result.getSecond(), result.getFirst());
				p.go(panel);
			}
		});
	}

	private void setupFromEstimationInvoiceView(final AcceptsOneWidget panel, final InvoiceView view, final FromEstimationInvoicePlace place){
		ServerFacade.batchFetcher.fetchNewInvoiceFromEstimationOpData(place.getEstimationId(), new DocumentCallack<Pair<Long,EstimationDTO>>(){

			@Override
			public void onSuccess(Pair<Long, EstimationDTO> result) {
				NewInvoicePresenter p = new NewInvoicePresenter(getClientFactory().getPlaceController(), 
						getClientFactory().getEventBus(), view);
				p.setDataForNewInvoice(result.getFirst(), result.getSecond());
				p.go(panel);
			}
			
		});
	}

	private void setupFromTransportDocumentInvoiceView(final AcceptsOneWidget panel, final InvoiceView view, 
			final FromTransportDocumentInvoicePlace place){
		ServerFacade.batchFetcher.fetchNewInvoiceFromTransportDocumentOpData(place.getTransportDocumentId(), 
				new DocumentCallack<Pair<Long,TransportDocumentDTO>>() {

					@Override
					public void onSuccess(
							Pair<Long, TransportDocumentDTO> result) {
						NewInvoicePresenter p = new NewInvoicePresenter(getClientFactory().getPlaceController(), 
								getClientFactory().getEventBus(), view);
						p.setDataForNewInvoice(result.getFirst(), result.getSecond());
						p.go(panel);
					}
		});
	}


	private void setupModifyInvoiceView(final AcceptsOneWidget panel, final InvoiceView view, ModifyInvoicePlace place){
		ServerFacade.invoice.get(place.getInvoiceId(), new DocumentCallack<InvoiceDTO>() {

			@Override
			public void onSuccess(InvoiceDTO result) {
				ModifyInvoicePresenter p = new ModifyInvoicePresenter(getClientFactory().getPlaceController(), 
						getClientFactory().getEventBus(), view);
				p.setData(result);
				p.go(panel);
			}

		});
	}

	private void setupCloneInvoiceView(final AcceptsOneWidget panel, final InvoiceView view, final CloneInvoicePlace place){
		ServerFacade.batchFetcher.fetchCloneInvoiceOpData(place.getInvoiceId(), place.getClientId(), 
				new DocumentCallack<Triple<Long,ClientDTO,InvoiceDTO>>() {

					@Override
					public void onSuccess(
							Triple<Long, ClientDTO, InvoiceDTO> result) {
						NewInvoicePresenter p = new NewInvoicePresenter(getClientFactory().getPlaceController(), 
								getClientFactory().getEventBus(), view);
						p.setDataForNewInvoice(result.getSecond(), result.getFirst(), result.getThird());
						p.go(panel);
					}
		});
	}

}
