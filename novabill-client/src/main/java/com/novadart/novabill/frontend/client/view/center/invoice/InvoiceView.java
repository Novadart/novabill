package com.novadart.novabill.frontend.client.view.center.invoice;

import java.util.Date;

import com.google.gwt.user.client.ui.Label;
import com.novadart.gwtshared.client.LoaderButton;
import com.novadart.gwtshared.client.validation.widget.ValidatedListBox;
import com.novadart.novabill.frontend.client.view.DocumentView;
import com.novadart.novabill.frontend.client.widget.ValidatedTextArea;

public interface InvoiceView extends DocumentView<InvoiceView.Presenter> {
	
	public static interface Presenter extends DocumentView.Presenter {
		
		void onDateChanged(Date date);
		
		void onCreateInvoiceClicked();
		
		void onModifyInvoiceClicked();
		
		void onCancelClicked();
		
	}

	LoaderButton getCreateInvoice();

	LoaderButton getModifyDocument();

	Label getInvoiceNumberSuffix();

	ValidatedTextArea getNote();

	ValidatedTextArea getPaymentNote();

	Label getPaymentNoteLabel();

	Label getInvoiceNumber();

	ValidatedListBox getPayment();

	Label getPaymentLabel();

	Label getTitleLabel();

}
