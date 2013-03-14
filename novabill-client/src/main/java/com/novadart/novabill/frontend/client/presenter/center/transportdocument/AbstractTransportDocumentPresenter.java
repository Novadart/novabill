package com.novadart.novabill.frontend.client.presenter.center.transportdocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.gwtshared.client.validation.widget.ValidatedWidget;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.place.ClientPlace.DOCUMENTS;
import com.novadart.novabill.frontend.client.presenter.center.DocumentPresenter;
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.view.center.transportdocument.TransportDocumentView;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.EndpointDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public abstract class AbstractTransportDocumentPresenter extends DocumentPresenter<TransportDocumentView> implements TransportDocumentView.Presenter {

	private TransportDocumentDTO transportDocument;
	
	
	public AbstractTransportDocumentPresenter(PlaceController placeController, EventBus eventBus, TransportDocumentView view) {
		super(placeController, eventBus, view);
	}

	protected void setTransportDocument(TransportDocumentDTO transportDocument) {
		this.transportDocument = transportDocument;
	}
	
	protected TransportDocumentDTO getTransportDocument() {
		return transportDocument;
	}
	
	@Override
	public void onFromAddressButtonDefaultCLicked() {
		BusinessDTO b = Configuration.getBusiness();
		getView().getFromAddrCity().setText(b.getCity());
		getView().getFromAddrCompanyName().setText(b.getName());
		getView().getFromAddrPostCode().setText(b.getPostcode());
		getView().getFromAddrProvince().setSelectedItem(b.getProvince());
		getView().getFromAddrStreetName().setText(b.getAddress());
		getView().getFromAddrCountry().setSelectedItemByValue(b.getCountry());
	}
	
	@Override
	public void onToAddressButtonDefaultCLicked() {
		getView().getToAddrCity().setText(getClient().getCity());
		getView().getToAddrCompanyName().setText(getClient().getName());
		getView().getToAddrPostCode().setText(getClient().getPostcode());
		if(getClient().getCountry().equalsIgnoreCase("IT")){
			getView().getToAddrProvince().setSelectedItem(getClient().getProvince());
		} else {
			getView().getToAddrProvince().setEnabled(false);
			getView().getToAddrProvince().setSelectedIndex(0);
		}
		getView().getToAddrStreetName().setText(getClient().getAddress());
		getView().getToAddrCountry().setSelectedItemByValue(getClient().getCountry());
	}
	
	@Override
	public void onFromCountryChange() {
		getView().getFromAddrProvince().setEnabled(getView().getFromAddrCountry().getSelectedItemValue().equalsIgnoreCase("IT"));
		getView().getFromAddrProvince().reset();
	}
	
	@Override
	public void onToCountryChange() {
		getView().getToAddrProvince().setEnabled(getView().getToAddrCountry().getSelectedItemValue().equalsIgnoreCase("IT"));
		getView().getToAddrProvince().reset();
	}
	
	@Override
	public void onCancelClicked() {
		Notification.showConfirm(I18N.INSTANCE.cancelModificationsConfirmation(), new NotificationCallback<Boolean>() {

			@Override
			public void onNotificationClosed(Boolean value) {
				if(value){
					ClientPlace cp = new ClientPlace();
					cp.setClientId(getClient().getId());
					cp.setDocs(DOCUMENTS.transportDocuments);
					goTo(cp);
				}
			}
		});
	}
	
	protected TransportDocumentDTO createTransportDocument(TransportDocumentDTO transportDocument){
		TransportDocumentDTO td;

		if(transportDocument != null){
			td = transportDocument;
		} else {
			td = new TransportDocumentDTO();
			td.setBusiness(Configuration.getBusiness());
			td.setClient(getClient());
		}

		td.setDocumentID(Long.parseLong(getView().getNumber().getText()));

		EndpointDTO loc = new EndpointDTO();
		loc.setCompanyName(getView().getFromAddrCompanyName().getText());
		loc.setCity(getView().getFromAddrCity().getText());
		loc.setPostcode(getView().getFromAddrPostCode().getText());
		if(getView().getFromAddrCountry().getSelectedItemValue().equalsIgnoreCase("IT")){
			loc.setProvince(getView().getFromAddrProvince().getSelectedItemText());
		} else {
			loc.setProvince("");
		}
		loc.setStreet(getView().getFromAddrStreetName().getText());
		loc.setCountry(getView().getFromAddrCountry().getSelectedItemValue());
		td.setFromEndpoint(loc);

		loc = new EndpointDTO();
		loc.setCompanyName(getView().getToAddrCompanyName().getText());
		loc.setCity(getView().getToAddrCity().getText());
		loc.setPostcode(getView().getToAddrPostCode().getText());
		if(getView().getToAddrCountry().getSelectedItemValue().equalsIgnoreCase("IT")){
			loc.setProvince(getView().getToAddrProvince().getSelectedItemText());
		} else {
			loc.setProvince("");
		}
		loc.setStreet(getView().getToAddrStreetName().getText());
		loc.setCountry(getView().getToAddrCountry().getSelectedItemValue());
		td.setToEndpoint(loc);

		Date tsd = getView().getTransportStartDate().getValue();
		String dateTime = DateTimeFormat.getFormat("dd MMMM yyyy").format(tsd);
		dateTime += " "+getView().getHour().getSelectedItemText()+":"+getView().getMinute().getSelectedItemText();
		tsd = DateTimeFormat.getFormat("dd MMMM yyyy HH:mm").parse(dateTime);		
		td.setTransportStartDate(tsd);

		td.setNumberOfPackages(Integer.valueOf(getView().getNumberOfPackages().getText()));
		td.setTradeZone(getView().getTradeZone().getText());
		td.setTransportationResponsibility(getView().getTransportationResponsibility().getText());
		td.setTransporter(getView().getTransporter().getText());

		td.setAccountingDocumentDate(getView().getDate().getValue());
		List<AccountingDocumentItemDTO> invItems = new ArrayList<AccountingDocumentItemDTO>();
		for (AccountingDocumentItemDTO itemDTO : getView().getItemInsertionForm().getItems()) {
			invItems.add(itemDTO);
		}
		td.setItems(invItems);
		td.setNote(getView().getNote().getText());
		DocumentUtils.calculateTotals(invItems, td);
		return td;
	}
	
	protected boolean validateTransportDocument(){
		getView().getDate().validate();
		getView().getTransportStartDate().validate();
		
		if(getView().getItemInsertionForm().getItems().isEmpty()){
			return false;
		} else {
			boolean validation = getView().getDate().isValid() && getView().getTransportStartDate().isValid();
			for (ValidatedWidget<?> vw : new ValidatedWidget<?>[]{getView().getNumber(), getView().getFromAddrCity(), getView().getFromAddrCompanyName(), 
					getView().getFromAddrPostCode(), getView().getFromAddrStreetName(), getView().getFromAddrCountry(), getView().getToAddrCountry(), 
					getView().getToAddrCity(), getView().getToAddrCompanyName(), getView().getToAddrPostCode(),	getView().getToAddrStreetName(), 
					getView().getNumberOfPackages(), getView().getHour(), getView().getMinute()}) {
				vw.validate();
				validation = validation && vw.isValid();
			}

			if(getView().getFromAddrCountry().getSelectedItemValue().equalsIgnoreCase("IT")){
				getView().getFromAddrProvince().validate();
				validation = validation && getView().getFromAddrProvince().isValid();
			}

			if(getView().getToAddrCountry().getSelectedItemValue().equalsIgnoreCase("IT")){
				getView().getToAddrProvince().validate();
				validation = validation && getView().getToAddrProvince().isValid();
			}


			if(!validation){
				return false;
			}
		}
		return true;
	}

}