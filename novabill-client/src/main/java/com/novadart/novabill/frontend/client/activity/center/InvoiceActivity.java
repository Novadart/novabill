package com.novadart.novabill.frontend.client.activity.center;

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
import com.novadart.novabill.frontend.client.ui.center.InvoiceView;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class InvoiceActivity extends BasicActivity {

	private final InvoicePlace invoicePlace;


	public InvoiceActivity(InvoicePlace place, ClientFactory clientFactory) {
		super(clientFactory);
		this.invoicePlace = place;
	}

	@Override
	public void start(final AcceptsOneWidget panel, EventBus eventBus) {
		final InvoiceView iv = getClientFactory().getInvoiceView();
		iv.setPresenter(this);

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
					Window.alert(I18N.get.errorServerCommunication());
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

}
