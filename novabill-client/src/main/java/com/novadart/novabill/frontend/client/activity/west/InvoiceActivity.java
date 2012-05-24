package com.novadart.novabill.frontend.client.activity.west;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.activity.BasicActivity;
import com.novadart.novabill.frontend.client.facade.AuthAwareAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.place.InvoicePlace;
import com.novadart.novabill.frontend.client.ui.west.WestView;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class InvoiceActivity extends BasicActivity {

	private final InvoicePlace place;

	public InvoiceActivity(InvoicePlace place, ClientFactory clientFactory) {
		super(clientFactory);
		this.place = place;
	}

	@Override
	public void start(final AcceptsOneWidget panel, EventBus eventBus) {
		final WestView cv = getClientFactory().getWestView();
		cv.setPresenter(this);

		if(place.getInvoiceId() == 0){

			if(place.getClient() == null){
			
				goTo(new HomePlace());
			
			} else {
				
				cv.setClient(place.getClient());
				panel.setWidget(cv);
				
			}

		} else {

			ServerFacade.client.getFromInvoiceId(place.getInvoiceId(), new AuthAwareAsyncCallback<ClientDTO>() {

				@Override
				public void onSuccess(ClientDTO result) {
					cv.setClient(result);
					panel.setWidget(cv);
				}

				@Override
				public void onException(Throwable caught) {
					Window.alert(I18N.INSTANCE.errorServerCommunication());
					goTo(new HomePlace());
				}
			});

		}

	}

}
