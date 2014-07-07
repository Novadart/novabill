package com.novadart.novabill.frontend.client.presenter.center.transportdocument;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.gwtshared.client.validation.widget.ValidatedWidget;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.bridge.BridgeUtils;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.presenter.center.DocumentPresenter;
import com.novadart.novabill.frontend.client.util.CalcUtils;
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.view.center.transportdocument.TransportDocumentView;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.EndpointDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.dto.TransporterDTO;

public abstract class AbstractTransportDocumentPresenter extends DocumentPresenter<TransportDocumentView> implements TransportDocumentView.Presenter {

	private TransportDocumentDTO transportDocument;
	private List<TransporterDTO> transporters;

	public AbstractTransportDocumentPresenter(PlaceController placeController, EventBus eventBus, TransportDocumentView view, JavaScriptObject callback) {
		super(placeController, eventBus, view, callback);
	}

	protected void setTransportDocument(TransportDocumentDTO transportDocument) {
		this.transportDocument = transportDocument;
	}

	protected TransportDocumentDTO getTransportDocument() {
		return transportDocument;
	}

	protected void loadTransporters(){
		getView().getLoadTransporterAddress().setEnabled(false);
		getView().getLoadTransporterAddress().clear();
		ServerFacade.INSTANCE.getBusinessService().getTransporters(Configuration.getBusinessId(), new ManagedAsyncCallback<List<TransporterDTO>>() {

			@Override
			public void onSuccess(List<TransporterDTO> result) {
				transporters = result;

				getView().getLoadTransporterAddress().addItem(I18N.INSTANCE.selectTransporter(), "");
				for (TransporterDTO c : result) {
					getView().getLoadTransporterAddress().addItem(c.getName(), c.getId().toString());
				}
				getView().getLoadTransporterAddress().setEnabled(true);

			}
		});
	}

	@Override
	public void onLoadTransporterAddressChange() {
		int selectedIndex = getView().getLoadTransporterAddress().getSelectedIndex();

		switch (selectedIndex) {
		case 0:
			break;

		default:
			Long selId = Long.parseLong(getView().getLoadTransporterAddress().getValue(selectedIndex));
			for (TransporterDTO c : transporters) {
				if(c.getId().equals(selId)){
					getView().getTransporter().setText(c.getDescription());
					break;
				}
			}
			break;
		}
	}

	@Override
	public void onFromAddressButtonDefaultCLicked() {
		BusinessDTO b = Configuration.getBusiness();
		getView().getFromAddrCity().setText(b.getCity());
		getView().getFromAddrCompanyName().setText(b.getName());
		getView().getFromAddrPostCode().setText(b.getPostcode());
		getView().getFromAddrProvince().setText(b.getProvince());
		getView().getFromAddrStreetName().setText(b.getAddress());
		getView().getFromAddrCountry().setSelectedItemByValue(b.getCountry());
	}


	@Override
	public void onCountItemsCLicked() {
		List<AccountingDocumentItemDTO> items = getView().getItemInsertionForm().getItems();

		BigDecimal total = BigDecimal.ZERO;
		for (AccountingDocumentItemDTO i : items) {
			if(i.getQuantity() != null){
				total = total.add(i.getQuantity());
			}
		}

		BigDecimal roundedTotal = total.setScale(0, RoundingMode.CEILING);
		getView().getNumberOfPackages().setText(String.valueOf(roundedTotal.intValue()));
	}

	@Override
	public void onTotalWeightCalcClicked() {
		List<AccountingDocumentItemDTO> items = getView().getItemInsertionForm().getItems();

		BigDecimal total = CalcUtils.calculateTotalWeight(items);
		BigDecimal roundedTotal = total.setScale(3, RoundingMode.HALF_UP);
		getView().getTotalWeight().setText(NumberFormat.getDecimalFormat().format(roundedTotal));
	}

	@Override
	public void onCancelClicked() {
		Notification.showConfirm(I18N.INSTANCE.cancelModificationsConfirmation(), new NotificationCallback() {

			@Override
			public void onNotificationClosed(boolean value) {
				if(value){
					BridgeUtils.invokeJSCallback(Boolean.FALSE, getCallback());
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

		// this to auto populate the fields
		if(!getView().getSetFromAddress().getValue()){
			onFromAddressButtonDefaultCLicked();
		}

		if(!getView().getSetToAddress().getValue()){
			getView().getToAddrCity().setText(getClient().getCity());
			getView().getToAddrCompanyName().setText(getClient().getName());
			getView().getToAddrPostCode().setText(getClient().getPostcode());
			getView().getToAddrProvince().setText(getClient().getProvince());
			getView().getToAddrStreetName().setText(getClient().getAddress());
			getView().getToAddrCountry().setSelectedItemByValue(getClient().getCountry());
		}

		td.setLayoutType(Configuration.getBusiness().getSettings().getDefaultLayoutType());

		td.setDocumentID(Long.parseLong(getView().getNumber().getText()));

		EndpointDTO loc = new EndpointDTO();
		loc.setCompanyName(getView().getFromAddrCompanyName().getText());
		loc.setCity(getView().getFromAddrCity().getText());
		loc.setPostcode(getView().getFromAddrPostCode().getText());
		loc.setProvince(getView().getFromAddrProvince().getText());
		loc.setStreet(getView().getFromAddrStreetName().getText());
		loc.setCountry(getView().getFromAddrCountry().getSelectedItemValue());
		td.setFromEndpoint(loc);

		loc = new EndpointDTO();
		loc.setCompanyName(getView().getToAddrCompanyName().getText());
		loc.setCity(getView().getToAddrCity().getText());
		loc.setPostcode(getView().getToAddrPostCode().getText());
		loc.setProvince(getView().getToAddrProvince().getText());
		loc.setStreet(getView().getToAddrStreetName().getText());
		loc.setCountry(getView().getToAddrCountry().getSelectedItemValue());
		td.setToEndpoint(loc);

		Date tsd = getView().getTransportStartDate().getValue();
		String dateTime = DateTimeFormat.getFormat("dd MMMM yyyy").format(tsd);
		dateTime += " "+getView().getHour().getSelectedItemText()+":"+getView().getMinute().getSelectedItemText();
		tsd = DateTimeFormat.getFormat("dd MMMM yyyy HH:mm").parse(dateTime);		
		td.setTransportStartDate(tsd);

		td.setAppearanceOfTheGoods(getView().getAppearanceOfTheGoods().getText());
		td.setCause(getView().getCause().getText());
		td.setNumberOfPackages(getView().getNumberOfPackages().getText());
		td.setTotalWeight(getView().getTotalWeight().getText());
		td.setTradeZone(getView().getTradeZone().getText());
		td.setTransportationResponsibility(getView().getTransportationResponsibility().getText());
		td.setTransporter(getView().getTransporter().getText());

		td.setAccountingDocumentDate(DocumentUtils.createNormalizedDate(getView().getDate().getValue()));
		List<AccountingDocumentItemDTO> invItems = new ArrayList<AccountingDocumentItemDTO>();
		for (AccountingDocumentItemDTO itemDTO : getView().getItemInsertionForm().getItems()) {
			invItems.add(itemDTO);
		}
		td.setItems(invItems);
		td.setNote(getView().getNote().getText());
		CalcUtils.calculateTotals(invItems, td);
		return td;
	}

	protected boolean validateTransportDocument(){
		getView().getDate().validate();
		getView().getTransportStartDate().validate();

		if(!getView().getItemInsertionForm().isValid()){
			return false;
		} else {
			boolean validation = getView().getDate().isValid() && getView().getTransportStartDate().isValid();
			for (ValidatedWidget<?> vw : new ValidatedWidget<?>[]{getView().getNumber(), getView().getCause(), getView().getTradeZone(),
					getView().getTransportationResponsibility(), getView().getAppearanceOfTheGoods(),
					getView().getNumberOfPackages(), getView().getTotalWeight(), getView().getHour(), getView().getMinute()}) {
				vw.validate();
				validation = validation && vw.isValid();
			}

			if(getView().getSetFromAddress().getValue()){
				for (ValidatedWidget<?> vw : new ValidatedWidget<?>[]{getView().getFromAddrCity(), getView().getFromAddrCompanyName(), 
						getView().getFromAddrPostCode(), getView().getFromAddrStreetName(), getView().getFromAddrCountry()}) {
					vw.validate();
					validation = validation && vw.isValid();
				}

			}

			if(getView().getSetToAddress().getValue()){
				for (ValidatedWidget<?> vw : new ValidatedWidget<?>[]{getView().getToAddrCountry(), getView().getToAddrCity(), 
						getView().getToAddrCompanyName(), getView().getToAddrPostCode(),	getView().getToAddrStreetName()}) {
					vw.validate();
					validation = validation && vw.isValid();
				}

			}

			if(getView().getSetFromAddress().getValue() && getView().getFromAddrCountry().getSelectedItemValue().equalsIgnoreCase("IT")){
				getView().getFromAddrProvince().validate();
				validation = validation && getView().getFromAddrProvince().isValid();
			}

			if(getView().getSetToAddress().getValue() && getView().getToAddrCountry().getSelectedItemValue().equalsIgnoreCase("IT")){
				getView().getToAddrProvince().validate();
				validation = validation && getView().getToAddrProvince().isValid();
			}

			return validation;
		}
	}

}
