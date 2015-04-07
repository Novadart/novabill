package com.novadart.novabill.frontend.client.activity.center;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.bridge.BridgeUtils;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.place.invoice.*;
import com.novadart.novabill.frontend.client.presenter.center.invoice.ModifyInvoicePresenter;
import com.novadart.novabill.frontend.client.presenter.center.invoice.NewInvoicePresenter;
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.view.center.invoice.InvoiceView;
import com.novadart.novabill.shared.client.dto.*;
import com.novadart.novabill.shared.client.tuple.Triple;

import java.util.List;

public class InvoiceActivity extends AbstractCenterActivity {

	private abstract class InvoiceViewCallback implements AsyncCallback<InvoiceView> {
		@Override
		public void onFailure(Throwable caught) {
			manageError();
		}
	} 

	private final InvoicePlace place;


	public InvoiceActivity(InvoicePlace place, ClientFactory clientFactory, JavaScriptObject callback) {
		super(clientFactory, callback);
		this.place = place;
	}

	@Override
	public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
		super.start(panel, eventBus);


		if (place instanceof ModifyInvoicePlace) {
			ModifyInvoicePlace p = (ModifyInvoicePlace) place;
			setupModifyInvoiceView(panel, p);

		} else if (place instanceof CloneInvoicePlace) {
			CloneInvoicePlace p = (CloneInvoicePlace) place;
			setupCloneInvoiceView(panel, p);

		} else if (place instanceof FromEstimationInvoicePlace) {
			FromEstimationInvoicePlace p = (FromEstimationInvoicePlace) place;
			setupFromEstimationInvoiceView(panel, p);

		} else if (place instanceof FromTransportDocumentListInvoicePlace) {
			FromTransportDocumentListInvoicePlace p = (FromTransportDocumentListInvoicePlace) place;
			setupFromTransportDocumentListInvoiceView(panel, p);

		} else if (place instanceof NewInvoicePlace) {
			NewInvoicePlace p = (NewInvoicePlace) place;
			setupNewInvoiceView(panel, p);

		} else {
			getClientFactory().getPlaceController().goTo(new HomePlace());
		}

	}

	private void setupNewInvoiceView(final AcceptsOneWidget panel, final NewInvoicePlace place){
		ServerFacade.INSTANCE.getBatchfetcherService().fetchNewInvoiceForClientOpData(place.getClientId(), new DocumentCallack<Triple<Long,ClientDTO,PaymentTypeDTO>>() {

			@Override
			public void onSuccess(final Triple<Long,ClientDTO,PaymentTypeDTO> result) {
				DocumentUtils.showBusinessDialogIfBusinessInformationNotComplete(new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(Void vo) {
						DocumentUtils.showClientDialogIfClientInformationNotComplete(result.getSecond(), new AsyncCallback<ClientDTO>() {

							@Override
							public void onFailure(Throwable caught) {
								BridgeUtils.invokeJSCallbackOnException(caught.getClass().getName(), "", getCallback());
							}

							@Override
							public void onSuccess(final ClientDTO newClient) {
								getClientFactory().getInvoiceView(false, new InvoiceViewCallback() {

									@Override
									public void onSuccess(InvoiceView view) {
										NewInvoicePresenter p = new NewInvoicePresenter(getClientFactory().getPlaceController(), 
												getClientFactory().getEventBus(), view, getCallback());
										p.setDataForNewInvoice(newClient, result.getFirst(), result.getThird());
										p.go(panel);

									}
								});
							}
						});
					}
				});
			}
		});
	}

	private void setupFromEstimationInvoiceView(final AcceptsOneWidget panel, final FromEstimationInvoicePlace place){
		ServerFacade.INSTANCE.getBatchfetcherService().fetchNewInvoiceFromEstimationOpData(place.getEstimationId(), new DocumentCallack<Triple<Long,EstimationDTO,PaymentTypeDTO>>(){

			@Override
			public void onSuccess(final Triple<Long, EstimationDTO, PaymentTypeDTO> result) {
				DocumentUtils.showBusinessDialogIfBusinessInformationNotComplete(new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(Void vo) {
						DocumentUtils.showClientDialogIfClientInformationNotComplete(result.getSecond().getClient(), new AsyncCallback<ClientDTO>() {

							@Override
							public void onFailure(Throwable caught) {
								BridgeUtils.invokeJSCallbackOnException(caught.getClass().getName(), "", getCallback());
							}

							@Override
							public void onSuccess(final ClientDTO newClient) {
								getClientFactory().getInvoiceView(false, new InvoiceViewCallback() {

									@Override
									public void onSuccess(InvoiceView view) {
										result.getSecond().setClient(newClient);
										NewInvoicePresenter p = new NewInvoicePresenter(getClientFactory().getPlaceController(), 
												getClientFactory().getEventBus(), view, getCallback());
										p.setDataForNewInvoice(result.getFirst(), result.getSecond(), result.getThird());
										p.go(panel);

									}
								});
								
							}
						});
					}
				});
			}

		});
	}


	private void setupFromTransportDocumentListInvoiceView(final AcceptsOneWidget panel, 
			final FromTransportDocumentListInvoicePlace place){
		ServerFacade.INSTANCE.getBatchfetcherService().fetchNewInvoiceFromTransportDocumentsOpData(place.getTransportDocumentList(), 
				new DocumentCallack<Triple<Long, List<TransportDocumentDTO>, PaymentTypeDTO>>() {

			@Override
			public void onSuccess(final Triple<Long, List<TransportDocumentDTO>, PaymentTypeDTO> result) {
				getClientFactory().getInvoiceView(true, new InvoiceViewCallback() {

					@Override
					public void onSuccess(final InvoiceView view) {
						NewInvoicePresenter p = new NewInvoicePresenter(getClientFactory().getPlaceController(), 
								getClientFactory().getEventBus(), view, getCallback());
						p.setTransportDocumentSources(place.getTransportDocumentList());
						p.setDataForNewInvoice(result.getFirst(), result.getSecond(), result.getThird());
						p.go(panel);
					}
				});
			}
		});
	}



	private void setupModifyInvoiceView(final AcceptsOneWidget panel, ModifyInvoicePlace place){
		ServerFacade.INSTANCE.getInvoiceService().get(place.getInvoiceId(), new DocumentCallack<InvoiceDTO>() {

			@Override
			public void onSuccess(final InvoiceDTO result) {
				getClientFactory().getInvoiceView(result.isCreatedFromTransportDocuments(), new InvoiceViewCallback() {

					@Override
					public void onSuccess(InvoiceView view) {
						ModifyInvoicePresenter p = new ModifyInvoicePresenter(getClientFactory().getPlaceController(), 
								getClientFactory().getEventBus(), view, getCallback());
						p.setData(result);
						p.go(panel);

					}
				});
			}

		});
	}

	private void setupCloneInvoiceView(final AcceptsOneWidget panel, final CloneInvoicePlace place){
		ServerFacade.INSTANCE.getBatchfetcherService().fetchCloneInvoiceOpData(place.getInvoiceId(), place.getClientId(), 
				new DocumentCallack<Triple<Long,ClientDTO,InvoiceDTO>>() {

			@Override
			public void onSuccess(final Triple<Long, ClientDTO, InvoiceDTO> result) {
				DocumentUtils.showBusinessDialogIfBusinessInformationNotComplete(new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(Void vo) {
						DocumentUtils.showClientDialogIfClientInformationNotComplete(result.getSecond(), new AsyncCallback<ClientDTO>() {

							@Override
							public void onFailure(Throwable caught) {
								BridgeUtils.invokeJSCallbackOnException(caught.getClass().getName(), "", getCallback());
							}

							@Override
							public void onSuccess(final ClientDTO newClient) {
								getClientFactory().getInvoiceView(false, new InvoiceViewCallback() {

									@Override
									public void onSuccess(InvoiceView view) {
										NewInvoicePresenter p = new NewInvoicePresenter(getClientFactory().getPlaceController(), 
												getClientFactory().getEventBus(), view, getCallback());
										p.setDataForNewInvoice(newClient, result.getFirst(), result.getThird());
										p.go(panel);
									}
								});
							}
						});
					}
				});
			}
		});
	}

}
