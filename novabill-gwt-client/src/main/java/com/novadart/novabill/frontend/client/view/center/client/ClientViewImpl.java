package com.novadart.novabill.frontend.client.view.center.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.gwtshared.client.LoaderButton;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.resources.GlobalCss;
import com.novadart.novabill.frontend.client.resources.ImageResources;
import com.novadart.novabill.frontend.client.util.WidgetUtils;
import com.novadart.novabill.frontend.client.widget.list.impl.CreditNoteList;
import com.novadart.novabill.frontend.client.widget.list.impl.EstimationList;
import com.novadart.novabill.frontend.client.widget.list.impl.InvoiceList;
import com.novadart.novabill.frontend.client.widget.list.impl.TransportDocumentList;
import com.novadart.novabill.frontend.client.widget.tip.TipFactory;
import com.novadart.novabill.frontend.client.widget.tip.Tips;

public class ClientViewImpl extends Composite implements ClientView {
	
	private static ClientViewImplUiBinder uiBinder = GWT
			.create(ClientViewImplUiBinder.class);

	interface ClientViewImplUiBinder extends UiBinder<Widget, ClientViewImpl> {
	}
	
	public interface Style extends CssResource {
		@ClassName("gwt-TabLayoutPanelTab")
		String gwtTabLayoutPanelTab();

		@ClassName("contact-down")
		String contactDown();

		@ClassName("contact-disabled")
		String contactDisabled();

		String contact();

		@ClassName("gwt-TabLayoutPanelTab-selected")
		String gwtTabLayoutPanelTabSelected();

		String newCreditNote();

		String cancelClient();

		String newTransportDocument();

		String modifyClient();

		String clientName();

		String listWrapper();

		String newEstimation();

		String clientMainBody();

		@ClassName("contact-up-hovering")
		String contactUpHovering();

		String actionWrapper();

		String clientOptions();

		String clientDetails();

		@ClassName("contact-down-hovering")
		String contactDownHovering();

		String newInvoice();
	}

	private Presenter presenter;
	
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

	@UiField ToggleButton contact;
	
	@UiField(provided=true) LoaderButton cancelClient;
	@UiField Button modifyClient;
	
	@UiField Button newInvoice;
	@UiField Button newEstimation;
	@UiField Button newTransportDocument;
	@UiField Button newCreditNote;
	
	@UiField SimplePanel tip;
	
	@UiField Style s;


	public ClientViewImpl() {
		cancelClient = new LoaderButton(ImageResources.INSTANCE.loader(), GlobalBundle.INSTANCE.loaderButton());
		initWidget(uiBinder.createAndBindUi(this));
		setStyleName("ClientView");
		cancelClient.getButton().setStyleName(s.cancelClient()+" "+GlobalBundle.INSTANCE.globalCss().button());
		
		TipFactory.show(Tips.client_view, tip);
	}

	@Override
	protected void onUnload() {
		super.onUnload();
		presenter.onUnload();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		presenter.onLoad();
		
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

			@Override
			public void execute() {
				WidgetUtils.setElementHeightToFillSpace(clientMainBody.getElement(), getElement(), 
						getClientName().getElement(), getClientDetails().getElement(), clientOptions.getElement());

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
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		invoiceList.setPresenter(presenter);
		estimationList.setPresenter(presenter);
		creditNoteList.setPresenter(presenter);
		transportDocumentList.setPresenter(presenter);
	}

	@Override
	public void reset() {
		clientName.setText("");
		clientDetails.setHTML("");
		contactPopup.reset();
		contact.setDown(false);
		contact.setEnabled(false);
		contact.setStyleName(s.contact());
		
		setLocked(false);
		cancelClient.reset();
	}

	@UiHandler("contact")
	void onContactMouseClick(ClickEvent event) {
		presenter.onContactMouseClick();
	}

	@UiHandler("newInvoice")
	void onNewInvoiceClicked(ClickEvent e){
		presenter.onNewInvoiceClicked();
	}

	@UiHandler("newEstimation")
	void onNewEstimationClicked(ClickEvent e){
		presenter.onNewEstimationClicked();
	}

	@UiHandler("newTransportDocument")
	void onNewTransportDocumentClicked(ClickEvent e){
		presenter.onNewTransportDocumentClicked();
	}

	@UiHandler("newCreditNote")
	void onNewCreditNoteClicked(ClickEvent e){
		presenter.onNewCreditNoteClicked();
	}


	@UiHandler("modifyClient")
	void onModifyClientClicked(ClickEvent e){
		presenter.onModifyClientClicked();
	}


	@UiHandler("cancelClient")
	void onCancelClientClicked(ClickEvent e){
		presenter.onCancelClientClicked();
	}

	@Override
	public void setLocked(boolean value) {
		newInvoice.setEnabled(!value);
		newEstimation.setEnabled(!value);
		newTransportDocument.setEnabled(!value);
		newCreditNote.setEnabled(!value);
		
		modifyClient.setEnabled(!value);
	}
	
	@UiFactory
	GlobalCss getgGlobalCss(){
		return GlobalBundle.INSTANCE.globalCss();
	}

	@Override
	public ContactPopup getContactPopup() {
		return contactPopup;
	}

	@Override
	public InvoiceList getInvoiceList() {
		return invoiceList;
	}

	@Override
	public EstimationList getEstimationList() {
		return estimationList;
	}

	@Override
	public CreditNoteList getCreditNoteList() {
		return creditNoteList;
	}

	@Override
	public TransportDocumentList getTransportDocumentList() {
		return transportDocumentList;
	}

	@Override
	public Label getClientName() {
		return clientName;
	}

	@Override
	public HTML getClientDetails() {
		return clientDetails;
	}

	@Override
	public TabLayoutPanel getTabPanel() {
		return tabPanel;
	}

	@Override
	public FlowPanel getListWrapperInvoice() {
		return listWrapperInvoice;
	}

	@Override
	public SimplePanel getActionWrapperInvoice() {
		return actionWrapperInvoice;
	}

	@Override
	public ScrollPanel getScrollInvoice() {
		return scrollInvoice;
	}

	@Override
	public FlowPanel getListWrapperEstimation() {
		return listWrapperEstimation;
	}

	@Override
	public SimplePanel getActionWrapperEstimation() {
		return actionWrapperEstimation;
	}

	@Override
	public ScrollPanel getScrollEstimation() {
		return scrollEstimation;
	}

	@Override
	public FlowPanel getListWrapperCredit() {
		return listWrapperCredit;
	}

	@Override
	public SimplePanel getActionWrapperCredit() {
		return actionWrapperCredit;
	}

	@Override
	public ScrollPanel getScrollCredit() {
		return scrollCredit;
	}

	@Override
	public FlowPanel getListWrapperTransport() {
		return listWrapperTransport;
	}

	@Override
	public SimplePanel getActionWrapperTransport() {
		return actionWrapperTransport;
	}

	@Override
	public ScrollPanel getScrollTransport() {
		return scrollTransport;
	}

	@Override
	public HorizontalPanel getClientOptions() {
		return clientOptions;
	}

	@Override
	public SimpleLayoutPanel getClientMainBody() {
		return clientMainBody;
	}

	@Override
	public ToggleButton getContact() {
		return contact;
	}

	@Override
	public LoaderButton getCancelClient() {
		return cancelClient;
	}

	@Override
	public Button getModifyClient() {
		return modifyClient;
	}

	@Override
	public Button getNewInvoice() {
		return newInvoice;
	}

	@Override
	public Button getNewEstimation() {
		return newEstimation;
	}

	@Override
	public Button getNewTransportDocument() {
		return newTransportDocument;
	}

	@Override
	public Button getNewCreditNote() {
		return newCreditNote;
	}

	
	@Override
	public SimplePanel getTip() {
		return tip;
	}
	
}
