package com.novadart.novabill.frontend.client.view.center.home;

import com.google.gwt.resources.client.CssResource;

public interface ShowMoreButton extends CssResource {

	String businessName();

	String homeBody();

	String businessDetails();

	String invoicesTitle();

	String date();

	String businessPanel();

	String newCreditNote();

	String newTransportDocument();

	@ClassName("gwt-TabBarItem")
	String gwtTabBarItem();

	String currentDate();

	String scrollHome();

	String listWrapper();

	String newEstimation();

	String tabPanel();

	String businessLogo();

	String yourDocsLabel();

	String actions();

	@ClassName("gwt-TabBarItem-selected")
	String gwtTabBarItemSelected();

}
