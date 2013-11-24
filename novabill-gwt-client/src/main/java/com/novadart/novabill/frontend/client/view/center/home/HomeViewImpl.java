package com.novadart.novabill.frontend.client.view.center.home;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.novabill.frontend.client.ClientFactory;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.resources.GlobalCss;
import com.novadart.novabill.frontend.client.widget.list.ShowMoreButton;
import com.novadart.novabill.frontend.client.widget.list.impl.CreditNoteList;
import com.novadart.novabill.frontend.client.widget.list.impl.EstimationList;
import com.novadart.novabill.frontend.client.widget.list.impl.InvoiceList;
import com.novadart.novabill.frontend.client.widget.list.impl.TransportDocumentList;
import com.novadart.novabill.frontend.client.widget.tip.TipFactory;
import com.novadart.novabill.frontend.client.widget.tip.Tips;
import com.novadart.novabill.shared.client.dto.BusinessDTO;

public class HomeViewImpl extends Composite implements HomeView {

	private static final int DOCS_PAGE_SIZE = 10;

	private static HomeViewUiBinder uiBinder = GWT
			.create(HomeViewUiBinder.class);

	interface HomeViewUiBinder extends UiBinder<Widget, HomeViewImpl> {
	}
	
	public interface ShowMoreStyle extends ShowMoreButton.Style {
		String showmore();
	}
	
	public interface Style extends CssResource {
		String businessName();
		String homeBody();
		String businessDetails();
		String invoicesTitle();
		String date();
		String businessPanel();
		String newCreditNote();
		String newTransportDocument();
		String currentDate();
		String scrollHome();
		String listWrapper();
		String newEstimation();
		String tabPanel();
		String businessLogo();
		String yourDocsLabel();
		String actions();
	}

	@UiField TabBar tabBar;

	private final InvoiceList invoiceList = new InvoiceList();
	private final EstimationList estimationList = new EstimationList();
	private final CreditNoteList creditNoteList = new CreditNoteList();
	private final TransportDocumentList transportDocumentList = new TransportDocumentList();
	@UiField(provided=true) HTML date;
	@UiField SimplePanel tipWelcome;
	@UiField SimplePanel tipDocs;
	@UiField(provided=true) SimplePanel tabBody;
	@UiField Image businessLogo;
	@UiField HTML businessDetails;
	
	@UiField ShowMoreStyle sm;
	@UiField Style s;
	
	private final Map<Integer, FlowPanel> documentViews = new HashMap<Integer, FlowPanel>();

	private Presenter presenter;

	public HomeViewImpl() {
		date = setupDate();
		tabBody = new SimplePanel();
		initWidget(uiBinder.createAndBindUi(this));

		setupLists();
		
		tabBar.addTab(I18N.INSTANCE.invoices());
		tabBar.addTab(I18N.INSTANCE.estimates());
		tabBar.addTab(I18N.INSTANCE.creditNote());
		tabBar.addTab(I18N.INSTANCE.transportDocumentsTab());
		
		TipFactory.show(Tips.center_home_welcome, tipWelcome);
		TipFactory.show(Tips.center_home_yourdocs, tipDocs);
		
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		presenter.onLoad();
		updateBusinessDetails(Configuration.getBusiness());
		tabBar.selectTab(0);
	}
	
	private void updateBusinessDetails(BusinessDTO business){
		businessLogo.setUrl(ClientFactory.INSTANCE.getLogoUrl());

		SafeHtmlBuilder shb = new SafeHtmlBuilder();
		shb.appendHtmlConstant("<p class='"+s.businessName()+"'>");
		shb.appendEscaped(business.getName());
		shb.appendHtmlConstant("</p>");
		shb.appendHtmlConstant("<p>");
		shb.appendEscaped(business.getAddress());
		shb.appendEscaped(" "+business.getPostcode());
		shb.appendEscaped(" "+business.getCity());
		shb.appendEscaped(" ("+business.getProvince()+")");
		shb.appendEscaped(" "+business.getCountry());
		shb.appendHtmlConstant("</p>");
		shb.appendHtmlConstant("<p>");
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
		shb.appendHtmlConstant("<p>");
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
		shb.appendHtmlConstant("<p>");
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
			shb.appendHtmlConstant("<a target='_blank' href='"+ 
					(business.getWeb().startsWith("http://") || business.getWeb().startsWith("https://") ? business.getWeb() : "http://"+business.getWeb()) +"'>");
			shb.appendEscaped(business.getWeb());
			shb.appendHtmlConstant("<a>");
		}
		shb.appendHtmlConstant("</p>");
		businessDetails.setHTML(shb.toSafeHtml());
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
				shb.appendHtmlConstant("<span class='"+s.date()+"'>"+date.format(d)+"</span>");
				shb.appendHtmlConstant("<span>"+time.format(d)+"</span>");
				dateBox.setHTML(shb.toSafeHtml());
			}
		};

		updater.scheduleRepeating(1000);

		return dateBox;
	}

	private void setupLists() {
		FlowPanel fp = new FlowPanel();
		fp.setStyleName(s.listWrapper()+" "+GlobalBundle.INSTANCE.globalCss().panel());
		ShowMoreButton sb = new ShowMoreButton(sm, DOCS_PAGE_SIZE);
		sb.setDisplay(invoiceList);
		sb.setStyleName(sm.showmore());
		sb.getButton().addStyleName(GlobalBundle.INSTANCE.globalCss().actionButton());
		fp.add(invoiceList);
		fp.add(sb);
		documentViews.put(0, fp);

		fp = new FlowPanel();
		fp.setStyleName(s.listWrapper()+" "+GlobalBundle.INSTANCE.globalCss().panel());
		sb = new ShowMoreButton(sm, DOCS_PAGE_SIZE);
		sb.setDisplay(estimationList);
		sb.setStyleName(sm.showmore());
		sb.getButton().addStyleName(GlobalBundle.INSTANCE.globalCss().actionButton());
		fp.add(estimationList);
		fp.add(sb);
		documentViews.put(1, fp);

		fp = new FlowPanel();
		fp.setStyleName(s.listWrapper()+" "+GlobalBundle.INSTANCE.globalCss().panel());
		sb = new ShowMoreButton(sm, DOCS_PAGE_SIZE);
		sb.setDisplay(creditNoteList);
		sb.setStyleName(sm.showmore());
		sb.getButton().addStyleName(GlobalBundle.INSTANCE.globalCss().actionButton());
		fp.add(creditNoteList);
		fp.add(sb);
		documentViews.put(2, fp);

		fp = new FlowPanel();
		fp.setStyleName(s.listWrapper()+" "+GlobalBundle.INSTANCE.globalCss().panel());
		sb = new ShowMoreButton(sm, DOCS_PAGE_SIZE);
		sb.setDisplay(transportDocumentList);
		sb.setStyleName(sm.showmore());
		sb.getButton().addStyleName(GlobalBundle.INSTANCE.globalCss().actionButton());
		fp.add(transportDocumentList);
		fp.add(sb);
		documentViews.put(3, fp);
	}
	

	@UiFactory
	I18N getI18N(){
		return I18N.INSTANCE;
	}
	
	@UiFactory
	GlobalCss getGlobalCss() {
		return GlobalBundle.INSTANCE.globalCss();
	}

	@Override
	public void reset(){
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		invoiceList.setPresenter(presenter);
		estimationList.setPresenter(presenter);
		creditNoteList.setPresenter(presenter);
		transportDocumentList.setPresenter(presenter);
	}
	
	@UiHandler("businessLogo")
	void onLogoClicked(ClickEvent e){
		presenter.onLogoClicked();
	}
	

	@UiHandler("tabBar")
	void onTabBarSelected(SelectionEvent<Integer> event) {
		int selectedTab = event.getSelectedItem();
		presenter.onTabBarSelected(selectedTab);
	}

	@UiHandler("newInvoice")
	void onNewInvoiceClicked(ClickEvent e) {
		presenter.onNewInvoiceClicked();
	}


	@UiHandler("newCreditNote")
	void onNewCreditNoteClicked(ClickEvent e) {
		presenter.onNewCreditNoteClicked();
	}


	@UiHandler("newEstimation")
	void onNewEstimationClicked(ClickEvent e) {
		presenter.onNewEstimationClicked();
	}


	@UiHandler("newTransportDocument")
	void onNewTransportDocumentClicked(ClickEvent e) {
		presenter.onNewTransportDocumentClicked();
	}

	@Override
	public InvoiceList getInvoiceList() {
		return invoiceList;
	}
	
	@Override
	public CreditNoteList getCreditNoteList() {
		return creditNoteList;
	}

	@Override
	public EstimationList getEstimationList() {
		return estimationList;
	}

	@Override
	public TransportDocumentList getTransportDocumentList() {
		return transportDocumentList;
	}

	@Override
	public Image getBusinessLogo() {
		return businessLogo;
	}

	@Override
	public HTML getBusinessDetails() {
		return businessDetails;
	}
	
	@Override
	public SimplePanel getTabBody() {
		return tabBody;
	}
	
	@Override
	public Map<Integer, FlowPanel> getDocumentViews() {
		return documentViews;
	}
	
}