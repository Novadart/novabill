package com.novadart.novabill.frontend.client.view;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.novadart.gwtshared.client.LoaderButton;
import com.novadart.gwtshared.client.textbox.RichTextBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedDateBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedListBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.view.center.ItemInsertionForm;
import com.novadart.novabill.frontend.client.widget.ValidatedTextArea;

public interface DocumentView<P extends DocumentView.Presenter> extends View<P>, HasUILocking {
	
	public static interface Presenter extends com.novadart.novabill.frontend.client.presenter.Presenter {
	
		void onLoad();
		
		void onCreateDocumentClicked();
		
		void onCancelClicked();
		
	    void onToCountryChange();

	    void onToAddressButtonDefaultChange();

	}
	
    HorizontalPanel getToAddressContainer();

    CheckBox getSetToAddress();

	RichTextBox getToAddrCompanyName();

	RichTextBox getToAddrStreetName();

	RichTextBox getToAddrPostCode();

	RichTextBox getToAddrCity();

	ValidatedListBox getToAddrProvince();

	ValidatedListBox getToAddrCountry();

	ListBox getToAddrButtonDefault();

	LoaderButton getCreateDocument();

	Button getAbort();
	
	ValidatedTextBox getNumber();
	
	ValidatedDateBox getDate();

	Label getClientName();
	
	Label getTotalAfterTaxes();

	Label getTotalTax();

	Label getTotalBeforeTaxes();
	
	ItemInsertionForm getItemInsertionForm();
	
	ValidatedTextArea getNote();
	
}
