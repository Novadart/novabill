package com.novadart.novabill.frontend.client.view.center.transportdocument;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;
import com.novadart.gwtshared.client.textbox.RichTextBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedListBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.view.DocumentView;

public interface TransportDocumentView extends DocumentView<TransportDocumentView.Presenter> {
	
	public static interface Presenter extends DocumentView.Presenter {
		
		void onFromCountryChange();
		
		void onToCountryChange();
		
		void onFromAddressButtonDefaultCLicked();
		
		void onToAddressButtonDefaultCLicked();
	}
	
	
	Label getTitleLabel();

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

	ValidatedTextBox getTransporter();

	DateBox getTransportStartDate();

	ValidatedListBox getHour();

	ValidatedListBox getMinute();

	TextBox getTransportationResponsibility();

	TextBox getTradeZone();
	
}
