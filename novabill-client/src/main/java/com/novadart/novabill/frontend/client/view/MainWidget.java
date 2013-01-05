package com.novadart.novabill.frontend.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.Const;
import com.novadart.novabill.frontend.client.datawatcher.DataWatchEvent.DATA;
import com.novadart.novabill.frontend.client.datawatcher.DataWatchEventHandler;
import com.novadart.novabill.frontend.client.datawatcher.DataWatcher;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.BusinessPlace;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.resources.ImageResources;
import com.novadart.novabill.frontend.client.view.feedback.FeedbackDialog;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;

public class MainWidget extends Composite {

	private static MainWidgetUiBinder uiBinder = GWT
			.create(MainWidgetUiBinder.class);

	interface MainWidgetUiBinder extends UiBinder<Widget, MainWidget> {
	}

	private static MainWidget instance;

	public static MainWidget getInstance() {
		return instance;
	}
	@UiField DockLayoutPanel dockWidget;

	@UiField SimplePanel centerContainer;
	@UiField SimplePanel westContainer;
	@UiField HTML businessBanner;
	@UiField HTML stats;
	@UiField Anchor logout;
	@UiField Anchor logoAnchor;
	@UiField(provided=true) Anchor changePasswordAnchor;

	private PlaceController placeController;

	public MainWidget() {
		changePasswordAnchor = new Anchor(I18N.INSTANCE.changePassword(), Const.CHANGE_PASSWORD_URL);

		initWidget(uiBinder.createAndBindUi(this));
		logout.setHref(GWT.getHostPageBaseURL()+"resources/j_spring_security_logout");
		logoAnchor.setHref(GWT.getHostPageBaseURL());

		DataWatcher.getInstance().addDataEventHandler(new DataWatchEventHandler() {

			@Override
			public void onDataUpdated(DATA data) {
				switch (data) {
				case STATS:
					ServerFacade.business.getStats(Configuration.getBusinessId(), new WrappedAsyncCallback<BusinessStatsDTO>() {

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
					break;

				default:
					break;
				}
			}
		});

		generateBusinessBanner();
		generateStats(Configuration.getStats());

		instance = this;
	}

	public void setLargeView(){
		dockWidget.setWidgetSize(westContainer, 15);
	}

	public void setStandardView(){
		dockWidget.setWidgetSize(westContainer, 25);
	}

	public void setPlaceController(PlaceController placeController) {
		this.placeController = placeController;
	}

	@UiFactory
	I18N getI18N(){
		return I18N.INSTANCE;
	}

	@UiFactory
	ImageResources getImage(){
		return ImageResources.INSTANCE;
	}

	@UiHandler("home")
	void onHomeClicked(ClickEvent e){
		this.placeController.goTo(new HomePlace());
	}
	
	@UiHandler("feedback")
	void onFeedbackClicked(ClickEvent e){
		FeedbackDialog feedbackDialog = new FeedbackDialog();
		feedbackDialog.showCentered();
	}

	@UiHandler("myData")
	void onMyDataClicked(ClickEvent e){
		this.placeController.goTo(new BusinessPlace());
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
		shb.appendEscaped(I18N.INSTANCE.totalClients());
		shb.appendHtmlConstant("<span class='value'>");
		shb.append(statistics.getClientsCount());
		shb.appendHtmlConstant("</span>");
		shb.appendHtmlConstant("</span>");

		//second row
		shb.appendHtmlConstant("<span class='invoices'>");
		shb.appendEscaped(I18N.INSTANCE.totalInvoices());
		shb.appendHtmlConstant("<span class='value'>");
		shb.append(statistics.getInvoicesCountForYear());
		shb.appendHtmlConstant("</span>");
		shb.appendHtmlConstant("</span>");

		//third row
		shb.appendHtmlConstant("<span class='totalAmount'>");
		shb.appendEscaped(I18N.INSTANCE.totalInvoicing());
		shb.appendHtmlConstant("<span class='value'>");
		shb.appendEscaped(NumberFormat.getCurrencyFormat().format(statistics.getTotalAfterTaxesForYear()));
		shb.appendHtmlConstant("</span>");
		shb.appendHtmlConstant("</span>");

		stats.setHTML(shb.toSafeHtml());
	}

}
