package com.novadart.novabill.frontend.client.presenter.center.invoice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.i18n.I18NM;
import com.novadart.novabill.frontend.client.view.center.invoice.InvoiceView;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public class NewInvoicePresenter extends AbstractInvoicePresenter {

	public NewInvoicePresenter(PlaceController placeController,	EventBus eventBus, InvoiceView view) {
		super(placeController, eventBus, view);
	}

	@Override
	protected void setPresenterinView(InvoiceView view) {
		view.setPresenter(this);
	}
	
	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		
	}
	
	public void setDataForNewInvoice(ClientDTO client, Long progressiveId) {
		setClient(client);

		getView().getClientName().setText(client.getName());
		Date d = new Date();
		getView().getDate().setValue(d);
		getView().getInvoiceNumberSuffix().setText(" / "+ getYearFormat().format(d));
		getView().getNumber().setText(progressiveId.toString());

		getView().getCreateDocument().setVisible(true);
	}

	
	public void setDataForNewInvoice(ClientDTO client, Long progressiveId, InvoiceDTO invoice) {
		setDataForNewInvoice(client, progressiveId);

		List<AccountingDocumentItemDTO> items = null;
		items = new ArrayList<AccountingDocumentItemDTO>(invoice.getItems().size());
		for (AccountingDocumentItemDTO i : invoice.getItems()) {
			items.add(i.clone());
		}
		getView().getItemInsertionForm().setItems(items);
		getView().getNote().setText(invoice.getNote());
		getView().getPaymentNote().setText(invoice.getPaymentNote());
		if(invoice.getPaymentType() != null) { //can be null if the invoice is derived from an estimation
			getView().getPayment().setSelectedIndex(invoice.getPaymentType().ordinal()+1);
		}
	}

	
	public void setDataForNewInvoice(Long progressiveId, EstimationDTO estimation) {
		setDataForNewInvoice(estimation.getClient(), progressiveId);

		List<AccountingDocumentItemDTO> items = new ArrayList<AccountingDocumentItemDTO>(estimation.getItems().size());
		for (AccountingDocumentItemDTO i : estimation.getItems()) {
			items.add(i.clone());
		}

		getView().getItemInsertionForm().setItems(items);
		getView().getNote().setText(I18NM.get.generatedFromEstimation(estimation.getDocumentID(), 
				DateTimeFormat.getFormat("dd MMMM yyyy").format(estimation.getAccountingDocumentDate())));
	}

	
	public void setDataForNewInvoice(Long progressiveId, TransportDocumentDTO transportDocument) {
		setDataForNewInvoice(transportDocument.getClient(), progressiveId);

		List<AccountingDocumentItemDTO> items = new ArrayList<AccountingDocumentItemDTO>(transportDocument.getItems().size());
		for (AccountingDocumentItemDTO i : transportDocument.getItems()) {
			items.add(i.clone());
		}

		getView().getItemInsertionForm().setItems(items);
		getView().getNote().setText(transportDocument.getNote());
	}

}
