package com.novadart.novabill.frontend.client.view.center.invoice;

import java.util.Date;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.novadart.novabill.frontend.client.view.DocumentView;
import com.novadart.novabill.frontend.client.widget.ValidatedTextArea;
import com.novadart.novabill.frontend.client.widget.payment.SelectPayment;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;

public interface InvoiceView extends DocumentView<InvoiceView.Presenter> {

	interface Presenter extends DocumentView.Presenter {

		void onDateChanged(Date date);

		void onPaymentSelected(PaymentTypeDTO payment);

		void onPaymentClear();

		void onDocumentIdClassChange();

	}

	Label getInvoiceNumberSuffix();

	ListBox getDocumentIDClassListBox();

	ValidatedTextArea getPaymentNote();

	ListBox getSelectSplitPayment();

	Label getInvoiceNumber();

	SelectPayment getPayment();

	CheckBox getMakePaymentAsDefault();

}
