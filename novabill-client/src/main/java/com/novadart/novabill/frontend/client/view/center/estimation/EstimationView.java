package com.novadart.novabill.frontend.client.view.center.estimation;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.datepicker.client.DateBox;
import com.novadart.gwtshared.client.LoaderButton;
import com.novadart.novabill.frontend.client.view.DocumentView;
import com.novadart.novabill.frontend.client.widget.ValidatedTextArea;

public interface EstimationView extends DocumentView<EstimationView.Presenter> {
	
	public static interface Presenter extends DocumentView.Presenter {
		
		void onConvertToInvoiceClicked();
		
	}
	
	public Label getTitleLabel();

	public DateBox getValidTill();

	public ValidatedTextArea getPaymentNote();

	public ValidatedTextArea getLimitations();

	public LoaderButton getConvertToInvoice();
	
}
