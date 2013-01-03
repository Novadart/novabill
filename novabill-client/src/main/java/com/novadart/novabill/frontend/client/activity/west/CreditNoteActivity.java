package com.novadart.novabill.frontend.client.activity.west;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.activity.BasicActivity;
import com.novadart.novabill.frontend.client.place.creditnote.CreditNotePlace;
import com.novadart.novabill.frontend.client.ui.west.WestView;

public class CreditNoteActivity extends BasicActivity {


	public CreditNoteActivity(CreditNotePlace place, ClientFactory clientFactory) {
		super(clientFactory);
	}

	@Override
	public void start(final AcceptsOneWidget panel, EventBus eventBus) {
		getClientFactory().getWestView(new AsyncCallback<WestView>() {
			
			@Override
			public void onSuccess(final WestView wv) {
				wv.setPresenter(CreditNoteActivity.this);
				wv.setClient(null);
				panel.setWidget(wv);
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});

	}

}
