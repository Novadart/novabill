package com.novadart.novabill.frontend.client.activity.center;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.activity.BasicActivity;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.place.creditnote.CreditNotePlace;
import com.novadart.novabill.frontend.client.place.creditnote.FromInvoiceCreditNotePlace;
import com.novadart.novabill.frontend.client.place.creditnote.ModifyCreditNotePlace;
import com.novadart.novabill.frontend.client.place.creditnote.NewCreditNotePlace;
import com.novadart.novabill.frontend.client.ui.MainWidget;
import com.novadart.novabill.frontend.client.ui.center.CreditNoteView;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

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
			public void onSuccess(final CreditNoteView view) {
				view.setPresenter(CreditNoteActivity.this);

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
					goTo(new HomePlace());
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				manageError();
			}
		});
	}

	private void setupNewCreditNoteView(final AcceptsOneWidget panel, final CreditNoteView view, final NewCreditNotePlace place){
		ServerFacade.creditNote.getNextCreditNoteDocumentID(new WrappedAsyncCallback<Long>() {

			@Override
			public void onSuccess(final Long progrId) {
				ServerFacade.client.get(place.getClientId(), new WrappedAsyncCallback<ClientDTO>() {

					@Override
					public void onSuccess(ClientDTO client) {
						view.setDataForNewCreditNote(client, progrId);
						MainWidget.getInstance().setLargeView();
						panel.setWidget(view);
					}

					@Override
					public void onException(Throwable caught) {
						manageError();
					}
				});
			}

			@Override
			public void onException(Throwable caught) {
				manageError();
			}
		});
	}
	
	private void setupFromInvoiceCreditNoteView(final AcceptsOneWidget panel, final CreditNoteView view, final FromInvoiceCreditNotePlace place){
		ServerFacade.creditNote.getNextCreditNoteDocumentID(new WrappedAsyncCallback<Long>() {

			@Override
			public void onSuccess(final Long progrId) {
				ServerFacade.invoice.get(place.getInvoiceId(), new WrappedAsyncCallback<InvoiceDTO>() {

					@Override
					public void onSuccess(InvoiceDTO result) {
						view.setDataForNewCreditNote(progrId, result);
						MainWidget.getInstance().setLargeView();
						panel.setWidget(view);
					}

					@Override
					public void onException(Throwable caught) {
						manageError();
					}
				});
			}

			@Override
			public void onException(Throwable caught) {
				manageError();
			}
		});
	}
	
	private void setupModifyCreditNoteView(final AcceptsOneWidget panel, final CreditNoteView view, ModifyCreditNotePlace place){
		ServerFacade.creditNote.get(place.getCreditNoteId(), new WrappedAsyncCallback<CreditNoteDTO>() {

			@Override
			public void onSuccess(CreditNoteDTO result) {
				view.setCreditNote(result);
				MainWidget.getInstance().setLargeView();
				panel.setWidget(view);
			}

			@Override
			public void onException(Throwable caught) {
				manageError();
			}
		});
	}
}
