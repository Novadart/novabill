package com.novadart.novabill.frontend.client.presenter.center.estimation;

import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.bridge.BridgeUtils;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.view.center.estimation.EstimationView;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.EndpointDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.exception.ValidationException;

public class ModifyEstimationPresenter extends AbstractEstimationPresenter {

	public ModifyEstimationPresenter(PlaceController placeController, EventBus eventBus, EstimationView view, JavaScriptObject callback) {
		super(placeController, eventBus, view, callback);
	}
	
	@Override
	protected void setPresenterInView(EstimationView view) {
		view.setPresenter(this);
	}

	public void setData(EstimationDTO estimation) {
		setEstimation(estimation);
		setClient(estimation.getClient());
		getView().getDate().setValue(estimation.getAccountingDocumentDate());
		getView().getValidTill().setValue(estimation.getValidTill());
		getView().getClientName().setText(estimation.getClient().getName());

		List<AccountingDocumentItemDTO> items = null;
		items = estimation.getItems();

		if(estimation.getDocumentID() != null){
			getView().getNumber().setText(estimation.getDocumentID().toString());
		} 
		
		EndpointDTO loc = estimation.getToEndpoint();
		// if loading a light client most likely the destination address was not set and we should avoid loading it
		if(loc.getCountry()!=null){
			getView().getToAddrCity().setText(loc.getCity());
			getView().getToAddrCompanyName().setText(loc.getCompanyName());
			getView().getToAddrPostCode().setText(loc.getPostcode());
			getView().getToAddrProvince().setText(loc.getProvince());
			getView().getToAddrStreetName().setText(loc.getStreet());
			getView().getToAddrCountry().setSelectedItemByValue(loc.getCountry());
			getView().getSetToAddress().setValue(true);
			getView().getToAddressContainer().setVisible(true);
		}

		getView().getItemInsertionForm().setItems(items);
		getView().getNote().setText(estimation.getNote());
		getView().getPaymentNote().setText(estimation.getPaymentNote());
		getView().getLimitations().setText(estimation.getLimitations());
		getView().getTermsAndConditions().setText(estimation.getTermsAndConditions());
		getView().getOverrideIncognitoModeCheckbox().setValue(!estimation.isIncognito());
	}


	@Override
	public void onCreateDocumentClicked() {
		if(!validateEstimation()){
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}

		Notification.showConfirm(I18N.INSTANCE.saveModificationsConfirm(), new NotificationCallback() {

			@Override
			public void onNotificationClosed(boolean value) {
				if(value){

					getView().getCreateDocument().showLoader(true);
					getView().setLocked(true);

					final EstimationDTO es = createEstimation(getEstimation());

					ServerFacade.INSTANCE.getEstimationService().update(es, new ManagedAsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							getView().getCreateDocument().showLoader(false);
							if(caught instanceof ValidationException){
								handleServerValidationException((ValidationException) caught);
							} else {
								super.onFailure(caught);
							}

							getView().setLocked(false);
						}

						@Override
						public void onSuccess(Void result) {
							getView().getCreateDocument().showLoader(false);

							Notification.showMessage(I18N.INSTANCE.estimationUpdateSuccess(), new NotificationCallback() {

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

}
