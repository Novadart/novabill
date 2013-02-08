package com.novadart.novabill.frontend.client.activity.center;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.facade.CallbackUtils;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.place.invoice.CloneInvoicePlace;
import com.novadart.novabill.frontend.client.place.invoice.FromEstimationInvoicePlace;
import com.novadart.novabill.frontend.client.place.invoice.FromTransportDocumentInvoicePlace;
import com.novadart.novabill.frontend.client.place.invoice.InvoicePlace;
import com.novadart.novabill.frontend.client.place.invoice.ModifyInvoicePlace;
import com.novadart.novabill.frontend.client.place.invoice.NewInvoicePlace;
import com.novadart.novabill.frontend.client.view.MainWidget;
import com.novadart.novabill.frontend.client.view.center.invoice.InvoiceView;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

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
				view.setPresenter(InvoiceActivity.this);
				view.setEventBus(eventBus);

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
					goTo(new HomePlace());
				}


			}

			@Override
			public void onFailure(Throwable caught) {
				manageError();
			}
		});
	}

	private void setupNewInvoiceView(final AcceptsOneWidget panel, final InvoiceView view, final NewInvoicePlace place){
		ServerFacade.invoice.getNextInvoiceDocumentID(new ManagedAsyncCallback<Long>() {

			@Override
			public void onSuccess(final Long progrId) {
				ServerFacade.client.get(place.getClientId(), new ManagedAsyncCallback<ClientDTO>() {

					@Override
					public void onSuccess(ClientDTO client) {
						view.setDataForNewInvoice(client, progrId);
						MainWidget.getInstance().setLargeView();
						panel.setWidget(view);
					}

					@Override
					public void onFailure(Throwable caught) {
						if(CallbackUtils.isServerCommunicationException(caught)){
							manageError();
						} else {
							super.onFailure(caught);
						}
					}
				});
			}

			@Override
			public void onFailure(Throwable caught) {
				if(CallbackUtils.isServerCommunicationException(caught)){
					manageError();
				} else {
					super.onFailure(caught);
				}
			}
		});
	}

	private void setupFromEstimationInvoiceView(final AcceptsOneWidget panel, final InvoiceView view, final FromEstimationInvoicePlace place){
		ServerFacade.invoice.getNextInvoiceDocumentID(new ManagedAsyncCallback<Long>() {

			@Override
			public void onSuccess(final Long progrId) {
				ServerFacade.estimation.get(place.getEstimationId(), new ManagedAsyncCallback<EstimationDTO>() {

					@Override
					public void onSuccess(EstimationDTO result) {
						view.setDataForNewInvoice(progrId, result);
						MainWidget.getInstance().setLargeView();
						panel.setWidget(view);
					}

					@Override
					public void onFailure(Throwable caught) {
						if(CallbackUtils.isServerCommunicationException(caught)){
							manageError();
						} else {
							super.onFailure(caught);
						}
					}
				});
			}

			@Override
			public void onFailure(Throwable caught) {
				if(CallbackUtils.isServerCommunicationException(caught)){
					manageError();
				} else {
					super.onFailure(caught);
				}
			}
		});
	}

	private void setupFromTransportDocumentInvoiceView(final AcceptsOneWidget panel, final InvoiceView view, 
			final FromTransportDocumentInvoicePlace place){
		ServerFacade.invoice.getNextInvoiceDocumentID(new ManagedAsyncCallback<Long>() {

			@Override
			public void onSuccess(final Long progrId) {
				ServerFacade.transportDocument.get(place.getTransportDocumentId(), new ManagedAsyncCallback<TransportDocumentDTO>() {

					@Override
					public void onSuccess(TransportDocumentDTO result) {
						view.setDataForNewInvoice(progrId, result);
						MainWidget.getInstance().setLargeView();
						panel.setWidget(view);
					}

					@Override
					public void onFailure(Throwable caught) {
						if(CallbackUtils.isServerCommunicationException(caught)){
							manageError();
						} else {
							super.onFailure(caught);
						}
					}
				});
			}

			@Override
			public void onFailure(Throwable caught) {
				if(CallbackUtils.isServerCommunicationException(caught)){
					manageError();
				} else {
					super.onFailure(caught);
				}
			}
		});
	}


	private void setupModifyInvoiceView(final AcceptsOneWidget panel, final InvoiceView view, ModifyInvoicePlace place){
		ServerFacade.invoice.get(place.getInvoiceId(), new ManagedAsyncCallback<InvoiceDTO>() {

			@Override
			public void onSuccess(InvoiceDTO result) {
				view.setInvoice(result);
				MainWidget.getInstance().setLargeView();
				panel.setWidget(view);
			}

			@Override
			public void onFailure(Throwable caught) {
				if(CallbackUtils.isServerCommunicationException(caught)){
					manageError();
				} else {
					super.onFailure(caught);
				}
			}
		});
	}

	private void setupCloneInvoiceView(final AcceptsOneWidget panel, final InvoiceView view, final CloneInvoicePlace place){
		ServerFacade.invoice.getNextInvoiceDocumentID(new ManagedAsyncCallback<Long>() {

			@Override
			public void onSuccess(final Long progrId) {
				ServerFacade.client.get(place.getClientId(), new ManagedAsyncCallback<ClientDTO>() {

					@Override
					public void onSuccess(final ClientDTO client) {
						ServerFacade.invoice.get(place.getInvoiceId(), new ManagedAsyncCallback<InvoiceDTO>() {

							@Override
							public void onSuccess(InvoiceDTO invoiceToClone) {
								view.setDataForNewInvoice(client, progrId, invoiceToClone);
								MainWidget.getInstance().setLargeView();
								panel.setWidget(view);
							}

							@Override
							public void onFailure(Throwable caught) {
								if(CallbackUtils.isServerCommunicationException(caught)){
									manageError();
								} else {
									super.onFailure(caught);
								}
							}
						});

					}

					@Override
					public void onFailure(Throwable caught) {
						if(CallbackUtils.isServerCommunicationException(caught)){
							manageError();
						} else {
							super.onFailure(caught);
						}
					}
				});
			}

			@Override
			public void onFailure(Throwable caught) {
				if(CallbackUtils.isServerCommunicationException(caught)){
					manageError();
				} else {
					super.onFailure(caught);
				}
			}
		});
	}

}
