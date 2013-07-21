package com.novadart.novabill.frontend.client.demo.widget.list.impl;

import com.novadart.novabill.frontend.client.demo.i18n.DemoMessages;
import com.novadart.novabill.frontend.client.widget.list.impl.TransportDocumentCellImpl;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public class DemoTransportDocumentCellImpl extends TransportDocumentCellImpl {

	@Override
	public void onPdfClicked(TransportDocumentDTO transportDocument) {
		Notification.showMessage(DemoMessages.INSTANCE.functionNotAvailable());
	}
	
}
