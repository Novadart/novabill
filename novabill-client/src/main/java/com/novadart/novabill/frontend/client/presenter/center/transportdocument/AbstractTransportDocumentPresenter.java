package com.novadart.novabill.frontend.client.presenter.center.transportdocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.gwtshared.client.validation.widget.ValidatedWidget;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.event.DocumentAddEvent;
import com.novadart.novabill.frontend.client.event.DocumentUpdateEvent;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
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
import com.novadart.novabill.shared.client.exception.ValidationException;

public abstract class AbstractTransportDocumentPresenter extends DocumentPresenter<TransportDocumentView> implements TransportDocumentView.Presenter {

	private TransportDocumentDTO transportDocument;
	
	
	public AbstractTransportDocumentPresenter(PlaceController placeController, EventBus eventBus, TransportDocumentView view) {
		super(placeController, eventBus, view);
	}

	protected void setTransportDocument(TransportDocumentDTO transportDocument) {
		this.transportDocument = transportDocument;
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
	public void onCreateDocumentClicked() {
		if(!validateTransportDocument()){
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}

		getView().getCreateDocument().showLoader(true);
		getView().setLocked(true);
		
		final TransportDocumentDTO transportDocument = createTransportDocument(null);

		ServerFacade.transportDocument.add(transportDocument, new ManagedAsyncCallback<Long>() {

			@Override
			public void onSuccess(Long result) {
				getView().getCreateDocument().showLoader(false);
				
				getEventBus().fireEvent(new DocumentAddEvent(transportDocument));
				Notification.showMessage(I18N.INSTANCE.transportDocumentCreationSuccess(), new NotificationCallback<Void>() {

					@Override
					public void onNotificationClosed(Void value) {
						ClientPlace cp = new ClientPlace();
						cp.setClientId(getClient().getId());
						cp.setDocs(DOCUMENTS.transportDocuments);
						goTo(cp);
						getView().setLocked(false);
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

	@Override
	public void onModifyDocumentClicked() {
		if(!validateTransportDocument()){
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}

		Notification.showConfirm(I18N.INSTANCE.saveModificationsConfirm(), new NotificationCallback<Boolean>() {

			@Override
			public void onNotificationClosed(Boolean value) {
				if(value){
					getView().getModifyDocument().showLoader(true);
					getView().setLocked(true);
					
					final TransportDocumentDTO td = createTransportDocument(transportDocument);

					ServerFacade.transportDocument.update(td, new ManagedAsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							getView().getModifyDocument().showLoader(false);
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
							getView().getModifyDocument().showLoader(false);
							getEventBus().fireEvent(new DocumentUpdateEvent(transportDocument));
							Notification.showMessage(I18N.INSTANCE.transportDocumentUpdateSuccess(), new NotificationCallback<Void>() {

								@Override
								public void onNotificationClosed(Void value) {
									ClientPlace cp = new ClientPlace();
									cp.setClientId(td.getClient().getId());
									cp.setDocs(DOCUMENTS.transportDocuments);
									goTo(cp);
									getView().setLocked(false);
								}
							});
						}
					});
				}
			}
		});
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
	
	private TransportDocumentDTO createTransportDocument(TransportDocumentDTO transportDocument){
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
	
	private boolean validateTransportDocument(){
		if(getView().getDate().getTextBox().getText().isEmpty() || getView().getDate().getValue() == null 
				|| getView().getTransportStartDate().getTextBox().getText().isEmpty() || getView().getTransportStartDate().getValue() == null){
			return false;
		} else if(getView().getItemInsertionForm().getItems().isEmpty()){
			return false;
		} else {
			boolean validation = true;
			for (ValidatedWidget<?, ?> vw : new ValidatedWidget<?, ?>[]{getView().getNumber(), getView().getFromAddrCity(), getView().getFromAddrCompanyName(), 
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
