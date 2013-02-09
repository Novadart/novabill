package com.novadart.novabill.frontend.client.presenter.center.estimation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.event.DocumentAddEvent;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.place.ClientPlace.DOCUMENTS;
import com.novadart.novabill.frontend.client.view.center.estimation.EstimationView;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.exception.ValidationException;

public class NewEstimationPresenter extends AbstractEstimationPresenter {

	public NewEstimationPresenter(PlaceController placeController, EventBus eventBus, EstimationView view) {
		super(placeController, eventBus, view);
	}
	
	@Override
	protected void setPresenterinView(EstimationView view) {
		view.setPresenter(this);
	}

	public void setDataForNewEstimation(ClientDTO client, Long progressiveId) {
		setClient(client);

		getView().getClientName().setText(client.getName());
		getView().getNumber().setText(progressiveId.toString());
		Date now = new Date();
		getView().getDate().setValue(now);
		getView().getValidTill().setValue(new Date(now.getTime() + 2592000000L));

		getView().getCreateDocument().setVisible(true);
		getView().getConvertToInvoice().setVisible(true);
	}

	public void setDataForNewEstimation(ClientDTO client, Long progressiveId, EstimationDTO estimation) {
		setDataForNewEstimation(client, progressiveId);

		List<AccountingDocumentItemDTO> items = null;
		items = new ArrayList<AccountingDocumentItemDTO>(estimation.getItems().size());
		for (AccountingDocumentItemDTO i : estimation.getItems()) {
			items.add(i.clone());
		}

		getView().getItemInsertionForm().setItems(items);
		getView().getNote().setText(estimation.getNote());
		getView().getPaymentNote().setText(estimation.getPaymentNote());
		getView().getLimitations().setText(estimation.getLimitations());
	}

	@Override
	public void onLoad() {
		getView().getCreateDocument().setText(I18N.INSTANCE.createEstimate());
		getView().getTitleLabel().setText(I18N.INSTANCE.newEstimationCreation());
	}
	
	@Override
	public void onCreateDocumentClicked() {
		if(!validateEstimation()){
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}
		
		getView().getCreateDocument().showLoader(true);
		getView().setLocked(true);
		getView().getConvertToInvoice().getButton().setEnabled(false);

		final EstimationDTO estimation = createEstimation(null);
		ServerFacade.estimation.add(estimation, new ManagedAsyncCallback<Long>() {

			@Override
			public void onSuccess(Long result) {
				getView().getCreateDocument().showLoader(false);
				Notification.showMessage(I18N.INSTANCE.estimationCreationSuccess());

				getEventBus().fireEvent(new DocumentAddEvent(estimation));

				ClientPlace cp = new ClientPlace();
				cp.setClientId(getClient().getId());
				cp.setDocs(DOCUMENTS.estimations);
				goTo(cp);
				
				getView().setLocked(false);
				getView().getConvertToInvoice().getButton().setEnabled(true);
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
				getView().getConvertToInvoice().getButton().setEnabled(true);
			}
		});
	}

}
