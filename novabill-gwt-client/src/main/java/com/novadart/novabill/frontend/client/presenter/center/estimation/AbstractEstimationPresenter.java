package com.novadart.novabill.frontend.client.presenter.center.estimation;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.gwtshared.client.validation.widget.ValidatedWidget;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.bridge.BridgeUtils;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.presenter.center.DocumentPresenter;
import com.novadart.novabill.frontend.client.util.CalcUtils;
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.view.center.estimation.EstimationView;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.EndpointDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;

public abstract class AbstractEstimationPresenter extends DocumentPresenter<EstimationView> implements EstimationView.Presenter {

	private EstimationDTO estimation;

	public AbstractEstimationPresenter(PlaceController placeController,	EventBus eventBus, EstimationView view, JavaScriptObject callback) {
		super(placeController, eventBus, view, callback);
	}

	protected void setEstimation(EstimationDTO estimation) {
		this.estimation = estimation;
	}

	protected EstimationDTO getEstimation() {
		return estimation;
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


	protected boolean validateEstimation(){
		getView().getNumber().validate();
		getView().getDate().validate();
		getView().getValidTill().validate();

		boolean validation = true;
		
		if(getView().getSetToAddress().getValue()){
			for (ValidatedWidget<?> vw : new ValidatedWidget<?>[]{getView().getToAddrCountry(), getView().getToAddrCity(), 
					getView().getToAddrCompanyName(), getView().getToAddrPostCode(),	getView().getToAddrStreetName()}) {
				vw.validate();
				validation = validation && vw.isValid();
			}
		}

		if(getView().getSetToAddress().getValue() && getView().getToAddrCountry().getSelectedItemValue().equalsIgnoreCase("IT")){
			getView().getToAddrProvince().validate();
			validation = validation && getView().getToAddrProvince().isValid();
		}

		if(!getView().getItemInsertionForm().isValid()){
			return false;
		}

		return validation && getView().getNumber().isValid() && getView().getDate().isValid() && getView().getValidTill().isValid();
	}


	protected EstimationDTO createEstimation(EstimationDTO estimation){
		EstimationDTO es;

		if(estimation != null){
			es = estimation;
		} else {
			es = new EstimationDTO();
			es.setBusiness(Configuration.getBusiness());
			es.setClient(getClient());
		}

		if(!getView().getSetToAddress().getValue()){
			getView().getToAddrCompanyName().setText(getClient().getName());
			if(getClient().getVatID()!=null || getClient().getSsn()!=null){
				getView().getToAddrCity().setText(getClient().getCity());
				getView().getToAddrPostCode().setText(getClient().getPostcode());
				if(getClient().getCountry().equalsIgnoreCase("IT")){
					getView().getToAddrProvince().setSelectedItem(getClient().getProvince());
				}
				getView().getToAddrStreetName().setText(getClient().getAddress());
				getView().getToAddrCountry().setSelectedItemByValue(getClient().getCountry());
			}
		}

		EndpointDTO loc = new EndpointDTO();
		loc.setCompanyName(getView().getToAddrCompanyName().getText());
		loc.setCity(getView().getToAddrCity().getText());
		loc.setPostcode(getView().getToAddrPostCode().getText());
		if("IT".equalsIgnoreCase(getView().getToAddrCountry().getSelectedItemValue())){
			loc.setProvince(getView().getToAddrProvince().getSelectedItemText());
		} else {
			loc.setProvince("");
		}
		loc.setStreet(getView().getToAddrStreetName().getText());
		
		if(getView().getToAddrCountry().getSelectedIndex() > 0) {
			loc.setCountry(getView().getToAddrCountry().getSelectedItemValue());
		}
		es.setToEndpoint(loc);

		es.setLayoutType(Configuration.getBusiness().getSettings().getDefaultLayoutType());

		es.setDocumentID(Long.parseLong(getView().getNumber().getText()));

		es.setAccountingDocumentDate(DocumentUtils.createNormalizedDate(getView().getDate().getValue()));
		es.setValidTill(DocumentUtils.createNormalizedDate(getView().getValidTill().getValue()));
		List<AccountingDocumentItemDTO> invItems = new ArrayList<AccountingDocumentItemDTO>();
		for (AccountingDocumentItemDTO itemDTO : getView().getItemInsertionForm().getItems()) {
			invItems.add(itemDTO);
		}
		es.setItems(invItems);
		es.setNote(getView().getNote().getText());
		es.setPaymentNote(getView().getPaymentNote().getText());
		es.setLimitations(getView().getLimitations().getText());
		es.setIncognito(Configuration.getBusiness().getSettings().isIncognitoEnabled() && !getView().getOverrideIncognitoModeCheckbox().getValue());
		CalcUtils.calculateTotals(invItems, es);
		return es;
	}
}
