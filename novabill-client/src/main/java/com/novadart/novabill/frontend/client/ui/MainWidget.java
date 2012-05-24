package com.novadart.novabill.frontend.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.datawatcher.DataWatchEvent.DATA;
import com.novadart.novabill.frontend.client.datawatcher.DataWatchEventHandler;
import com.novadart.novabill.frontend.client.datawatcher.DataWatcher;
import com.novadart.novabill.frontend.client.facade.AuthAwareAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.BusinessPlace;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.resources.Image;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;

public class MainWidget extends Composite {

	private static MainWidgetUiBinder uiBinder = GWT
			.create(MainWidgetUiBinder.class);

	interface MainWidgetUiBinder extends UiBinder<Widget, MainWidget> {
	}

	@UiField SimplePanel centerContainer;
	@UiField SimplePanel westContainer;
	@UiField HTML businessBanner;
	@UiField HTML stats;

	private PlaceController placeController;

	public MainWidget() {
		initWidget(uiBinder.createAndBindUi(this));

		DataWatcher.getInstance().addDataEventHandler(new DataWatchEventHandler() {

			@Override
			public void onDataUpdated(DATA data) {
				switch (data) {
				case STATS:
					ServerFacade.business.getStats(new AuthAwareAsyncCallback<BusinessStatsDTO>() {

						@Override
						public void onSuccess(BusinessStatsDTO result) {
							if(result == null){
								return;
							}
							generateStats(result);
						}

						@Override
						public void onException(Throwable caught) {
						}
					});
					break;
					
				case BUSINESS:
					generateBusinessBanner();
				}
			}
		});

		generateBusinessBanner();
		generateStats(Configuration.getStats());
	}

	public void setPlaceController(PlaceController placeController) {
		this.placeController = placeController;
	}

	@UiHandler("changeButton")
	void onChangeBusinessDetailsClicked(ClickEvent e){
		this.placeController.goTo(new BusinessPlace());
	}

	@UiFactory
	I18N getI18N(){
		return I18N.get;
	}

	@UiFactory
	Image getImage(){
		return Image.get;
	}

	@UiHandler("logout")
	void onLogoutClicked(ClickEvent e){
		Window.Location.assign(GWT.getHostPageBaseURL()+"resources/j_spring_security_logout");
	}

	@UiHandler("home")
	void onHomeClicked(ClickEvent e){
		this.placeController.goTo(new HomePlace());
	}

	private void generateBusinessBanner(){
		SafeHtmlBuilder shb = new SafeHtmlBuilder();

		BusinessDTO b = Configuration.getBusiness();
		//first row
		shb.appendHtmlConstant("<span class='name'>");
		shb.appendEscaped(b.getName());
		shb.appendHtmlConstant("</span>");

		//second row
		shb.appendHtmlConstant("<span class='address'>");
		shb.appendEscaped(b.getAddress()+" - " +
				b.getPostcode()+" "+b.getCity()+" ("+b.getProvince()+")");
		shb.appendHtmlConstant("</span>");

		//third row
		shb.appendHtmlConstant("<span class='phoneAndFax'>");
		shb.appendEscaped("Tel. "+Configuration.getBusiness().getPhone()+" - Fax "+Configuration.getBusiness().getFax());
		shb.appendHtmlConstant("</span>");

		//fourth row
		shb.appendHtmlConstant("<span class='vatID'>");
		shb.appendEscaped("P.IVA "+Configuration.getBusiness().getVatID());
		shb.appendHtmlConstant("</span>");

		businessBanner.setHTML(shb.toSafeHtml());
	}

	public SimplePanel getCenterContainer() {
		return centerContainer;
	}

	public SimplePanel getWestContainer() {
		return westContainer;
	}

	private void generateStats(BusinessStatsDTO statistics){
		SafeHtmlBuilder shb = new SafeHtmlBuilder();

		//first row
		shb.appendHtmlConstant("<span class='clients'>");
		shb.appendEscaped(I18N.get.totalClients());
		shb.appendHtmlConstant("<span class='value'>");
		shb.append(statistics.getClientsCount());
		shb.appendHtmlConstant("</span>");
		shb.appendHtmlConstant("</span>");

		//second row
		shb.appendHtmlConstant("<span class='invoices'>");
		shb.appendEscaped(I18N.get.totalInvoices());
		shb.appendHtmlConstant("<span class='value'>");
		shb.append(statistics.getInvoicesCountForYear());
		shb.appendHtmlConstant("</span>");
		shb.appendHtmlConstant("</span>");

		//third row
		shb.appendHtmlConstant("<span class='totalAmount'>");
		shb.appendEscaped(I18N.get.totalInvoicing());
		shb.appendHtmlConstant("<span class='value'>");
		shb.appendEscaped(NumberFormat.getCurrencyFormat().format(statistics.getTotalAfterTaxesForYear()));
		shb.appendHtmlConstant("</span>");
		shb.appendHtmlConstant("</span>");

		stats.setHTML(shb.toSafeHtml());
	}

}
