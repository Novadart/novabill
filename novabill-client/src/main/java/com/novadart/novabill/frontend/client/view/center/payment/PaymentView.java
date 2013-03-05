package com.novadart.novabill.frontend.client.view.center.payment;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.HTML;
import com.novadart.novabill.frontend.client.view.HasUILocking;
import com.novadart.novabill.frontend.client.view.View;
import com.novadart.novabill.frontend.client.view.center.payment.PaymentViewImpl.Style;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;

public interface PaymentView extends View<PaymentView.Presenter>, HasUILocking{

	public static interface Presenter extends com.novadart.novabill.frontend.client.presenter.Presenter {
		void onLoad();
		void onPaymentDelete(PaymentTypeDTO payment);
		void onAddPaymentClicked();
	}
	
	Style getStyle();

	CellTable<PaymentTypeDTO> getPayments();

	HTML getDescription();
}
