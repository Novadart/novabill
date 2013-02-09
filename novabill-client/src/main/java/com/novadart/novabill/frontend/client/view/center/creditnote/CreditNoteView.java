package com.novadart.novabill.frontend.client.view.center.creditnote;

import com.google.gwt.user.client.ui.Label;
import com.novadart.gwtshared.client.validation.widget.ValidatedListBox;
import com.novadart.novabill.frontend.client.view.DocumentView;
import com.novadart.novabill.frontend.client.widget.ValidatedTextArea;

public interface CreditNoteView extends DocumentView<CreditNoteView.Presenter> {
	
	public static interface Presenter extends DocumentView.Presenter{
		
	}

	ValidatedListBox getPayment();

	ValidatedTextArea getPaymentNote();

	Label getTitleLabel();

}
