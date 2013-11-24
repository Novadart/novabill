package com.novadart.novabill.frontend.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.event.ClientAddEvent;
import com.novadart.novabill.frontend.client.event.ClientAddHandler;
import com.novadart.novabill.frontend.client.event.ClientDeleteEvent;
import com.novadart.novabill.frontend.client.event.ClientDeleteHandler;
import com.novadart.novabill.frontend.client.event.DocumentAddEvent;
import com.novadart.novabill.frontend.client.event.DocumentAddHandler;
import com.novadart.novabill.frontend.client.event.DocumentDeleteEvent;
import com.novadart.novabill.frontend.client.event.DocumentDeleteHandler;
import com.novadart.novabill.frontend.client.event.DocumentUpdateEvent;
import com.novadart.novabill.frontend.client.event.DocumentUpdateHandler;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.BusinessPlace;
import com.novadart.novabill.frontend.client.place.HomePlace;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.resources.GlobalCss;
import com.novadart.novabill.frontend.client.resources.ImageResources;
import com.novadart.novabill.frontend.client.view.feedback.FeedbackDialog;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;

public class MainWidgetImpl extends Composite implements MainWidget {
	
	interface Style extends CssResource {
		String logo();
		String menuBar();
		String westContainer();
		String myDataButton();
		String stats();
		String exitButton();
		String header();
		String homeButton();
		String logoBar();
		String centerContainer();
		String value();
		String totalAmount();
		String changePassword();
		String invoices();
		String productName();
		String clients();
		String feedbackButton();
	}

	private static MainWidgetImplUiBinder uiBinder = GWT
			.create(MainWidgetImplUiBinder.class);

	interface MainWidgetImplUiBinder extends UiBinder<Widget, MainWidgetImpl> {
	}

	private static MainWidget instance;

	public static MainWidget getInstance() {
		return instance;
	}
	@UiField DockLayoutPanel dockWidget;

	@UiField SimplePanel centerContainer;
	@UiField SimplePanel westContainer;
	@UiField FlowPanel menuBar;
	@UiField HTML stats;
	@UiField Anchor logout;
	@UiField Anchor logoAnchor;
	@UiField(provided=true) protected Anchor changePasswordAnchor;
	@UiField Style style;

	private PlaceController placeController;

	public MainWidgetImpl() {
		GlobalBundle.INSTANCE.globalCss().ensureInjected();
		changePasswordAnchor = new Anchor(I18N.INSTANCE.changePassword(), ClientFactory.INSTANCE.getChangePasswordUrl());

		initWidget(uiBinder.createAndBindUi(this));
		logout.setHref(ClientFactory.INSTANCE.getLogoutUrl());
		logoAnchor.setHref(GWT.getHostPageBaseURL());

//		generateStats(Configuration.getStats());

		instance = this;
	}
	
	@Override
	public void setEventBus(EventBus eventBus) {
		eventBus.addHandler(DocumentUpdateEvent.TYPE, new DocumentUpdateHandler() {

			@Override
			public void onDocumentUpdate(DocumentUpdateEvent event) {
				onDocumentChangeEvent();
			}
			
		});
		eventBus.addHandler(DocumentDeleteEvent.TYPE, new DocumentDeleteHandler() {
			
			@Override
			public void onDocumentDelete(DocumentDeleteEvent event) {
				onDocumentChangeEvent();
			}
		});
		
		eventBus.addHandler(DocumentAddEvent.TYPE, new DocumentAddHandler() {
			
			@Override
			public void onDocumentAdd(DocumentAddEvent event) {
				onDocumentChangeEvent();
			}
		});
		eventBus.addHandler(ClientDeleteEvent.TYPE, new ClientDeleteHandler() {
			
			@Override
			public void onClientDelete(ClientDeleteEvent event) {
				onDocumentChangeEvent();
			}
		});
		eventBus.addHandler(ClientAddEvent.TYPE, new ClientAddHandler() {
			
			@Override
			public void onClientAdd(ClientAddEvent event) {
				onDocumentChangeEvent();
			}
		});
	}
	
	private void onDocumentChangeEvent(){
		ServerFacade.INSTANCE.getBusinessService().getStats(Configuration.getBusinessId(), new ManagedAsyncCallback<BusinessStatsDTO>() {

			@Override
			public void onSuccess(BusinessStatsDTO result) {
				if(result == null){
					return;
				}
				
//				Configuration.setStats(result);
				
				generateStats(result);
			}

		});
	}

	@Override
	public void setLargeView(){
		dockWidget.setWidgetSize(westContainer, 15);
	}

	@Override
	public void setStandardView(){
		dockWidget.setWidgetSize(westContainer, 25);
	}

	@Override
	public void setPlaceController(PlaceController placeController) {
		this.placeController = placeController;
	}

	@UiFactory
	I18N getI18N(){
		return I18N.INSTANCE;
	}
	
	@UiFactory
	GlobalCss getGlobalCss(){
		return GlobalBundle.INSTANCE.globalCss();
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
	protected void onFeedbackClicked(ClickEvent e){
		GWT.runAsync(new RunAsyncCallback() {
			
			@Override
			public void onSuccess() {
				FeedbackDialog feedbackDialog = new FeedbackDialog();
				feedbackDialog.showCentered();
			}
			
			@Override
			public void onFailure(Throwable reason) {
				I18N.INSTANCE.errorServerCommunication();
			}
		});
	}

	@UiHandler("myData")
	void onMyDataClicked(ClickEvent e){
		this.placeController.goTo(new BusinessPlace());
	}

	@Override
	public SimplePanel getCenterContainer() {
		return centerContainer;
	}

	@Override
	public SimplePanel getWestContainer() {
		return westContainer;
	}

	private void generateStats(BusinessStatsDTO statistics){
		SafeHtmlBuilder shb = new SafeHtmlBuilder();

		//first row
		shb.appendHtmlConstant("<span class='"+style.clients()+"'>");
		shb.appendEscaped(I18N.INSTANCE.totalClients());
		shb.appendHtmlConstant("<span class='"+style.value()+"'>");
		shb.append(statistics.getClientsCount());
		shb.appendHtmlConstant("</span>");
		shb.appendHtmlConstant("</span>");

		//second row
		shb.appendHtmlConstant("<span class='"+style.invoices()+"'>");
		shb.appendEscaped(I18N.INSTANCE.totalInvoices());
		shb.appendHtmlConstant("<span class='"+style.value()+"'>");
		shb.append(statistics.getInvoicesCountForYear());
		shb.appendHtmlConstant("</span>");
		shb.appendHtmlConstant("</span>");

		//third row
		shb.appendHtmlConstant("<span class='"+style.totalAmount()+"'>");
		shb.appendEscaped(I18N.INSTANCE.totalInvoicing());
		shb.appendHtmlConstant("<span class='"+style.value()+"'>");
		shb.appendEscaped(NumberFormat.getCurrencyFormat().format(statistics.getTotalAfterTaxesForYear()));
		shb.appendHtmlConstant("</span>");
		shb.appendHtmlConstant("</span>");

		stats.setHTML(shb.toSafeHtml());
	}
	
	protected FlowPanel getMenuBar() {
		return menuBar;
	}

}