package com.novadart.novabill.frontend.client.view;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.novadart.gwtshared.client.LoaderButton;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.view.center.ItemInsertionForm;
import com.novadart.novabill.frontend.client.widget.ValidatedTextArea;

public interface DocumentView<P extends DocumentView.Presenter> extends View<P>, HasUILocking {
	
	public static interface Presenter extends com.novadart.novabill.frontend.client.presenter.Presenter {
	
		void onLoad();
		
		void onCreateDocumentClicked();
		
		void onCancelClicked();

	}

	LoaderButton getCreateDocument();

	Button getAbort();
	
	ValidatedTextBox getNumber();
	
	DateBox getDate();

	Label getClientName();
	
	Label getTotalAfterTaxes();

	Label getTotalTax();

	Label getTotalBeforeTaxes();
	
	ItemInsertionForm getItemInsertionForm();
	
	ScrollPanel getDocScroll();
	
	ValidatedTextArea getNote();
	
}
