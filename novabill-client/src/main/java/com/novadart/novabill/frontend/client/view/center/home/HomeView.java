package com.novadart.novabill.frontend.client.view.center.home;

import java.util.Map;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.novadart.novabill.frontend.client.view.View;
import com.novadart.novabill.frontend.client.widget.list.impl.CreditNoteList;
import com.novadart.novabill.frontend.client.widget.list.impl.EstimationList;
import com.novadart.novabill.frontend.client.widget.list.impl.InvoiceList;
import com.novadart.novabill.frontend.client.widget.list.impl.TransportDocumentList;

public interface HomeView extends View<HomeView.Presenter> {
	
	public static interface Presenter extends com.novadart.novabill.frontend.client.presenter.Presenter {
		
		void onNewTransportDocumentClicked();
		
		void onNewEstimationClicked();
		
		void onNewCreditNoteClicked();
		
		void onNewInvoiceClicked();
		
		void onLogoClicked();
		
		void onLoad();

		void onTabBarSelected(int selectedTab);
		
	}

	InvoiceList getInvoiceList();

	EstimationList getEstimationList();

	TransportDocumentList getTransportDocumentList();

	Image getBusinessLogo();

	HTML getBusinessDetails();

	CreditNoteList getCreditNoteList();

	SimplePanel getTabBody();

	Map<Integer, FlowPanel> getDocumentViews();

}
