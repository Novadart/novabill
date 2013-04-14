package com.novadart.novabill.frontend.client.demo.widget.list.impl;

import com.novadart.novabill.frontend.client.demo.i18n.DemoMessages;
import com.novadart.novabill.frontend.client.widget.list.impl.InvoiceCellImpl;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class DemoInvoiceCellImpl extends InvoiceCellImpl {

	@Override
	public void onPdfClicked(InvoiceDTO invoice) {
		Notification.showMessage(DemoMessages.INSTANCE.functionNotAvailable());
	}
	
}
