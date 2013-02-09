package com.novadart.novabill.frontend.client.activity.center;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.place.creditnote.CreditNotePlace;
import com.novadart.novabill.frontend.client.place.creditnote.FromInvoiceCreditNotePlace;
import com.novadart.novabill.frontend.client.place.creditnote.ModifyCreditNotePlace;
import com.novadart.novabill.frontend.client.place.creditnote.NewCreditNotePlace;
import com.novadart.novabill.frontend.client.presenter.center.creditnote.ModifyCreditNotePresenter;
import com.novadart.novabill.frontend.client.presenter.center.creditnote.NewCreditNotePresenter;
import com.novadart.novabill.frontend.client.view.center.creditnote.CreditNoteView;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class CreditNoteActivity extends AbstractCenterActivity {

	private final CreditNotePlace place;


	public CreditNoteActivity(CreditNotePlace place, ClientFactory clientFactory) {
		super(clientFactory);
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
		ServerFacade.creditNote.getNextCreditNoteDocumentID(new DocumentCallack<Long>() {

			@Override
			public void onSuccess(final Long progrId) {
				ServerFacade.client.get(place.getClientId(), new DocumentCallack<ClientDTO>() {

					@Override
					public void onSuccess(ClientDTO client) {
						NewCreditNotePresenter p = new NewCreditNotePresenter(getClientFactory().getPlaceController(), 
								getClientFactory().getEventBus(), view);
						p.setDataForNewCreditNote(client, progrId);
						p.go(panel);
					}

				});
			}
		
		});
	}

	private void setupFromInvoiceCreditNoteView(final AcceptsOneWidget panel, final CreditNoteView view, final FromInvoiceCreditNotePlace place){
		ServerFacade.creditNote.getNextCreditNoteDocumentID(new DocumentCallack<Long>() {

			@Override
			public void onSuccess(final Long progrId) {
				ServerFacade.invoice.get(place.getInvoiceId(), new DocumentCallack<InvoiceDTO>() {

					@Override
					public void onSuccess(InvoiceDTO result) {
						NewCreditNotePresenter p = new NewCreditNotePresenter(getClientFactory().getPlaceController(), 
								getClientFactory().getEventBus(), view);
						p.setDataForNewCreditNote(progrId, result);
						p.go(panel);
					}
				});
			}
		});
	}

	private void setupModifyCreditNoteView(final AcceptsOneWidget panel, final CreditNoteView view, ModifyCreditNotePlace place){
		ServerFacade.creditNote.get(place.getCreditNoteId(), new DocumentCallack<CreditNoteDTO>() {

			@Override
			public void onSuccess(CreditNoteDTO result) {
				ModifyCreditNotePresenter p = new ModifyCreditNotePresenter(getClientFactory().getPlaceController(), 
						getClientFactory().getEventBus(), view);
				p.setData(result);
				p.go(panel);
			}

		});
	}
}
