package com.novadart.novabill.frontend.client.view.center.invoice;

import java.util.Date;

import com.google.gwt.user.client.ui.Label;
import com.novadart.novabill.frontend.client.view.DocumentView;
import com.novadart.novabill.frontend.client.widget.ValidatedTextArea;
import com.novadart.novabill.frontend.client.widget.payment.SelectPayment;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;

public interface InvoiceView extends DocumentView<InvoiceView.Presenter> {
	
	public static interface Presenter extends DocumentView.Presenter {
		
		void onDateChanged(Date date);

		void onPaymentSelected(PaymentTypeDTO payment);
	}

	Label getInvoiceNumberSuffix();

	ValidatedTextArea getPaymentNote();

	Label getPaymentNoteLabel();

	Label getInvoiceNumber();

	SelectPayment getPayment();

	Label getPaymentLabel();

	Label getTitleLabel();

}