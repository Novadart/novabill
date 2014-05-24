package com.novadart.novabill.frontend.client.view.center.transportdocument;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.novadart.gwtshared.client.textbox.RichTextArea;
import com.novadart.gwtshared.client.textbox.RichTextBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedDateBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedListBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextArea;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.view.DocumentView;

public interface TransportDocumentView extends DocumentView<TransportDocumentView.Presenter> {
	
	public static interface Presenter extends DocumentView.Presenter {
		
		void onFromCountryChange();
		
		void onFromAddressButtonDefaultCLicked();
		
		void onCountItemsCLicked();

		void onTotalWeightCalcClicked();

		void onLoadTransporterAddressChange();
	}
	
	
	RichTextArea getFromAddrCompanyName();

	RichTextBox getFromAddrStreetName();

	RichTextBox getFromAddrPostCode();

	RichTextBox getFromAddrCity();

	ValidatedListBox getFromAddrProvince();

	ValidatedListBox getFromAddrCountry();

	Button getFromAddrButtonDefault();

	ValidatedTextBox getNumberOfPackages();
	
	ValidatedTextBox getTotalWeight();

	ValidatedTextArea getTransporter();
	
	ListBox getLoadTransporterAddress();

	ValidatedDateBox getTransportStartDate();

	ValidatedListBox getHour();

	ValidatedListBox getMinute();

	ValidatedTextBox getTransportationResponsibility();

	ValidatedTextBox getTradeZone();
	
	ValidatedTextBox getCause();
	
	ValidatedTextArea getAppearanceOfTheGoods();

	CheckBox getSetFromAddress();

	Button getCountItems();

	HorizontalPanel getFromAddressContainer();

	Label getReadonlyWarning();
	
}
