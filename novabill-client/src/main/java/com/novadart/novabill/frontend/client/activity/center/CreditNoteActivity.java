package com.novadart.novabill.frontend.client.activity.center;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.activity.BasicActivity;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.CreditNotePlace;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.ui.center.CreditNoteView;
import com.novadart.novabill.frontend.client.ui.widget.notification.Notification;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;

public class CreditNoteActivity extends BasicActivity {

	private final CreditNotePlace place;


	public CreditNoteActivity(CreditNotePlace place, ClientFactory clientFactory) {
		super(clientFactory);
		this.place = place;
	}

	@Override
	public void start(final AcceptsOneWidget panel, EventBus eventBus) {
		getClientFactory().getCreditNoteView(new AsyncCallback<CreditNoteView>() {

			@Override
			public void onSuccess(final CreditNoteView cnv) {
				cnv.setPresenter(CreditNoteActivity.this);

				if(place.getCreditNoteId() == 0){ //we're creating a new credit note

					if(place.getClient() == null){

						goTo(new HomePlace());

					} else {

						if(place.getSourceInvoice() != null){
							cnv.setDataForNewCreditNote(place.getCreditNoteProgressiveId(), place.getSourceInvoice());
						} else {
							cnv.setDataForNewCreditNote(place.getClient(), place.getCreditNoteProgressiveId());
						}

						panel.setWidget(cnv);

					}

				} else {

					ServerFacade.creditNote.get(place.getCreditNoteId(), new WrappedAsyncCallback<CreditNoteDTO>() {

						@Override
						public void onException(Throwable caught) {
							Notification.showMessage(I18N.INSTANCE.errorServerCommunication());
							goTo(new HomePlace());
						}

						@Override
						public void onSuccess(CreditNoteDTO result) {
							cnv.setCreditNote(result);
							panel.setWidget(cnv);
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
