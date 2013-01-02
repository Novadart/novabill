package com.novadart.novabill.frontend.client.ui.center.home;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.Range;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.datawatcher.DataWatchEvent.DATA;
import com.novadart.novabill.frontend.client.datawatcher.DataWatchEventHandler;
import com.novadart.novabill.frontend.client.datawatcher.DataWatcher;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.CreditNotePlace;
import com.novadart.novabill.frontend.client.place.EstimationPlace;
import com.novadart.novabill.frontend.client.place.InvoicePlace;
import com.novadart.novabill.frontend.client.place.TransportDocumentPlace;
import com.novadart.novabill.frontend.client.ui.center.HomeView;
import com.novadart.novabill.frontend.client.ui.widget.dialog.SelectClientDialog;
import com.novadart.novabill.frontend.client.ui.widget.list.ShowMoreButton;
import com.novadart.novabill.frontend.client.ui.widget.list.impl.CreditNoteList;
import com.novadart.novabill.frontend.client.ui.widget.list.impl.EstimationList;
import com.novadart.novabill.frontend.client.ui.widget.list.impl.InvoiceList;
import com.novadart.novabill.frontend.client.ui.widget.list.impl.TransportDocumentList;
import com.novadart.novabill.frontend.client.ui.widget.notification.Notification;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;

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
	@UiField(provided=true) HTML welcome;
	@UiField(provided=true) SimplePanel tabBody;
	@UiField Label welcomeMessage;

	private final Map<Integer, FlowPanel> lists = new HashMap<Integer, FlowPanel>();

	private Presenter presenter;

	private final InvoiceDataProvider invoiceDataProvider = new InvoiceDataProvider();
	private final EstimationDataProvider estimationDataProvider = new EstimationDataProvider();
	private final CreditNoteDataProvider creditNoteDataProvider = new CreditNoteDataProvider();
	private final TransportDocumentDataProvider transportDocumentDataProvider = new TransportDocumentDataProvider();

	public HomeViewImpl() {
		date = setupDate();
		welcome = setupWelcomeMessage();
		tabBody = new SimplePanel();
		setupLists();

		initWidget(uiBinder.createAndBindUi(this));

		tabBar.addTab(I18N.INSTANCE.invoices());
		tabBar.addTab(I18N.INSTANCE.estimates());
		tabBar.addTab(I18N.INSTANCE.creditNote());
		tabBar.addTab(I18N.INSTANCE.transportDocumentsTab());
		tabBar.selectTab(0);

		setStyleName("HomeView");

		DataWatcher.getInstance().addDataEventHandler(new DataWatchEventHandler() {

			@Override
			public void onDataUpdated(DATA data) {
				switch (data) {
				case INVOICE:
					invoiceList.setVisibleRangeAndClearData(DOCS_LIST_RANGE, true);
					break;

				case ESTIMATION:
					estimationList.setVisibleRangeAndClearData(DOCS_LIST_RANGE, true);
					break;

				case CREDIT_NOTE:
					creditNoteList.setVisibleRangeAndClearData(DOCS_LIST_RANGE, true);
					break;

				case TRANSPORT_DOCUMENT:
					transportDocumentList.setVisibleRangeAndClearData(DOCS_LIST_RANGE, true);
					break;

				case CLIENT_DATA:
					invoiceList.setVisibleRangeAndClearData(invoiceList.getVisibleRange(), true);
					estimationList.setVisibleRangeAndClearData(estimationList.getVisibleRange(), true);
					creditNoteList.setVisibleRangeAndClearData(creditNoteList.getVisibleRange(), true);
					transportDocumentList.setVisibleRangeAndClearData(transportDocumentList.getVisibleRange(), true);
					break;

				case STATS:
					ServerFacade.business.getStats(Configuration.getBusinessId(), new WrappedAsyncCallback<BusinessStatsDTO>() {

						@Override
						public void onSuccess(BusinessStatsDTO result) {
							if(result == null){
								return;
							}
							if(result.getInvoicesCountForYear() > 0){
								welcomeMessage.setVisible(false);
								tabBody.setVisible(true);
							}
						}

						@Override
						public void onException(Throwable caught) {
						}
					});
					break;

				default:
					break;
				}
			}
		});
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		welcomeMessage.setVisible(invoiceList.getVisibleItemCount()
				+estimationList.getVisibleItemCount()
				+transportDocumentList.getVisibleItemCount()
				+creditNoteList.getVisibleItemCount() == 0);
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


	private HTML setupWelcomeMessage() {
		HTML welcomeMessage = new HTML();
		SafeHtmlBuilder shb = new SafeHtmlBuilder();
		shb.appendHtmlConstant("<p>"+I18N.INSTANCE.welcomeMessage1()+"</p>");
		shb.appendHtmlConstant("<p>"+I18N.INSTANCE.welcomeMessage2()+"</p>");
		shb.appendHtmlConstant("<p>"+I18N.INSTANCE.welcomeMessage3()+"</p>");
		welcomeMessage.setHTML(shb.toSafeHtml());
		return welcomeMessage;
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

		welcomeMessage.setVisible(invoiceList.getVisibleItemCount()
				+estimationList.getVisibleItemCount()
				+transportDocumentList.getVisibleItemCount()
				+creditNoteList.getVisibleItemCount() == 0);
		tabBody.setWidget(lists.get(selectedTab));
	}

	@UiHandler("newInvoice")
	void onNewInvoiceClicked(ClickEvent e) {
		SelectClientDialog scd = new SelectClientDialog(new SelectClientDialog.Handler() {

			@Override
			public void onClientSelected(final ClientDTO client) {
				ServerFacade.invoice.getNextInvoiceDocumentID(new WrappedAsyncCallback<Long>() {

					@Override
					public void onSuccess(Long result) {
						InvoicePlace ip = new InvoicePlace();
						ip.setDataForNewInvoice(client, result);
						presenter.goTo(ip);
					}

					@Override
					public void onException(Throwable caught) {
						Notification.showMessage(I18N.INSTANCE.errorServerCommunication());
					}
				});
			}
		});
		scd.showCentered();
	}


	@UiHandler("newCreditNote")
	void onNewCreditNoteClicked(ClickEvent e) {
		SelectClientDialog scd = new SelectClientDialog(new SelectClientDialog.Handler() {

			@Override
			public void onClientSelected(final ClientDTO client) {
				ServerFacade.creditNote.getNextCreditNoteDocumentID(new WrappedAsyncCallback<Long>() {

					@Override
					public void onSuccess(Long result) {
						CreditNotePlace cnp = new CreditNotePlace();
						cnp.setDataForNewCreditNote(client, result);
						presenter.goTo(cnp);
					}

					@Override
					public void onException(Throwable caught) {
						Notification.showMessage(I18N.INSTANCE.errorServerCommunication());
					}
				});
			}
		});
		scd.showCentered();
	}


	@UiHandler("newEstimation")
	void onNewEstimationClicked(ClickEvent e) {
		SelectClientDialog scd = new SelectClientDialog(new SelectClientDialog.Handler() {

			@Override
			public void onClientSelected(final ClientDTO client) {
				ServerFacade.estimation.getNextEstimationId(new WrappedAsyncCallback<Long>() {

					@Override
					public void onSuccess(Long result) {
						EstimationPlace ep = new EstimationPlace();
						ep.setDataForNewEstimation(client, result);
						presenter.goTo(ep);
					}

					@Override
					public void onException(Throwable caught) {
						Notification.showMessage(I18N.INSTANCE.errorServerCommunication());
					}
				});

			}
		});
		scd.showCentered();
	}


	@UiHandler("newTransportDocument")
	void onNewTransportDocumentClicked(ClickEvent e) {
		SelectClientDialog scd = new SelectClientDialog(new SelectClientDialog.Handler() {

			@Override
			public void onClientSelected(final ClientDTO client) {
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
		});
		scd.showCentered();
	}

}
