package com.novadart.novabill.frontend.client.demo.presenter.center.business;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.novadart.novabill.frontend.client.demo.i18n.DemoMessages;
import com.novadart.novabill.frontend.client.presenter.center.business.BusinessPresenter;
import com.novadart.novabill.frontend.client.widget.notification.Notification;

public class DemoBusinessPresenter extends BusinessPresenter {

	@Override
	public void onExportClientDataClicked() {
		Notification.showMessage(DemoMessages.INSTANCE.functionNotAvailable());
	}
	
	@Override
	public void onExportCreditNoteDataClicked() {
		Notification.showMessage(DemoMessages.INSTANCE.functionNotAvailable());
	}
	
	@Override
	public void onExportEstimationDataClicked() {
		Notification.showMessage(DemoMessages.INSTANCE.functionNotAvailable());
	}
	
	@Override
	public void onExportInvoiceDataClicked() {
		Notification.showMessage(DemoMessages.INSTANCE.functionNotAvailable());
	}
	
	@Override
	public void onExportTransportDocumentDataClicked() {
		Notification.showMessage(DemoMessages.INSTANCE.functionNotAvailable());
	}
	
	@Override
	public void onRemoveLogoClicked() {
		Notification.showMessage(DemoMessages.INSTANCE.functionNotAvailable());
	}
	
	@Override
	public void onUpdateLogoClicked() {
		Notification.showMessage(DemoMessages.INSTANCE.functionNotAvailable());
	}
	
	@Override
	public void onLogoSubmit() {
		Notification.showMessage(DemoMessages.INSTANCE.functionNotAvailable());
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		
		getView().getDeleteAccount().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				event.preventDefault();
				Notification.showMessage(DemoMessages.INSTANCE.functionNotAvailable());
			}
		});
	}
}
