package com.novadart.novabill.frontend.client.presenter.center.transportdocument;

import java.util.ArrayList;
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
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EndpointDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.ValidationException;

public class NewTransportDocumentPresenter extends AbstractTransportDocumentPresenter {


	public NewTransportDocumentPresenter(PlaceController placeController, EventBus eventBus, TransportDocumentView view, JavaScriptObject callback) {
		super(placeController, eventBus, view, callback);
	}

	public void setDataForNewTransportDocument(ClientDTO client, Long transportDocumentProgressiveId, TransportDocumentDTO document) {
		setDataForNewTransportDocument(client,transportDocumentProgressiveId);

		List<AccountingDocumentItemDTO> items = null;
		items = new ArrayList<AccountingDocumentItemDTO>(document.getItems().size());
		for (AccountingDocumentItemDTO i : document.getItems()) {
			items.add(i.clone());
		}

		getView().getItemInsertionForm().setItems(items);
		getView().getNote().setText(document.getNote());
		
		getView().getCause().setText(document.getCause());
		getView().getNumberOfPackages().setText(String.valueOf(document.getNumberOfPackages()));
		getView().getTotalWeight().setText(document.getTotalWeight());

		EndpointDTO loc = document.getFromEndpoint();
		getView().getFromAddrCity().setText(loc.getCity());
		getView().getFromAddrCompanyName().setText(loc.getCompanyName());
		getView().getFromAddrPostCode().setText(loc.getPostcode());
		getView().getFromAddrProvince().setSelectedItem(loc.getProvince());
		getView().getFromAddrStreetName().setText(loc.getStreet());
		getView().getFromAddrCountry().setSelectedItemByValue(loc.getCountry());

		loc = document.getToEndpoint();
		getView().getToAddrCity().setText(loc.getCity());
		getView().getToAddrCompanyName().setText(loc.getCompanyName());
		getView().getToAddrPostCode().setText(loc.getPostcode());
		getView().getToAddrProvince().setSelectedItem(loc.getProvince());
		getView().getToAddrStreetName().setText(loc.getStreet());
		getView().getToAddrCountry().setSelectedItemByValue(loc.getCountry());

	}

	@Override
	public void onCreateDocumentClicked() {
		if(!validateTransportDocument()){
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}

		getView().getCreateDocument().showLoader(true);
		getView().setLocked(true);

		final TransportDocumentDTO transportDocument = createTransportDocument(null);

		ServerFacade.INSTANCE.getTransportdocumentService().add(transportDocument, new ManagedAsyncCallback<Long>() {

			@Override
			public void onSuccess(Long result) {
				getView().getCreateDocument().showLoader(false);

				Notification.showMessage(I18N.INSTANCE.transportDocumentCreationSuccess(), new NotificationCallback<Void>() {

					@Override
					public void onNotificationClosed(Void value) {
						getView().setLocked(false);
						BridgeUtils.invokeJSCallback(Boolean.TRUE, getCallback());
					}
				});

			}

			@Override
			public void onFailure(Throwable caught) {
				getView().getCreateDocument().showLoader(false);

				if(caught instanceof ValidationException){
					handleServerValidationException((ValidationException) caught);
				} else {
					Notification.showMessage(I18N.INSTANCE.transportDocumentCreationFailure());
					super.onFailure(caught);
				}

				getView().setLocked(false);
			}
		});
	}

	public void setDataForNewTransportDocument(ClientDTO client, Long transportDocumentProgressiveId) {
		setClient(client);

		getView().getNumber().setText(transportDocumentProgressiveId.toString());
		getView().getClientName().setText(client.getName());
		Date d = new Date();
		String hourStr = DateTimeFormat.getFormat("HH").format(d);
		String minuteStr = DateTimeFormat.getFormat("mm").format(d);
		getView().getDate().setValue(d);
		getView().getTransportStartDate().setValue(d);
		getView().getHour().setSelectedItem(hourStr);
		getView().getMinute().setSelectedItem(minuteStr);

		getView().getCreateDocument().setVisible(true);
	}

	@Override
	public void onLoad() {
	}

	@Override
	protected void setPresenterInView(TransportDocumentView view) {
		view.setPresenter(this);
	}
}
