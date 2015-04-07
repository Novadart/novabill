package com.novadart.novabill.frontend.client.presenter.center.transportdocument;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.bridge.BridgeUtils;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.view.center.transportdocument.TransportDocumentView;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.EndpointDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.ValidationException;

public class ModifyTransportDocumentPresenter extends AbstractTransportDocumentPresenter {

	public ModifyTransportDocumentPresenter(PlaceController placeController, EventBus eventBus, TransportDocumentView view, JavaScriptObject callback) {
		super(placeController, eventBus, view, callback);
	}

	public void setData(TransportDocumentDTO document) {
		setTransportDocument(document);
		setClient(document.getClient());
		getView().getDate().setValue(document.getAccountingDocumentDate());
		getView().getClientName().setText(document.getClient().getName());

		Date d = document.getTransportStartDate();
		getView().getTransportStartDate().setValue(d);
		String hourStr = DateTimeFormat.getFormat("HH").format(d);
		String minuteStr = DateTimeFormat.getFormat("mm").format(d);
		getView().getHour().setSelectedItem(hourStr);
		getView().getMinute().setSelectedItem(minuteStr);

		List<AccountingDocumentItemDTO> items = null;
		items = document.getItems();

		getView().getItemInsertionForm().setItems(items);
		getView().getNote().setText(document.getNote());

		if(document.getDocumentID() != null){
			getView().getNumber().setText(document.getDocumentID().toString());
		} 

		getView().getAppearanceOfTheGoods().setText(document.getAppearanceOfTheGoods());
		getView().getNumberOfPackages().setText(String.valueOf(document.getNumberOfPackages() != null ? document.getNumberOfPackages() : ""));
		getView().getTotalWeight().setText(document.getTotalWeight());
		getView().getCause().setText(document.getCause());
		getView().getTransporter().setText(document.getTransporter());
		getView().getTradeZone().setText(document.getTradeZone());
		getView().getTransportationResponsibility().setText(document.getTransportationResponsibility());

		EndpointDTO loc = document.getFromEndpoint();
		getView().getFromAddrCity().setText(loc.getCity());
		getView().getFromAddrCompanyName().setText(loc.getCompanyName());
		getView().getFromAddrPostCode().setText(loc.getPostcode());
		getView().getFromAddrProvince().setText(loc.getProvince());
		getView().getFromAddrStreetName().setText(loc.getStreet());
		getView().getFromAddrCountry().setSelectedItemByValue(loc.getCountry());
		getView().getSetFromAddress().setValue(true);
		getView().getFromAddressContainer().setVisible(true);
		
		loc = document.getToEndpoint();
		getView().getToAddrCity().setText(loc.getCity());
		getView().getToAddrCompanyName().setText(loc.getCompanyName());
		getView().getToAddrPostCode().setText(loc.getPostcode());
		getView().getToAddrProvince().setText(loc.getProvince());
		getView().getToAddrStreetName().setText(loc.getStreet());
		getView().getToAddrCountry().setSelectedItemByValue(loc.getCountry());
		getView().getSetToAddress().setValue(true);
		getView().getToAddressContainer().setVisible(true);
		
		// lock the UI ig the transport document was included in an invoice already
		if(getTransportDocument().getInvoice() != null){
			getView().setLocked(true);
			getView().getReadonlyWarning().setVisible(true);
			getView().getAbort().setEnabled(true);
		}
	}
	
	@Override
	public void onLoad() {
		// load the button only if readonly
		if(getTransportDocument().getInvoice() == null){
			super.onLoad();
			loadTransporters();
		}
	}

	@Override
	public void onCreateDocumentClicked() {
		if(!validateTransportDocument()){
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}

		Notification.showConfirm(I18N.INSTANCE.saveModificationsConfirm(), new NotificationCallback() {

			@Override
			public void onNotificationClosed(boolean value) {
				if(value){
					getView().getCreateDocument().showLoader(true);
					getView().setLocked(true);

					final TransportDocumentDTO td = createTransportDocument(getTransportDocument());

					ServerFacade.INSTANCE.getTransportdocumentService().update(td, new ManagedAsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							getView().getCreateDocument().showLoader(false);
							if(caught instanceof ValidationException){
								handleServerValidationException((ValidationException) caught);
							} else {
								Notification.showMessage(I18N.INSTANCE.transportDocumentUpdateFailure());
								super.onFailure(caught);
							}
							getView().setLocked(false);
						}

						@Override
						public void onSuccess(Void result) {
							getView().getCreateDocument().showLoader(false);
							Notification.showMessage(I18N.INSTANCE.transportDocumentUpdateSuccess(), new NotificationCallback() {

								@Override
								public void onNotificationClosed(boolean value) {
									getView().setLocked(false);
									BridgeUtils.invokeJSCallback(Boolean.TRUE, getCallback());
								}
							});
						}
					});
				}
			}
		});
	}

	@Override
	protected void setPresenterInView(TransportDocumentView view) {
		view.setPresenter(this);
	}

}
