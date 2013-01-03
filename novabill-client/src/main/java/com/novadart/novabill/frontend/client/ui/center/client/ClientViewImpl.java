package com.novadart.novabill.frontend.client.ui.center.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.datawatcher.DataWatchEvent.DATA;
import com.novadart.novabill.frontend.client.datawatcher.DataWatchEventHandler;
import com.novadart.novabill.frontend.client.datawatcher.DataWatcher;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.ClientPlace.DOCUMENTS;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.place.TransportDocumentPlace;
import com.novadart.novabill.frontend.client.place.creditnote.NewCreditNotePlace;
import com.novadart.novabill.frontend.client.place.estimation.NewEstimationPlace;
import com.novadart.novabill.frontend.client.place.invoice.NewInvoicePlace;
import com.novadart.novabill.frontend.client.ui.center.ClientView;
import com.novadart.novabill.frontend.client.ui.center.client.dialog.ClientDialog;
import com.novadart.novabill.frontend.client.ui.widget.list.impl.CreditNoteList;
import com.novadart.novabill.frontend.client.ui.widget.list.impl.EstimationList;
import com.novadart.novabill.frontend.client.ui.widget.list.impl.InvoiceList;
import com.novadart.novabill.frontend.client.ui.widget.list.impl.TransportDocumentList;
import com.novadart.novabill.frontend.client.ui.widget.notification.Notification;
import com.novadart.novabill.frontend.client.util.WidgetUtils;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;

public class ClientViewImpl extends Composite implements ClientView {

	private static ClientViewImplUiBinder uiBinder = GWT
			.create(ClientViewImplUiBinder.class);

	interface ClientViewImplUiBinder extends UiBinder<Widget, ClientViewImpl> {
	}

	private Presenter presenter;
	private ClientDTO client;
	private final ListDataProvider<InvoiceDTO> invoiceDataProvider = new ListDataProvider<InvoiceDTO>();
	private final ListDataProvider<EstimationDTO> estimationDataProvider = new ListDataProvider<EstimationDTO>();
	private final ListDataProvider<CreditNoteDTO> creditNoteDataProvider = new ListDataProvider<CreditNoteDTO>();
	private final ListDataProvider<TransportDocumentDTO> transportDocumentDataProvider = new ListDataProvider<TransportDocumentDTO>();
	private final ContactPopup contactPopup = new ContactPopup();
	
	@UiField InvoiceList invoiceList;
	@UiField EstimationList estimationList;
	@UiField CreditNoteList creditNoteList;
	@UiField TransportDocumentList transportDocumentList;
	@UiField Label clientName;
	@UiField HTML clientDetails;
	@UiField TabLayoutPanel tabPanel;
	
	@UiField FlowPanel listWrapperInvoice;
	@UiField SimplePanel actionWrapperInvoice;
	@UiField ScrollPanel scrollInvoice;
	@UiField FlowPanel listWrapperEstimation;
	@UiField SimplePanel actionWrapperEstimation;
	@UiField ScrollPanel scrollEstimation;
	@UiField FlowPanel listWrapperCredit;
	@UiField SimplePanel actionWrapperCredit;
	@UiField ScrollPanel scrollCredit;
	@UiField FlowPanel listWrapperTransport;
	@UiField SimplePanel actionWrapperTransport;
	@UiField ScrollPanel scrollTransport;

	@UiField HorizontalPanel clientOptions;
	@UiField SimpleLayoutPanel clientMainBody;
	
	@UiField Label contact;
	
	private static final int HIDE_TIMEOUT = 3000;
	private Timer hideContactPopup = new Timer() {
		
		@Override
		public void run() {
			contactPopup.hide();
		}
	};
	
	
	public ClientViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		setStyleName("ClientView");
		invoiceDataProvider.addDataDisplay(invoiceList);
		estimationDataProvider.addDataDisplay(estimationList);
		creditNoteDataProvider.addDataDisplay(creditNoteList);
		transportDocumentDataProvider.addDataDisplay(transportDocumentList);
		DataWatcher.getInstance().addDataEventHandler(new DataWatchEventHandler() {
			
			@Override
			public void onDataUpdated(DATA data) {
				switch (data) {
				case INVOICE:
					loadInvoices();
					break;
					
				case CLIENT:
				case CLIENT_DATA:
					ServerFacade.client.get(ClientViewImpl.this.client.getId(), 
							new WrappedAsyncCallback<ClientDTO>() {

						@Override
						public void onSuccess(ClientDTO result) {
							setClient(result);
						}

						@Override
						public void onException(Throwable caught) {
							Window.Location.reload();
						}
					});
					break;
					
				case ESTIMATION:
					loadEstimations();
					break;
					
				case CREDIT_NOTE:
					loadCreditNotes();
					break;
					
				case TRANSPORT_DOCUMENT:
					loadTransportDocuments();
					break;
					
				default:
					break;
				}
				
			}
		});
	}
	
	@Override
	protected void onUnload() {
		super.onUnload();
		contactPopup.hide();
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			
			@Override
			public void execute() {
				WidgetUtils.setElementHeightToFillSpace(clientMainBody.getElement(), getElement(), 
						clientName.getElement(), clientDetails.getElement(), clientOptions.getElement());
				
				WidgetUtils.setElementHeightToFillSpace(scrollInvoice.getElement(), listWrapperInvoice.getElement(), 
						40, actionWrapperInvoice.getElement());
				WidgetUtils.setElementHeightToFillSpace(scrollEstimation.getElement(), listWrapperEstimation.getElement(), 
						40, actionWrapperEstimation.getElement());
				WidgetUtils.setElementHeightToFillSpace(scrollTransport.getElement(), listWrapperTransport.getElement(), 
						40, actionWrapperTransport.getElement());
				WidgetUtils.setElementHeightToFillSpace(scrollCredit.getElement(), listWrapperCredit.getElement(), 
						40, actionWrapperCredit.getElement());
				
			}
		});
	}
	
	@Override
	public void setDocumentsListing(DOCUMENTS documentsListing) {
		switch(documentsListing){
		case estimations:
			tabPanel.selectTab(1);
			break;
		case transportDocuments:
			tabPanel.selectTab(2);
			break;
		case creditNotes:
			tabPanel.selectTab(3);
			break;
			default:
		case invoices:
			tabPanel.selectTab(0);
			break;
		}
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		invoiceList.setPresenter(presenter);
		estimationList.setPresenter(presenter);
		creditNoteList.setPresenter(presenter);
		transportDocumentList.setPresenter(presenter);
	}

	@Override
	public void setClean() {
		clientName.setText("");
		clientDetails.setHTML("");
		invoiceDataProvider.getList().clear();
		invoiceDataProvider.refresh();
		estimationDataProvider.getList().clear();
		estimationDataProvider.refresh();
		creditNoteDataProvider.getList().clear();
		creditNoteDataProvider.refresh();
		transportDocumentDataProvider.getList().clear();
		transportDocumentDataProvider.refresh();
		setDocumentsListing(DOCUMENTS.invoices);
		contactPopup.reset();
		contact.setVisible(false);
	}
	
	@UiHandler("contact")
	void onContactMouseOver(MouseOverEvent event) {
		contactPopup.showRelativeTo(contact);
		hideContactPopup.cancel();
	}
	
	@UiHandler("contact")
	void onContactMouseOut(MouseOutEvent event) {
		hideContactPopup.schedule(HIDE_TIMEOUT);
	}
	
	@UiHandler("newInvoice")
	void onNewInvoiceClicked(ClickEvent e){
		NewInvoicePlace nip = new NewInvoicePlace();
		nip.setClientId(client.getId());
		presenter.goTo(nip);
	}
	
	@UiHandler("newEstimation")
	void onNewEstimationClicked(ClickEvent e){
		NewEstimationPlace p = new NewEstimationPlace();
		p.setClientId(client.getId());
		presenter.goTo(p);
	}
	
	@UiHandler("newTransportDocument")
	void onNewTransportDocumentClicked(ClickEvent e){
		ServerFacade.transportDocument.getNextTransportDocId(new WrappedAsyncCallback<Long>() {

			@Override
			public void onSuccess(Long result) {
				if(result == null){
					return;
				}
				TransportDocumentPlace tdp = new TransportDocumentPlace();
				tdp.setDataForNewTransportDocument(client, result);
				presenter.goTo(tdp);
			}

			@Override
			public void onException(Throwable caught) {
				
			}
		});
	}
	
	@UiHandler("newCreditNote")
	void onNewCreditNoteClicked(ClickEvent e){
		NewCreditNotePlace p = new NewCreditNotePlace();
		p.setClientId(client.getId());
		presenter.goTo(p);
	}
	
	
	@UiHandler("modifyClient")
	void onModifyClientClicked(ClickEvent e){
		ClientDialog cd = ClientDialog.getInstance();
		cd.setClient(client);
		cd.showCentered();
	}

	
	@UiHandler("cancelClient")
	void onCancelClientClicked(ClickEvent e){
		if(Notification.showYesNoRequest(I18N.INSTANCE.confirmClientDeletion())){
			ServerFacade.client.remove(Configuration.getBusinessId(), client.getId(), new WrappedAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					DataWatcher.getInstance().fireClientEvent();
					DataWatcher.getInstance().fireStatsEvent();
					presenter.goTo(new HomePlace());
				}

				@Override
				public void onException(Throwable caught) {
					if(caught instanceof DataIntegrityException){
						Notification.showMessage(I18N.INSTANCE.errorClientCancelation());
					} else {
						Notification.showMessage(I18N.INSTANCE.errorServerCommunication());
					}
				}
			});
		}
	}

	@Override
	public void setClient(ClientDTO client) {
		if(client == null){
			presenter.goTo(new HomePlace());
		}
		this.client = client;
		clientName.setText(client.getName());
		updateClientDetails(client);
		contactPopup.setContact(client.getContact());
		if(!contactPopup.isEmpty()) {
			contact.setVisible(true);
		}
		loadInvoices();
		loadEstimations();
		loadCreditNotes();
		loadTransportDocuments();
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

		clientDetails.setHTML(sb.toSafeHtml());
	}
	
	
	
	private void loadInvoices(){
		ServerFacade.invoice.getAllForClient(client.getId(), new WrappedAsyncCallback<List<InvoiceDTO>>() {

			@Override
			public void onException(Throwable caught) {
				Notification.showMessage(I18N.INSTANCE.errorServerCommunication());
			}

			@Override
			public void onSuccess(List<InvoiceDTO> result) {
				if(result == null){
					return;
				}
				invoiceDataProvider.setList(result);
				invoiceDataProvider.refresh();
			}
		});
	}
	
	
	private void loadTransportDocuments(){
		ServerFacade.transportDocument.getAllForClient(client.getId(), new WrappedAsyncCallback<List<TransportDocumentDTO>>() {

			@Override
			public void onSuccess(List<TransportDocumentDTO> result) {
				if(result == null){
					return;
				}
				transportDocumentDataProvider.setList(result);
				transportDocumentDataProvider.refresh();
			}

			@Override
			public void onException(Throwable caught) {
				Notification.showMessage(I18N.INSTANCE.errorServerCommunication());
			}
		});
	}
	
	
	private void loadCreditNotes(){
		ServerFacade.creditNote.getAllForClient(client.getId(), new WrappedAsyncCallback<List<CreditNoteDTO>>() {

			@Override
			public void onSuccess(List<CreditNoteDTO> result) {
				if(result == null){
					return;
				}
				creditNoteDataProvider.setList(result);
				creditNoteDataProvider.refresh();
			}

			@Override
			public void onException(Throwable caught) {
				Notification.showMessage(I18N.INSTANCE.errorServerCommunication());
			}
		});
	}
	
	private void loadEstimations(){
		ServerFacade.estimation.getAllForClient(client.getId(), new WrappedAsyncCallback<List<EstimationDTO>>() {

			@Override
			public void onException(Throwable caught) {
				Notification.showMessage(I18N.INSTANCE.errorServerCommunication());
			}

			@Override
			public void onSuccess(List<EstimationDTO> result) {
				if(result == null){
					return;
				}
				estimationDataProvider.setList(result);
				estimationDataProvider.refresh();
			}
		});
	}

}
