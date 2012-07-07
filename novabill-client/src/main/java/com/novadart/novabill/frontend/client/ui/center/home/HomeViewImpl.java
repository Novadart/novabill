package com.novadart.novabill.frontend.client.ui.center.home;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.Range;
import com.novadart.novabill.frontend.client.datawatcher.DataWatchEvent.DATA;
import com.novadart.novabill.frontend.client.datawatcher.DataWatchEventHandler;
import com.novadart.novabill.frontend.client.datawatcher.DataWatcher;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.ui.center.HomeView;
import com.novadart.novabill.frontend.client.ui.widget.list.ShowMoreButton;
import com.novadart.novabill.frontend.client.ui.widget.list.impl.InvoiceList;

public class HomeViewImpl extends Composite implements HomeView {
	
	private static final int INVOICELIST_PAGE_SIZE = 30;
	

	private static HomeViewUiBinder uiBinder = GWT
			.create(HomeViewUiBinder.class);

	interface HomeViewUiBinder extends UiBinder<Widget, HomeViewImpl> {
	}

	private static final Range INVOICE_LIST_RANGE = new Range(0, INVOICELIST_PAGE_SIZE);
	
	@UiField InvoiceList invoiceList;
	@UiField(provided=true) HTML date;
	@UiField(provided=true) ShowMoreButton showMore;

	private final InvoiceDataProvider invoiceDataProvider = new InvoiceDataProvider();
	
	public HomeViewImpl() {
		date = setupDate();
		showMore = new ShowMoreButton(INVOICELIST_PAGE_SIZE);
		initWidget(uiBinder.createAndBindUi(this));
		showMore.setDisplay(invoiceList);
		showMore.addStyleNameToButton("button");
		invoiceList.setVisibleRange(INVOICE_LIST_RANGE);
		setStyleName("HomeView");
		invoiceDataProvider.addDataDisplay(invoiceList);
		
		DataWatcher.getInstance().addDataEventHandler(new DataWatchEventHandler() {
			
			@Override
			public void onDataUpdated(DATA data) {
				if(data.equals(DATA.INVOICE)){
					invoiceList.setVisibleRangeAndClearData(INVOICE_LIST_RANGE, true);
				}
			}
		});
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
		
		updater.schedule(1000);
		
		return dateBox;
	}


	@UiFactory
	I18N getI18N(){
		return I18N.INSTANCE;
	}

	public void setClean(){
	}

	@Override
	public void setPresenter(Presenter presenter) {
		invoiceList.setPresenter(presenter);
	}
	
}
