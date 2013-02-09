package com.novadart.novabill.frontend.client.activity.west;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.activity.BasicActivity;
import com.novadart.novabill.frontend.client.place.creditnote.CreditNotePlace;
import com.novadart.novabill.frontend.client.presenter.west.empty.EmptyViewPresenter;
import com.novadart.novabill.frontend.client.view.west.empty.EmptyWestView;

public class CreditNoteActivity extends BasicActivity {


	public CreditNoteActivity(CreditNotePlace place, ClientFactory clientFactory) {
		super(clientFactory);
	}

	@Override
	public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
		getClientFactory().getEmptyWestView(new AsyncCallback<EmptyWestView>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(EmptyWestView wv) {
				EmptyViewPresenter p = new EmptyViewPresenter(getClientFactory().getPlaceController(), eventBus, wv);
				p.go(panel);
			}
		});

	}

}
