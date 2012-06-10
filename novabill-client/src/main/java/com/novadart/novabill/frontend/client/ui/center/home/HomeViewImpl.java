package com.novadart.novabill.frontend.client.ui.center.home;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.Range;
import com.novadart.novabill.frontend.client.datawatcher.DataWatchEvent.DATA;
import com.novadart.novabill.frontend.client.datawatcher.DataWatchEventHandler;
import com.novadart.novabill.frontend.client.datawatcher.DataWatcher;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.ui.center.HomeView;
import com.novadart.novabill.frontend.client.ui.widget.list.impl.InvoicePreviewList;

public class HomeViewImpl extends Composite implements HomeView {

	private static HomeViewUiBinder uiBinder = GWT
			.create(HomeViewUiBinder.class);

	interface HomeViewUiBinder extends UiBinder<Widget, HomeViewImpl> {
	}

	private static final Range INVOICE_LIST_RANGE = new Range(0, 20);
	
	@UiField InvoicePreviewList invoiceList;

	private final InvoiceDataProvider invoiceDataProvider = new InvoiceDataProvider();
	
	public HomeViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		invoiceList.getCellList().setVisibleRange(INVOICE_LIST_RANGE);
		setStyleName("HomeView");
		invoiceDataProvider.addDataDisplay(invoiceList.getCellList());
		
		DataWatcher.getInstance().addDataEventHandler(new DataWatchEventHandler() {
			
			@Override
			public void onDataUpdated(DATA data) {
				if(data.equals(DATA.INVOICE)){
					invoiceList.getCellList().setVisibleRangeAndClearData(INVOICE_LIST_RANGE, true);
				}
				
			}
		});
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
