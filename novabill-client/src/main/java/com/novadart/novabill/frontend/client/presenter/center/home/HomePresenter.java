package com.novadart.novabill.frontend.client.presenter.center.home;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.Range;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.Const;
import com.novadart.novabill.frontend.client.event.DocumentAddEvent;
import com.novadart.novabill.frontend.client.event.DocumentAddHandler;
import com.novadart.novabill.frontend.client.event.DocumentDeleteEvent;
import com.novadart.novabill.frontend.client.event.DocumentDeleteHandler;
import com.novadart.novabill.frontend.client.event.DocumentUpdateEvent;
import com.novadart.novabill.frontend.client.event.DocumentUpdateHandler;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.BusinessPlace;
import com.novadart.novabill.frontend.client.place.creditnote.NewCreditNotePlace;
import com.novadart.novabill.frontend.client.place.estimation.NewEstimationPlace;
import com.novadart.novabill.frontend.client.place.invoice.NewInvoicePlace;
import com.novadart.novabill.frontend.client.place.transportdocument.NewTransportDocumentPlace;
import com.novadart.novabill.frontend.client.presenter.AbstractPresenter;
import com.novadart.novabill.frontend.client.view.MainWidget;
import com.novadart.novabill.frontend.client.view.center.home.CreditNoteDataProvider;
import com.novadart.novabill.frontend.client.view.center.home.EstimationDataProvider;
import com.novadart.novabill.frontend.client.view.center.home.HomeView;
import com.novadart.novabill.frontend.client.view.center.home.InvoiceDataProvider;
import com.novadart.novabill.frontend.client.view.center.home.TransportDocumentDataProvider;
import com.novadart.novabill.frontend.client.widget.dialog.SelectClientDialog;
import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public class HomePresenter extends AbstractPresenter<HomeView> implements HomeView.Presenter {
	
	private static final int DOCS_PAGE_SIZE = 10;
	public static final Range DOCS_LIST_RANGE = new Range(0, DOCS_PAGE_SIZE);

	private boolean isInitialSetup = true;
	
	private final InvoiceDataProvider invoiceDataProvider = new InvoiceDataProvider();
	private final EstimationDataProvider estimationDataProvider = new EstimationDataProvider();
	private final CreditNoteDataProvider creditNoteDataProvider = new CreditNoteDataProvider();
	private final TransportDocumentDataProvider transportDocumentDataProvider = new TransportDocumentDataProvider();


	public HomePresenter(PlaceController placeController, EventBus eventBus, HomeView view) {
		super(placeController, eventBus, view);
	}

	@Override
	public void go(AcceptsOneWidget panel) {
		MainWidget.getInstance().setStandardView();
		super.go(panel);
	}

	@Override
	protected void setPresenterinView(HomeView view) {
		view.setPresenter(this);
	}

	@Override
	public void onLoad() {
		invoiceDataProvider.addDataDisplay(getView().getInvoiceList());
		getView().getInvoiceList().setVisibleRange(DOCS_LIST_RANGE);
		estimationDataProvider.addDataDisplay(getView().getEstimationList());
		getView().getEstimationList().setVisibleRange(DOCS_LIST_RANGE);
		creditNoteDataProvider.addDataDisplay(getView().getCreditNoteList());
		getView().getCreditNoteList().setVisibleRange(DOCS_LIST_RANGE);
		transportDocumentDataProvider.addDataDisplay(getView().getTransportDocumentList());
		getView().getTransportDocumentList().setVisibleRange(DOCS_LIST_RANGE);
		
		bind();
		getView().getInvoiceList().setEventBus(getEventBus());
		getView().getEstimationList().setEventBus(getEventBus());
		getView().getCreditNoteList().setEventBus(getEventBus());
		getView().getTransportDocumentList().setEventBus(getEventBus());
		
		updateBusinessDetails(Configuration.getBusiness());

		if(isInitialSetup){
			isInitialSetup = false;
		} else {
			getView().getInvoiceList().setVisibleRangeAndClearData(DOCS_LIST_RANGE, true);
			getView().getEstimationList().setVisibleRangeAndClearData(DOCS_LIST_RANGE, true);
			getView().getCreditNoteList().setVisibleRangeAndClearData(DOCS_LIST_RANGE, true);
			getView().getTransportDocumentList().setVisibleRangeAndClearData(DOCS_LIST_RANGE, true);
		}
		
	}

	
	@Override
	public void onNewTransportDocumentClicked() {
		SelectClientDialog scd = new SelectClientDialog(new SelectClientDialog.Handler() {

			@Override
			public void onClientSelected(final ClientDTO client) {
				NewTransportDocumentPlace p = new NewTransportDocumentPlace();
				p.setClientId(client.getId());
				goTo(p);
			}
		});
		scd.setEventBus(getEventBus());
		scd.showCentered();
	}
	
	@Override
	public void onNewEstimationClicked() {
		SelectClientDialog scd = new SelectClientDialog(new SelectClientDialog.Handler() {

			@Override
			public void onClientSelected(final ClientDTO client) {
				NewEstimationPlace p = new NewEstimationPlace();
				p.setClientId(client.getId());
				goTo(p);
			}
		});
		scd.setEventBus(getEventBus());
		scd.showCentered();
	}

	@Override
	public void onNewCreditNoteClicked() {
		SelectClientDialog scd = new SelectClientDialog(new SelectClientDialog.Handler() {

			@Override
			public void onClientSelected(final ClientDTO client) {
				NewCreditNotePlace p = new NewCreditNotePlace();
				p.setClientId(client.getId());
				goTo(p);
			}
		});
		scd.setEventBus(getEventBus());
		scd.showCentered();
	}
	
	@Override
	public void onNewInvoiceClicked() {
		SelectClientDialog scd = new SelectClientDialog(new SelectClientDialog.Handler() {

			@Override
			public void onClientSelected(final ClientDTO client) {
				NewInvoicePlace p = new NewInvoicePlace();
				p.setClientId(client.getId());
				goTo(p);
			}
		});
		scd.setEventBus(getEventBus());
		scd.showCentered();
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
	}

	private void onDocumentChangeEvent(AccountingDocumentDTO doc){
		if(doc instanceof InvoiceDTO){
			getView().getInvoiceList().setVisibleRangeAndClearData(getView().getInvoiceList().getVisibleRange(), true);
		} else if(doc instanceof EstimationDTO){
			getView().getEstimationList().setVisibleRangeAndClearData(getView().getEstimationList().getVisibleRange(), true);
		} else if(doc instanceof CreditNoteDTO){
			getView().getCreditNoteList().setVisibleRangeAndClearData(getView().getCreditNoteList().getVisibleRange(), true);
		} else if(doc instanceof TransportDocumentDTO){
			getView().getTransportDocumentList().setVisibleRangeAndClearData(getView().getTransportDocumentList().getVisibleRange(), true);
		}
	}

	private void updateBusinessDetails(BusinessDTO business){
		getView().getBusinessLogo().setUrl(Const.getLogoUrl());

		SafeHtmlBuilder shb = new SafeHtmlBuilder();
		shb.appendHtmlConstant("<p class=\"businessName\">");
		shb.appendEscaped(business.getName());
		shb.appendHtmlConstant("</p>");
		shb.appendHtmlConstant("<p class=\"businessAddress\">");
		shb.appendEscaped(business.getAddress());
		shb.appendEscaped(" "+business.getPostcode());
		shb.appendEscaped(" "+business.getCity());
		shb.appendEscaped(" ("+business.getProvince()+")");
		shb.appendEscaped(" "+business.getCountry());
		shb.appendHtmlConstant("</p>");
		shb.appendHtmlConstant("<p class=\"businessOther1\">");
		boolean needSpace = false;
		if(!business.getVatID().isEmpty()){
			needSpace = true;
			shb.appendEscaped(I18N.INSTANCE.vatID()+" "+business.getVatID());
		}
		if(!business.getVatID().isEmpty()){
			if(needSpace){
				shb.appendHtmlConstant("&nbsp;&nbsp;");
			}
			shb.appendEscaped(I18N.INSTANCE.ssn()+" "+business.getSsn());
		}
		shb.appendHtmlConstant("</p>");
		shb.appendHtmlConstant("<p class=\"businessOther2\">");
		needSpace = false;
		if(!business.getPhone().isEmpty()){
			needSpace = true;
			shb.appendEscaped(I18N.INSTANCE.phone()+" "+business.getPhone());
		}
		if(!business.getFax().isEmpty()){
			if(needSpace){
				shb.appendHtmlConstant("&nbsp;&nbsp;");
			}
			shb.appendEscaped(I18N.INSTANCE.fax()+" "+business.getFax());
		}
		shb.appendHtmlConstant("</p>");
		shb.appendHtmlConstant("<p class=\"businessOther3\">");
		needSpace = false;
		if(!business.getMobile().isEmpty()){
			needSpace = true;
			shb.appendEscaped(I18N.INSTANCE.mobile()+" "+business.getMobile());
		}
		if(!business.getWeb().isEmpty()){
			if(needSpace){
				shb.appendHtmlConstant("&nbsp;&nbsp;");
			}
			shb.appendEscaped(I18N.INSTANCE.web()+" ");
			shb.appendHtmlConstant("<a target=\"_blank\" href=\""+ 
					(business.getWeb().startsWith("http://") || business.getWeb().startsWith("https://") ? business.getWeb() : "http://"+business.getWeb()) +"\">");
			shb.appendEscaped(business.getWeb());
			shb.appendHtmlConstant("<a>");
		}
		shb.appendHtmlConstant("</p>");
		getView().getBusinessDetails().setHTML(shb.toSafeHtml());
	}

	@Override
	public void onTabBarSelected(int selectedTab) {
		getView().getTabBody().setWidget(getView().getDocumentViews().get(selectedTab));
	}

	@Override
	public void onLogoClicked() {
		goTo(new BusinessPlace());
	}



}
