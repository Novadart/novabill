package com.novadart.novabill.frontend.client.activity.center;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.activity.BasicActivity;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.place.InvoicePlace;
import com.novadart.novabill.frontend.client.ui.center.InvoiceView;
import com.novadart.novabill.frontend.client.ui.widget.notification.Notification;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class InvoiceActivity extends BasicActivity {

	private final InvoicePlace place;


	public InvoiceActivity(InvoicePlace place, ClientFactory clientFactory) {
		super(clientFactory);
		this.place = place;
	}

	@Override
	public void start(final AcceptsOneWidget panel, EventBus eventBus) {
		getClientFactory().getInvoiceView(new AsyncCallback<InvoiceView>() {

			@Override
			public void onSuccess(final InvoiceView iv) {
				iv.setPresenter(InvoiceActivity.this);

				if(place.getInvoiceId() == 0){ //we're creating a new invoice

					if(place.getClient() == null){

						goTo(new HomePlace());

					} else {

						if(place.getInvoiceToClone() != null){
							iv.setDataForNewInvoice(place.getClient(), place.getInvoiceProgressiveId(), place.getInvoiceToClone());
						} else if(place.getEstimationSource() != null) {
							iv.setDataForNewInvoice(place.getInvoiceProgressiveId(), place.getEstimationSource());
						} else {
							iv.setDataForNewInvoice(place.getClient(), place.getInvoiceProgressiveId());
						}

						panel.setWidget(iv);

					}

				} else {

					ServerFacade.invoice.get(place.getInvoiceId(), new WrappedAsyncCallback<InvoiceDTO>() {

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

}
