package com.novadart.novabill.frontend.client.presenter.center.estimation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.view.center.estimation.EstimationView;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;

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
		getView().getTitleLabel().setText(I18N.INSTANCE.newEstimationCreation());
	}

}
