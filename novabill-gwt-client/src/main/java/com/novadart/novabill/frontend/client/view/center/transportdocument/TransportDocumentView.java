package com.novadart.novabill.frontend.client.view.center.transportdocument;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.novadart.gwtshared.client.textbox.RichTextBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedDateBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedListBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextArea;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.view.DocumentView;

public interface TransportDocumentView extends DocumentView<TransportDocumentView.Presenter> {
	
	public static interface Presenter extends DocumentView.Presenter {
		
		void onFromCountryChange();
		
		void onToCountryChange();
		
		void onFromAddressButtonDefaultCLicked();
		
		void onToAddressButtonDefaultCLicked();
		
		void onCountItemsCLicked();

		void onTotalWeightCalcClicked();
	}
	
	
	RichTextBox getFromAddrCompanyName();

	RichTextBox getFromAddrStreetName();

	RichTextBox getFromAddrPostCode();

	RichTextBox getFromAddrCity();

	ValidatedListBox getFromAddrProvince();

	ValidatedListBox getFromAddrCountry();

	Button getFromAddrButtonDefault();

	RichTextBox getToAddrCompanyName();

	RichTextBox getToAddrStreetName();

	RichTextBox getToAddrPostCode();

	RichTextBox getToAddrCity();

	ValidatedListBox getToAddrProvince();

	ValidatedListBox getToAddrCountry();

	Button getToAddrButtonDefault();

	ValidatedTextBox getNumberOfPackages();
	
	ValidatedTextBox getTotalWeight();

	ValidatedTextArea getTransporter();

	ValidatedDateBox getTransportStartDate();

	ValidatedListBox getHour();

	ValidatedListBox getMinute();

	ValidatedTextBox getTransportationResponsibility();

	ValidatedTextBox getTradeZone();
	
	ValidatedTextBox getCause();
	
	ValidatedTextArea getAppearanceOfTheGoods();

	CheckBox getSetFromAddress();

	CheckBox getSetToAddress();

	Button getCountItems();

	HorizontalPanel getFromAddressContainer();

	HorizontalPanel getToAddressContainer();
	
}
