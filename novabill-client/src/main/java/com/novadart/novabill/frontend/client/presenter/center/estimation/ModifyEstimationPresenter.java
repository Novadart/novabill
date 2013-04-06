package com.novadart.novabill.frontend.client.presenter.center.estimation;

import java.util.List;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.event.DocumentUpdateEvent;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.place.ClientPlace.DOCUMENTS;
import com.novadart.novabill.frontend.client.view.center.estimation.EstimationView;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.exception.ValidationException;

public class ModifyEstimationPresenter extends AbstractEstimationPresenter {

	public ModifyEstimationPresenter(PlaceController placeController, EventBus eventBus, EstimationView view) {
		super(placeController, eventBus, view);
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

		getView().getConvertToInvoice().setVisible(true);

		List<AccountingDocumentItemDTO> items = null;
		items = estimation.getItems();

		if(estimation.getDocumentID() != null){
			getView().getNumber().setText(estimation.getDocumentID().toString());
		} 

		getView().getItemInsertionForm().setItems(items);
		getView().getNote().setText(estimation.getNote());
		getView().getPaymentNote().setText(estimation.getPaymentNote());
		getView().getLimitations().setText(estimation.getLimitations());
	}


	@Override
	public void onLoad() {
		getView().getTitleLabel().setText(I18N.INSTANCE.modifyEstimation());
	}
	
	@Override
	public void onCreateDocumentClicked() {
		if(!validateEstimation()){
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}

		Notification.showConfirm(I18N.INSTANCE.saveModificationsConfirm(), new NotificationCallback<Boolean>() {

			@Override
			public void onNotificationClosed(Boolean value) {
				if(value){

					getView().getCreateDocument().showLoader(true);
					getView().setLocked(true);
					getView().getConvertToInvoice().getButton().setEnabled(false);


					final EstimationDTO es = createEstimation(getEstimation());

					ServerFacade.estimation.update(es, new ManagedAsyncCallback<Void>() {

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

						@Override
						public void onSuccess(Void result) {
							getView().getCreateDocument().showLoader(false);

							Notification.showMessage(I18N.INSTANCE.estimationUpdateSuccess(), new NotificationCallback<Void>() {

								@Override
								public void onNotificationClosed(Void value) {
									getEventBus().fireEvent(new DocumentUpdateEvent(es));

									ClientPlace cp = new ClientPlace();
									cp.setClientId(es.getClient().getId());
									cp.setDocs(DOCUMENTS.estimations);
									goTo(cp);

									getView().setLocked(false);
									getView().getConvertToInvoice().getButton().setEnabled(true);
								}
							});
						}
					});
				}
			}
		});
	}

}
