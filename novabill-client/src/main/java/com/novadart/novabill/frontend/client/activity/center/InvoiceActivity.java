package com.novadart.novabill.frontend.client.activity.center;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.activity.BasicActivity;
import com.novadart.novabill.frontend.client.facade.AuthAwareAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.EstimationPlace;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.place.InvoicePlace;
import com.novadart.novabill.frontend.client.ui.center.InvoiceView;
import com.novadart.novabill.frontend.client.ui.widget.notification.Notification;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class InvoiceActivity extends BasicActivity {

	private final Place place;


	public InvoiceActivity(Place place, ClientFactory clientFactory) {
		super(clientFactory);
		this.place = place;
	}

	@Override
	public void start(final AcceptsOneWidget panel, EventBus eventBus) {
		if(place instanceof InvoicePlace){
			start((InvoicePlace)place, panel);
		} else {
			start((EstimationPlace)place, panel);
		}
	}

	private void start(final InvoicePlace invoicePlace, final AcceptsOneWidget panel){
		getClientFactory().getInvoiceView(new AsyncCallback<InvoiceView>() {

			@Override
			public void onSuccess(final InvoiceView iv) {
				iv.setPresenter(InvoiceActivity.this);

				if(invoicePlace.getInvoiceId() == 0){ //we're creating a new invoice

					if(invoicePlace.getClient() == null){

						goTo(new HomePlace());

					} else {

						iv.setDataForNewInvoice(invoicePlace.getClient(), invoicePlace.getInvoiceProgressiveId());
						panel.setWidget(iv);

					}

				} else {

					ServerFacade.invoice.get(invoicePlace.getInvoiceId(), new AuthAwareAsyncCallback<InvoiceDTO>() {

						@Override
						public void onException(Throwable caught) {
							Notification.showMessage(I18N.INSTANCE.errorServerCommunication());
							goTo(new HomePlace());
						}

						@Override
						public void onSuccess(InvoiceDTO result) {
							iv.setInvoice(result);
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

	private void start(final EstimationPlace estimationPlace, final AcceptsOneWidget panel){
		getClientFactory().getInvoiceView(new AsyncCallback<InvoiceView>() {

			@Override
			public void onSuccess(final InvoiceView iv) {
				iv.setPresenter(InvoiceActivity.this);

				if(estimationPlace.getEstimationId() == 0){ //we're creating a new invoice

					if(estimationPlace.getClient() == null){

						goTo(new HomePlace());

					} else {

						iv.setDataForNewEstimation(estimationPlace.getClient());
						panel.setWidget(iv);

					}

				} else {

					ServerFacade.estimation.get(estimationPlace.getEstimationId(), new AuthAwareAsyncCallback<EstimationDTO>() {

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
