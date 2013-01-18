package com.novadart.novabill.frontend.client.view.center.home;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.Range;
import com.novadart.novabill.frontend.client.event.DocumentAddEvent;
import com.novadart.novabill.frontend.client.event.DocumentAddHandler;
import com.novadart.novabill.frontend.client.event.DocumentDeleteEvent;
import com.novadart.novabill.frontend.client.event.DocumentDeleteHandler;
import com.novadart.novabill.frontend.client.event.DocumentUpdateEvent;
import com.novadart.novabill.frontend.client.event.DocumentUpdateHandler;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.creditnote.NewCreditNotePlace;
import com.novadart.novabill.frontend.client.place.estimation.NewEstimationPlace;
import com.novadart.novabill.frontend.client.place.invoice.NewInvoicePlace;
import com.novadart.novabill.frontend.client.place.transportdocument.NewTransportDocumentPlace;
import com.novadart.novabill.frontend.client.view.center.HomeView;
import com.novadart.novabill.frontend.client.widget.dialog.SelectClientDialog;
import com.novadart.novabill.frontend.client.widget.list.ShowMoreButton;
import com.novadart.novabill.frontend.client.widget.list.impl.CreditNoteList;
import com.novadart.novabill.frontend.client.widget.list.impl.EstimationList;
import com.novadart.novabill.frontend.client.widget.list.impl.InvoiceList;
import com.novadart.novabill.frontend.client.widget.list.impl.TransportDocumentList;
import com.novadart.novabill.frontend.client.widget.tip.TipFactory;
import com.novadart.novabill.frontend.client.widget.tip.Tips;
import com.novadart.novabill.shared.client.dto.AccountingDocumentDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public class HomeViewImpl extends Composite implements HomeView {

	private static final int DOCS_PAGE_SIZE = 10;


	private static HomeViewUiBinder uiBinder = GWT
			.create(HomeViewUiBinder.class);

	interface HomeViewUiBinder extends UiBinder<Widget, HomeViewImpl> {
	}

	private static final Range DOCS_LIST_RANGE = new Range(0, DOCS_PAGE_SIZE);

	@UiField TabBar tabBar;

	private final InvoiceList invoiceList = new InvoiceList();
	private final EstimationList estimationList = new EstimationList();
	private final CreditNoteList creditNoteList = new CreditNoteList();
	private final TransportDocumentList transportDocumentList = new TransportDocumentList();
	@UiField(provided=true) HTML date;
	@UiField SimplePanel tipWelcome;
	@UiField SimplePanel tipDocs;
	@UiField(provided=true) SimplePanel tabBody;

	private final Map<Integer, FlowPanel> lists = new HashMap<Integer, FlowPanel>();

	private Presenter presenter;
	private EventBus eventBus;

	private final InvoiceDataProvider invoiceDataProvider = new InvoiceDataProvider();
	private final EstimationDataProvider estimationDataProvider = new EstimationDataProvider();
	private final CreditNoteDataProvider creditNoteDataProvider = new CreditNoteDataProvider();
	private final TransportDocumentDataProvider transportDocumentDataProvider = new TransportDocumentDataProvider();
	
	private boolean isInitialSetup = true;

	public HomeViewImpl() {
		date = setupDate();
		tabBody = new SimplePanel();
		setupLists();

		initWidget(uiBinder.createAndBindUi(this));

		tabBar.addTab(I18N.INSTANCE.invoices());
		tabBar.addTab(I18N.INSTANCE.estimates());
		tabBar.addTab(I18N.INSTANCE.creditNote());
		tabBar.addTab(I18N.INSTANCE.transportDocumentsTab());
		tabBar.selectTab(0);
		
		TipFactory.show(Tips.center_home_welcome, tipWelcome);
		TipFactory.show(Tips.center_home_yourdocs, tipDocs);
		
		setStyleName("HomeView");
	}
	
	@Override
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
		bind();
		invoiceList.setEventBus(eventBus);
		estimationList.setEventBus(eventBus);
		creditNoteList.setEventBus(eventBus);
		transportDocumentList.setEventBus(eventBus);
	}
	
	private void bind(){
		eventBus.addHandler(DocumentAddEvent.TYPE, new DocumentAddHandler() {

			@Override
			public void onDocumentAdd(DocumentAddEvent event) {
				onDocumentChangeEvent(event.getDocument());
			}
		});
		eventBus.addHandler(DocumentDeleteEvent.TYPE, new DocumentDeleteHandler() {

			@Override
			public void onDocumentDelete(DocumentDeleteEvent event) {
				onDocumentChangeEvent(event.getDocument());
			}
		});
		eventBus.addHandler(DocumentUpdateEvent.TYPE, new DocumentUpdateHandler() {

			@Override
			public void onDocumentUpdate(DocumentUpdateEvent event) {
				onDocumentChangeEvent(event.getDocument());
			}
		});
	}
	
	private void onDocumentChangeEvent(AccountingDocumentDTO doc){
		if(HomeViewImpl.this.isAttached()){
			if(doc instanceof InvoiceDTO){
				invoiceList.setVisibleRangeAndClearData(invoiceList.getVisibleRange(), true);
			} else if(doc instanceof EstimationDTO){
				estimationList.setVisibleRangeAndClearData(estimationList.getVisibleRange(), true);
			} else if(doc instanceof CreditNoteDTO){
				creditNoteList.setVisibleRangeAndClearData(creditNoteList.getVisibleRange(), true);
			} else if(doc instanceof TransportDocumentDTO){
				transportDocumentList.setVisibleRangeAndClearData(transportDocumentList.getVisibleRange(), true);
			}
		}
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		
		if(isInitialSetup){
			isInitialSetup = false;
		} else {
			invoiceList.setVisibleRangeAndClearData(DOCS_LIST_RANGE, true);
			estimationList.setVisibleRangeAndClearData(DOCS_LIST_RANGE, true);
			creditNoteList.setVisibleRangeAndClearData(DOCS_LIST_RANGE, true);
			transportDocumentList.setVisibleRangeAndClearData(DOCS_LIST_RANGE, true);
		}
	}

	private HTML setupDate() {
		final HTML dateBox = new HTML();
		final DateTimeFormat date = DateTimeFormat.getFormat("EEEE, dd MMMM yyyy");
		final DateTimeFormat time = DateTimeFormat.getFormat("HH:mm");

		Timer updater = new Timer() {

			@Override
			public void run() {
				SafeHtmlBuilder shb = new SafeHtmlBuilder();
				Date d = new Date();
				shb.appendHtmlConstant("<span class='date'>"+date.format(d)+"</span>");
				shb.appendHtmlConstant("<span class='time'>"+time.format(d)+"</span>");
				dateBox.setHTML(shb.toSafeHtml());
			}
		};

		updater.scheduleRepeating(1000);

		return dateBox;
	}


	private void setupLists() {
		FlowPanel fp = new FlowPanel();
		fp.setStyleName("listWrapper panel");
		ShowMoreButton sb = new ShowMoreButton(DOCS_PAGE_SIZE);
		sb.setDisplay(invoiceList);
		sb.addStyleNameToButton("action-button");
		fp.add(invoiceList);
		fp.add(sb);
		invoiceList.setVisibleRange(DOCS_LIST_RANGE);
		invoiceDataProvider.addDataDisplay(invoiceList);
		lists.put(0, fp);

		fp = new FlowPanel();
		fp.setStyleName("listWrapper panel");
		sb = new ShowMoreButton(DOCS_PAGE_SIZE);
		sb.setDisplay(estimationList);
		sb.addStyleNameToButton("action-button");
		fp.add(estimationList);
		fp.add(sb);
		estimationList.setVisibleRange(DOCS_LIST_RANGE);
		estimationDataProvider.addDataDisplay(estimationList);
		lists.put(1, fp);

		fp = new FlowPanel();
		fp.setStyleName("listWrapper panel");
		sb = new ShowMoreButton(DOCS_PAGE_SIZE);
		sb.setDisplay(creditNoteList);
		sb.addStyleNameToButton("action-button");
		fp.add(creditNoteList);
		fp.add(sb);
		creditNoteList.setVisibleRange(DOCS_LIST_RANGE);
		creditNoteDataProvider.addDataDisplay(creditNoteList);
		lists.put(2, fp);

		fp = new FlowPanel();
		fp.setStyleName("listWrapper panel");
		sb = new ShowMoreButton(DOCS_PAGE_SIZE);
		sb.setDisplay(transportDocumentList);
		sb.addStyleNameToButton("action-button");
		fp.add(transportDocumentList);
		fp.add(sb);
		transportDocumentList.setVisibleRange(DOCS_LIST_RANGE);
		transportDocumentDataProvider.addDataDisplay(transportDocumentList);
		lists.put(3, fp);
	}


	@UiFactory
	I18N getI18N(){
		return I18N.INSTANCE;
	}

	@Override
	public void setClean(){
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		invoiceList.setPresenter(presenter);
		estimationList.setPresenter(presenter);
		creditNoteList.setPresenter(presenter);
		transportDocumentList.setPresenter(presenter);
	}

	@UiHandler("tabBar")
	void onTabBarSelected(SelectionEvent<Integer> event) {
		int selectedTab = event.getSelectedItem();
		tabBody.setWidget(lists.get(selectedTab));
	}

	@UiHandler("newInvoice")
	void onNewInvoiceClicked(ClickEvent e) {
		SelectClientDialog scd = new SelectClientDialog(new SelectClientDialog.Handler() {

			@Override
			public void onClientSelected(final ClientDTO client) {
				NewInvoicePlace p = new NewInvoicePlace();
				p.setClientId(client.getId());
				presenter.goTo(p);
			}
		});
		scd.setEventBus(eventBus);
		scd.showCentered();
	}


	@UiHandler("newCreditNote")
	void onNewCreditNoteClicked(ClickEvent e) {
		SelectClientDialog scd = new SelectClientDialog(new SelectClientDialog.Handler() {

			@Override
			public void onClientSelected(final ClientDTO client) {
				NewCreditNotePlace p = new NewCreditNotePlace();
				p.setClientId(client.getId());
				presenter.goTo(p);
			}
		});
		scd.setEventBus(eventBus);
		scd.showCentered();
	}


	@UiHandler("newEstimation")
	void onNewEstimationClicked(ClickEvent e) {
		SelectClientDialog scd = new SelectClientDialog(new SelectClientDialog.Handler() {

			@Override
			public void onClientSelected(final ClientDTO client) {
				NewEstimationPlace p = new NewEstimationPlace();
				p.setClientId(client.getId());
				presenter.goTo(p);
			}
		});
		scd.setEventBus(eventBus);
		scd.showCentered();
	}


	@UiHandler("newTransportDocument")
	void onNewTransportDocumentClicked(ClickEvent e) {
		SelectClientDialog scd = new SelectClientDialog(new SelectClientDialog.Handler() {

			@Override
			public void onClientSelected(final ClientDTO client) {
				NewTransportDocumentPlace p = new NewTransportDocumentPlace();
				p.setClientId(client.getId());
				presenter.goTo(p);
			}
		});
		scd.setEventBus(eventBus);
		scd.showCentered();
	}

}
