package com.novadart.novabill.frontend.client.presenter.center.client;

import java.util.Collections;
import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.ListDataProvider;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.Const;
import com.novadart.novabill.frontend.client.event.ClientDeleteEvent;
import com.novadart.novabill.frontend.client.event.ClientUpdateEvent;
import com.novadart.novabill.frontend.client.event.ClientUpdateHandler;
import com.novadart.novabill.frontend.client.event.DocumentAddEvent;
import com.novadart.novabill.frontend.client.event.DocumentAddHandler;
import com.novadart.novabill.frontend.client.event.DocumentDeleteEvent;
import com.novadart.novabill.frontend.client.event.DocumentDeleteHandler;
import com.novadart.novabill.frontend.client.event.DocumentUpdateEvent;
import com.novadart.novabill.frontend.client.event.DocumentUpdateHandler;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.ClientPlace.DOCUMENTS;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.place.creditnote.NewCreditNotePlace;
import com.novadart.novabill.frontend.client.place.estimation.NewEstimationPlace;
import com.novadart.novabill.frontend.client.place.invoice.NewInvoicePlace;
import com.novadart.novabill.frontend.client.place.transportdocument.NewTransportDocumentPlace;
import com.novadart.novabill.frontend.client.presenter.AbstractPresenter;
import com.novadart.novabill.frontend.client.view.MainWidget;
import com.novadart.novabill.frontend.client.view.center.client.ClientView;
import com.novadart.novabill.frontend.client.widget.dialog.client.ClientDialog;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;

public class ClientPresenter extends AbstractPresenter<ClientView> implements ClientView.Presenter {

	private ClientDTO client;
	private final ListDataProvider<InvoiceDTO> invoiceDataProvider = new ListDataProvider<InvoiceDTO>();
	private final ListDataProvider<EstimationDTO> estimationDataProvider = new ListDataProvider<EstimationDTO>();
	private final ListDataProvider<CreditNoteDTO> creditNoteDataProvider = new ListDataProvider<CreditNoteDTO>();
	private final ListDataProvider<TransportDocumentDTO> transportDocumentDataProvider = new ListDataProvider<TransportDocumentDTO>();

	
	public ClientPresenter(PlaceController placeController, EventBus eventBus,	ClientView view) {
		super(placeController, eventBus, view);
		setDocumentsListing(DOCUMENTS.invoices);
	}
	
	@Override
	protected void setPresenterInView(ClientView view) {
		view.setPresenter(this);
	}
	
	@Override
	public void go(AcceptsOneWidget panel) {
		MainWidget.INSTANCE.setStandardView();
		panel.setWidget(getView());
	}
	
	@Override
	public void setDocumentsListing(DOCUMENTS documentsListing) {
		switch(documentsListing){
		case estimations:
			getView().getTabPanel().selectTab(1);
			break;
		case transportDocuments:
			getView().getTabPanel().selectTab(3);
			break;
		case creditNotes:
			getView().getTabPanel().selectTab(2);
			break;
		default:
		case invoices:
			getView().getTabPanel().selectTab(0);
			break;
		}
	}

	@Override
	public void setClient(ClientDTO client) {
		if(client == null){
			goTo(new HomePlace());
		}
		this.client = client;
		getView().getClientName().setText(client.getName());
		updateClientDetails(client);
		getView().getContactPopup().setContact(client.getContact());
		if(getView().getContactPopup().isEmpty()) {
			getView().getContact().setStyleName("contact-disabled");
		} else {
			getView().getContact().setStyleName("contact");
		}
		getView().getContact().setEnabled(!getView().getContactPopup().isEmpty());
		loadInvoices();
		loadEstimations();
		loadCreditNotes();
		loadTransportDocuments();
	}


	private void bind(){
		getEventBus().addHandler(DocumentAddEvent.TYPE, new DocumentAddHandler() {

			@Override
			public void onDocumentAdd(DocumentAddEvent event) {
				onDocumentChangeEvent(event.getDocument());
			}
		});
		getEventBus().addHandler(DocumentDeleteEvent.TYPE, new DocumentDeleteHandler() {

			@Override
			public void onDocumentDelete(DocumentDeleteEvent event) {
				onDocumentChangeEvent(event.getDocument());
			}
		});
		getEventBus().addHandler(DocumentUpdateEvent.TYPE, new DocumentUpdateHandler() {

			@Override
			public void onDocumentUpdate(DocumentUpdateEvent event) {
				onDocumentChangeEvent(event.getDocument());
			}
		});
		getEventBus().addHandler(ClientUpdateEvent.TYPE, new ClientUpdateHandler() {

			@Override
			public void onClientUpdate(ClientUpdateEvent event) {
				ServerFacade.INSTANCE.getClientService().get(client.getId(), 
						new ManagedAsyncCallback<ClientDTO>() {

					@Override
					public void onSuccess(ClientDTO result) {
						setClient(result);
					}

					public void onFailure(Throwable caught) {
						super.onFailure(caught);
						Window.Location.reload();
					};
				});
			}
		});
	}


	private void updateClientDetails(ClientDTO client){
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		sb.appendEscaped(client.getAddress()+ " ");
		sb.appendEscaped(client.getPostcode() + " " 
				+ client.getCity() + client.getProvince()!=null&&!client.getProvince().isEmpty() ? 
						" (" + client.getProvince() +") " : " " + client.getCountry() +" ");
		boolean hasPhone = client.getPhone()!=null && !client.getPhone().isEmpty();
		boolean hasFax = client.getFax()!=null && !client.getFax().isEmpty();
		boolean hasVatID = client.getVatID()!=null && !client.getVatID().isEmpty();
		boolean hasSSN = client.getSsn()!=null && !client.getSsn().isEmpty();
		sb.appendEscaped( ( (hasPhone?"Tel. "+client.getPhone()+" ":"") 
				+ (hasFax?" Fax "+client.getFax()+" ":"")
				+ (hasVatID ? " " + I18N.INSTANCE.vatID()+" "+client.getVatID() + " " : " ") 
				+ (hasSSN ? " " + I18N.INSTANCE.ssn()+" "+client.getSsn() : "") ).trim() );

		getView().getClientDetails().setHTML(sb.toSafeHtml());
	}



	private void loadInvoices(){
		ServerFacade.INSTANCE.getInvoiceService().getAllForClient(client.getId(), new ManagedAsyncCallback<List<InvoiceDTO>>() {

			@Override
			public void onSuccess(List<InvoiceDTO> result) {
				if(result == null){
					return;
				}
				Collections.sort(result, Const.DOCUMENT_COMPARATOR);
				invoiceDataProvider.setList(result);
				invoiceDataProvider.refresh();
			}
		});
	}


	private void loadTransportDocuments(){
		ServerFacade.INSTANCE.getTransportdocumentService().getAllForClient(client.getId(), new ManagedAsyncCallback<List<TransportDocumentDTO>>() {

			@Override
			public void onSuccess(List<TransportDocumentDTO> result) {
				if(result == null){
					return;
				}
				Collections.sort(result, Const.DOCUMENT_COMPARATOR);
				transportDocumentDataProvider.setList(result);
				transportDocumentDataProvider.refresh();
			}

		});
	}


	private void loadCreditNotes(){
		ServerFacade.INSTANCE.getCreditnoteService().getAllForClient(client.getId(), new ManagedAsyncCallback<List<CreditNoteDTO>>() {

			@Override
			public void onSuccess(List<CreditNoteDTO> result) {
				if(result == null){
					return;
				}
				Collections.sort(result, Const.DOCUMENT_COMPARATOR);
				creditNoteDataProvider.setList(result);
				creditNoteDataProvider.refresh();
			}

		});
	}

	private void loadEstimations(){
		ServerFacade.INSTANCE.getEstimationService().getAllForClient(client.getId(), new ManagedAsyncCallback<List<EstimationDTO>>() {

			@Override
			public void onSuccess(List<EstimationDTO> result) {
				if(result == null){
					return;
				}
				Collections.sort(result, Const.DOCUMENT_COMPARATOR);
				estimationDataProvider.setList(result);
				estimationDataProvider.refresh();
			}
		});
	}

	
	private void onDocumentChangeEvent(AccountingDocumentDTO doc){
		if(doc instanceof InvoiceDTO){
			loadInvoices();
		} else if(doc instanceof EstimationDTO){
			loadEstimations();
		} else if(doc instanceof CreditNoteDTO){
			loadCreditNotes();
		} else if(doc instanceof TransportDocumentDTO){
			loadTransportDocuments();
		}
	}

	@Override
	public void onCancelClientClicked() {
		Notification.showConfirm(I18N.INSTANCE.confirmClientDeletion(), new NotificationCallback<Boolean>() {

			@Override
			public void onNotificationClosed(Boolean value) {
				if(value){

					getView().getCancelClient().showLoader(true);
					getView().setLocked(true);
					ServerFacade.INSTANCE.getClientService().remove(Configuration.getBusinessId(), client.getId(), new ManagedAsyncCallback<Void>() {

						@Override
						public void onSuccess(Void result) {
							getView().getCancelClient().showLoader(false);
							getEventBus().fireEvent(new ClientDeleteEvent(client));
							goTo(new HomePlace());
							getView().setLocked(false);
						}

						@Override
						public void onFailure(Throwable caught) {
							getView().getCancelClient().showLoader(false);
							if(caught instanceof DataIntegrityException){
								Notification.showMessage(I18N.INSTANCE.errorClientCancelation());
							} else {
								super.onFailure(caught);
							}
							getView().setLocked(false);
						}
					});
				}
			}
		});
	}


	@Override
	public void onContactMouseClick() {
		if(getView().getContact().isDown()){
			getView().getContactPopup().showRelativeTo(getView().getContact());
		} else {
			getView().getContactPopup().hide();
		}
	}


	@Override
	public void onLoad() {
		bind();
		getView().getInvoiceList().setEventBus(getEventBus());
		getView().getEstimationList().setEventBus(getEventBus());
		getView().getCreditNoteList().setEventBus(getEventBus());
		getView().getTransportDocumentList().setEventBus(getEventBus());
		
		invoiceDataProvider.addDataDisplay(getView().getInvoiceList());
		estimationDataProvider.addDataDisplay(getView().getEstimationList());
		creditNoteDataProvider.addDataDisplay(getView().getCreditNoteList());
		transportDocumentDataProvider.addDataDisplay(getView().getTransportDocumentList());

	}


	@Override
	public void onUnload() {
		getView().getContactPopup().hide();
	}


	@Override
	public void onNewCreditNoteClicked() {
		NewCreditNotePlace p = new NewCreditNotePlace();
		p.setClientId(client.getId());
		goTo(p);
	}


	@Override
	public void onNewEstimationClicked() {
		NewEstimationPlace p = new NewEstimationPlace();
		p.setClientId(client.getId());
		goTo(p);
	}


	@Override
	public void onNewTransportDocumentClicked() {
		NewTransportDocumentPlace p = new NewTransportDocumentPlace();
		p.setClientId(client.getId());
		goTo(p);
	}


	@Override
	public void onModifyClientClicked() {
		ClientDialog cd = ClientDialog.getInstance(getEventBus());
		cd.setClient(client);
		cd.showCentered();
	}


	@Override
	public void onNewInvoiceClicked() {
		NewInvoicePlace nip = new NewInvoicePlace();
		nip.setClientId(client.getId());
		goTo(nip);
	}


}
