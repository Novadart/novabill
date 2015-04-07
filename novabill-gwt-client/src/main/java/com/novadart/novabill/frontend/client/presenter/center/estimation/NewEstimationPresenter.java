package com.novadart.novabill.frontend.client.presenter.center.estimation;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.bridge.BridgeUtils;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.view.center.estimation.EstimationView;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.exception.ValidationException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewEstimationPresenter extends AbstractEstimationPresenter {

	public NewEstimationPresenter(PlaceController placeController, EventBus eventBus, EstimationView view, JavaScriptObject callback) {
		super(placeController, eventBus, view, callback);
	}

	@Override
	protected void setPresenterInView(EstimationView view) {
		view.setPresenter(this);
	}

	public void setDataForNewEstimation(ClientDTO client, Long progressiveId) {
		setClient(client);

		getView().getClientName().setText(client.getName());
		getView().getNumber().setText(progressiveId.toString());
		Date now = DocumentUtils.createNormalizedDate(new Date());
		getView().getDate().setValue(now);
		getView().getValidTill().setValue(new Date(now.getTime() + 2592000000L));
		getView().getTermsAndConditions().setText(Configuration.getBusiness().getSettings().getDefaultTermsAndConditionsForEstimation());;

		getView().getCreateDocument().setVisible(true);
	}

	public void setDataForNewEstimation(ClientDTO client, Long progressiveId, EstimationDTO estimation) {
		setDataForNewEstimation(client, progressiveId);

		List<AccountingDocumentItemDTO> items = null;
		items = new ArrayList<AccountingDocumentItemDTO>(estimation.getItems().size());
		for (AccountingDocumentItemDTO i : estimation.getItems()) {
			items.add(i.clone());
		}

		//NOTE obviously in this case we don't load the endpoint as it is specific to the client

		getView().getItemInsertionForm().setItems(items);
		getView().getNote().setText(estimation.getNote());
		getView().getPaymentNote().setText(estimation.getPaymentNote());
		getView().getLimitations().setText(estimation.getLimitations());
		getView().getTermsAndConditions().setText(estimation.getTermsAndConditions());
	}

	@Override
	public void onCreateDocumentClicked() {
		if(!validateEstimation()){
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}

		getView().getCreateDocument().showLoader(true);
		getView().setLocked(true);

		final EstimationDTO estimation = createEstimation(null);
		ServerFacade.INSTANCE.getEstimationService().add(estimation, new ManagedAsyncCallback<Long>() {

			@Override
			public void onSuccess(Long result) {
				getView().getCreateDocument().showLoader(false);
				Notification.showMessage(I18N.INSTANCE.estimationCreationSuccess());
				getView().setLocked(false);
				BridgeUtils.invokeJSCallback(Boolean.TRUE, getCallback());
			}

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
		});
	}

}
