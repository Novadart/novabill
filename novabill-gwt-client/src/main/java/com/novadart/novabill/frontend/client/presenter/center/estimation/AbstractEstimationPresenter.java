package com.novadart.novabill.frontend.client.presenter.center.estimation;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.bridge.BridgeUtils;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.presenter.center.DocumentPresenter;
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.view.center.estimation.EstimationView;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
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
		Notification.showConfirm(I18N.INSTANCE.cancelModificationsConfirmation(), new NotificationCallback<Boolean>() {

			@Override
			public void onNotificationClosed(Boolean value) {
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
		
		if(!getView().getItemInsertionForm().isValid()){
			return false;
		}
		
		return getView().getNumber().isValid() && getView().getDate().isValid() && getView().getValidTill().isValid();
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

		es.setLayoutType(Configuration.getBusiness().getDefaultLayoutType());
		
		es.setDocumentID(Long.parseLong(getView().getNumber().getText()));
		
		es.setAccountingDocumentDate(getView().getDate().getValue());
		es.setValidTill(getView().getValidTill().getValue());
		List<AccountingDocumentItemDTO> invItems = new ArrayList<AccountingDocumentItemDTO>();
		for (AccountingDocumentItemDTO itemDTO : getView().getItemInsertionForm().getItems()) {
			invItems.add(itemDTO);
		}
		es.setItems(invItems);
		es.setNote(getView().getNote().getText());
		es.setPaymentNote(getView().getPaymentNote().getText());
		es.setLimitations(getView().getLimitations().getText());
		DocumentUtils.calculateTotals(invItems, es);
		return es;
	}
}
