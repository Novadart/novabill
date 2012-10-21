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
import com.novadart.novabill.frontend.client.place.TransportDocumentPlace;
import com.novadart.novabill.frontend.client.ui.MainWidget;
import com.novadart.novabill.frontend.client.ui.center.TransportDocumentView;
import com.novadart.novabill.frontend.client.ui.widget.notification.Notification;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public class TransportDocumentActivity extends BasicActivity {

	private final TransportDocumentPlace transportDocumentPlace;

	public TransportDocumentActivity(TransportDocumentPlace place, ClientFactory clientFactory) {
		super(clientFactory);
		this.transportDocumentPlace = place;
	}

	@Override
	public void start(final AcceptsOneWidget panel, EventBus eventBus) {
		getClientFactory().getTransportDocumentView(new AsyncCallback<TransportDocumentView>() {

			@Override
			public void onSuccess(final TransportDocumentView tdv) {
				tdv.setPresenter(TransportDocumentActivity.this);

				if(transportDocumentPlace.getTransportDocumentId() == 0){ //we're creating a new invoice

					if(transportDocumentPlace.getClient() == null){

						goTo(new HomePlace());

					} else {
						
						if(transportDocumentPlace.getTransportDocumentToClone() != null) {
							tdv.setDataForNewTransportDocument(transportDocumentPlace.getClient(), 
									transportDocumentPlace.getTransportDocumentToClone());
						} else {
							tdv.setDataForNewTransportDocument(transportDocumentPlace.getClient());
						}
						
						MainWidget.getInstance().setLargeView();
						panel.setWidget(tdv);

					}

				} else {

					ServerFacade.transportDocument.get(transportDocumentPlace.getTransportDocumentId(), new WrappedAsyncCallback<TransportDocumentDTO>() {

						@Override
						public void onException(Throwable caught) {
							Notification.showMessage(I18N.INSTANCE.errorServerCommunication());
							goTo(new HomePlace());
						}

						@Override
						public void onSuccess(TransportDocumentDTO result) {
							tdv.setTransportDocument(result);
							MainWidget.getInstance().setLargeView();
							panel.setWidget(tdv);
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
