package com.novadart.novabill.frontend.client.presenter.center.transportdocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.view.center.transportdocument.TransportDocumentView;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EndpointDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public class NewTransportDocumentPresenter extends AbstractTransportDocumentPresenter {


	public NewTransportDocumentPresenter(PlaceController placeController, EventBus eventBus, TransportDocumentView view) {
		super(placeController, eventBus, view);
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

		getView().getNumberOfPackages().setText(String.valueOf(document.getNumberOfPackages()));

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
		getView().getTitleLabel().setText(I18N.INSTANCE.newTransportDocumentCreation());
	}

	@Override
	protected void setPresenterinView(TransportDocumentView view) {
		view.setPresenter(this);
	}
}
