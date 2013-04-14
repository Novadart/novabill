package com.novadart.novabill.frontend.client.demo.widget.list.impl;

import com.novadart.novabill.frontend.client.demo.i18n.DemoMessages;
import com.novadart.novabill.frontend.client.widget.list.impl.CreditNoteCellImpl;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;

public class DemoCreditNoteCellImpl extends CreditNoteCellImpl {

	@Override
	public void onPdfClicked(CreditNoteDTO creditNote) {
		Notification.showMessage(DemoMessages.INSTANCE.functionNotAvailable());
	}
}
