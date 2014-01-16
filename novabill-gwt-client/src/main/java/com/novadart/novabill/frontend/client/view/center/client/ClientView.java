package com.novadart.novabill.frontend.client.view.center.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.novadart.gwtshared.client.LoaderButton;
import com.novadart.novabill.frontend.client.place.ClientPlace.DOCUMENTS;
import com.novadart.novabill.frontend.client.view.HasUILocking;
import com.novadart.novabill.frontend.client.view.View;
import com.novadart.novabill.frontend.client.widget.list.impl.CreditNoteList;
import com.novadart.novabill.frontend.client.widget.list.impl.EstimationList;
import com.novadart.novabill.frontend.client.widget.list.impl.InvoiceList;
import com.novadart.novabill.frontend.client.widget.list.impl.TransportDocumentList;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public interface ClientView extends View<ClientView.Presenter>, HasUILocking {
	
	public static interface Presenter extends com.novadart.novabill.frontend.client.presenter.Presenter {
		void setClient(ClientDTO client);
		
		void setDocumentsListing(DOCUMENTS docs);
		
		void onCancelClientClicked();
		
		void onContactMouseClick();
		
		void onLoad();
		
		void onUnload();
		
		void onNewCreditNoteClicked();
		
		void onNewEstimationClicked();
		
		void onNewTransportDocumentClicked();
		
		void onModifyClientClicked();
		
		void onNewInvoiceClicked();
		
	}

	public SimplePanel getTip();

	public Button getNewCreditNote();

	public Button getNewTransportDocument();

	public Button getNewEstimation();

	public Button getNewInvoice();

	public Button getModifyClient();

	public LoaderButton getCancelClient();

	public ToggleButton getContact();

	public SimpleLayoutPanel getClientMainBody();

	public HorizontalPanel getClientOptions();

	public ScrollPanel getScrollTransport();

	public SimplePanel getActionWrapperTransport();

	public FlowPanel getListWrapperTransport();

	public ScrollPanel getScrollCredit();

	public SimplePanel getActionWrapperCredit();

	public FlowPanel getListWrapperCredit();

	public ScrollPanel getScrollEstimation();

	public SimplePanel getActionWrapperEstimation();

	public FlowPanel getListWrapperEstimation();

	public ScrollPanel getScrollInvoice();

	public SimplePanel getActionWrapperInvoice();

	public FlowPanel getListWrapperInvoice();

	public TabLayoutPanel getTabPanel();

	public HTML getClientDetails();

	public Label getClientName();

	public TransportDocumentList getTransportDocumentList();

	public CreditNoteList getCreditNoteList();

	public EstimationList getEstimationList();

	public InvoiceList getInvoiceList();

	public ContactPopup getContactPopup(); 
	
}
