package com.novadart.novabill.frontend.client.view.center.invoice;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.novadart.novabill.frontend.client.view.DocumentView;
import com.novadart.novabill.frontend.client.widget.ValidatedTextArea;
import com.novadart.novabill.frontend.client.widget.payment.SelectPayment;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;

import java.util.Date;

public interface InvoiceView extends DocumentView<InvoiceView.Presenter> {
	
	public static interface Presenter extends DocumentView.Presenter {
		
		void onDateChanged(Date date);

		void onPaymentSelected(PaymentTypeDTO payment);

		void onPaymentClear();

	}

	Label getInvoiceNumberSuffix();

	ValidatedTextArea getPaymentNote();

	Label getInvoiceNumber();

	SelectPayment getPayment();

	CheckBox getMakePaymentAsDefault();

}
