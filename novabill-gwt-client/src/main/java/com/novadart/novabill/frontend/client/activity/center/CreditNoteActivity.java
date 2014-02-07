package com.novadart.novabill.frontend.client.activity.center;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.bridge.BridgeUtils;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.place.creditnote.CreditNotePlace;
import com.novadart.novabill.frontend.client.place.creditnote.FromInvoiceCreditNotePlace;
import com.novadart.novabill.frontend.client.place.creditnote.ModifyCreditNotePlace;
import com.novadart.novabill.frontend.client.place.creditnote.NewCreditNotePlace;
import com.novadart.novabill.frontend.client.presenter.center.creditnote.ModifyCreditNotePresenter;
import com.novadart.novabill.frontend.client.presenter.center.creditnote.NewCreditNotePresenter;
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.view.center.creditnote.CreditNoteView;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.tuple.Pair;

public class CreditNoteActivity extends AbstractCenterActivity {

	private final CreditNotePlace place;


	public CreditNoteActivity(CreditNotePlace place, ClientFactory clientFactory, JavaScriptObject callback) {
		super(clientFactory, callback);
		this.place = place;
	}

	@Override
	public void start(final AcceptsOneWidget panel, final EventBus eventBus) {
		super.start(panel, eventBus);

		getClientFactory().getCreditNoteView(new AsyncCallback<CreditNoteView>() {

			@Override
			public void onSuccess(final CreditNoteView view) {
				if (place instanceof ModifyCreditNotePlace) {
					ModifyCreditNotePlace p = (ModifyCreditNotePlace) place;
					setupModifyCreditNoteView(panel, view, p);

				} else if (place instanceof FromInvoiceCreditNotePlace) {
					FromInvoiceCreditNotePlace p = (FromInvoiceCreditNotePlace) place;
					setupFromInvoiceCreditNoteView(panel, view, p);

				} else if (place instanceof NewCreditNotePlace) {
					NewCreditNotePlace p = (NewCreditNotePlace) place;
					setupNewCreditNoteView(panel, view, p);

				} else {
					getClientFactory().getPlaceController().goTo(new HomePlace());
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				manageError();
			}
		});
	}

	private void setupNewCreditNoteView(final AcceptsOneWidget panel, final CreditNoteView view, final NewCreditNotePlace place){
		
		ServerFacade.INSTANCE.getBatchfetcherService().fetchNewCreditNoteForClientOpData(place.getClientId(), new DocumentCallack<Pair<Long,ClientDTO>>() {

			@Override
			public void onSuccess(final Pair<Long, ClientDTO> result) {
				DocumentUtils.showClientDialogIfClientInformationNotComplete(result.getSecond(), new AsyncCallback<ClientDTO>() {

					@Override
					public void onFailure(Throwable caught) {
						BridgeUtils.invokeJSCallbackOnException(caught.getClass().getName(), "", getCallback());
					}

					@Override
					public void onSuccess(ClientDTO newClient) {
						NewCreditNotePresenter p = new NewCreditNotePresenter(getClientFactory().getPlaceController(), 
								getClientFactory().getEventBus(), view, getCallback());
						p.setDataForNewCreditNote(newClient, result.getFirst());
						p.go(panel);
						
					}
				});
			}
		});
	}

	private void setupFromInvoiceCreditNoteView(final AcceptsOneWidget panel, final CreditNoteView view, final FromInvoiceCreditNotePlace place){
		ServerFacade.INSTANCE.getBatchfetcherService().fetchNewCreditNoteFromInvoiceOpData(place.getInvoiceId(), new DocumentCallack<Pair<Long,InvoiceDTO>>() {

			@Override
			public void onSuccess(Pair<Long, InvoiceDTO> result) {
				NewCreditNotePresenter p = new NewCreditNotePresenter(getClientFactory().getPlaceController(), 
						getClientFactory().getEventBus(), view, getCallback());
				p.setDataForNewCreditNote(result.getFirst(), result.getSecond());
				p.go(panel);
			}
		});
	}

	private void setupModifyCreditNoteView(final AcceptsOneWidget panel, final CreditNoteView view, ModifyCreditNotePlace place){
		ServerFacade.INSTANCE.getCreditNoteService().get(place.getCreditNoteId(), new DocumentCallack<CreditNoteDTO>() {

			@Override
			public void onSuccess(CreditNoteDTO result) {
				ModifyCreditNotePresenter p = new ModifyCreditNotePresenter(getClientFactory().getPlaceController(), 
						getClientFactory().getEventBus(), view, getCallback());
				p.setData(result);
				p.go(panel);
			}

		});
	}
}
