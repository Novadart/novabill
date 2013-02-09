package com.novadart.novabill.frontend.client.presenter.center.estimation;

import java.util.List;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.view.center.estimation.EstimationView;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;

public class ModifyEstimationPresenter extends AbstractEstimationPresenter {

	public ModifyEstimationPresenter(PlaceController placeController, EventBus eventBus, EstimationView view) {
		super(placeController, eventBus, view);
	}
	
	@Override
	protected void setPresenterinView(EstimationView view) {
		view.setPresenter(this);
	}

	public void setData(EstimationDTO estimation) {
		setEstimation(estimation);
		setClient(estimation.getClient());
		getView().getDate().setValue(estimation.getAccountingDocumentDate());
		getView().getValidTill().setValue(estimation.getValidTill());
		getView().getClientName().setText(estimation.getClient().getName());
		getView().getModifyDocument().setVisible(true);

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

}
