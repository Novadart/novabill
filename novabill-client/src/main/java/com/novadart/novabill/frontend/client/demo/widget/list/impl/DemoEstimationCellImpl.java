package com.novadart.novabill.frontend.client.demo.widget.list.impl;

import com.novadart.novabill.frontend.client.demo.i18n.DemoMessages;
import com.novadart.novabill.frontend.client.widget.list.impl.EstimationCellImpl;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.shared.client.dto.EstimationDTO;

public class DemoEstimationCellImpl extends EstimationCellImpl {

	@Override
	public void onPdfClicked(EstimationDTO estimation) {
		Notification.showMessage(DemoMessages.INSTANCE.functionNotAvailable());
	}
}
