package com.novadart.novabill.frontend.client.presenter.center.invoice;

import java.util.List;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.view.center.invoice.InvoiceView;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class ModifyInvoicePresenter extends AbstractInvoicePresenter {

	public ModifyInvoicePresenter(PlaceController placeController, EventBus eventBus, InvoiceView view) {
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

	public void setData(InvoiceDTO invoice) {
		setInvoice(invoice);
		setClient(invoice.getClient());
		getView().getDate().setValue(invoice.getAccountingDocumentDate());
		getView().getInvoiceNumberSuffix().setText(" / "+ getYearFormat().format(invoice.getAccountingDocumentDate()));
		getView().getClientName().setText(invoice.getClient().getName());
		getView().getModifyDocument().setVisible(true);
		getView().getTitleLabel().setText(I18N.INSTANCE.modifyInvoice());

		List<AccountingDocumentItemDTO> items = null;
		items = invoice.getItems();


		getView().getItemInsertionForm().setItems(items);
		if(invoice.getDocumentID() != null){
			getView().getNumber().setText(invoice.getDocumentID().toString());
		} 
		getView().getNote().setText(invoice.getNote());
		getView().getPaymentNote().setText(invoice.getPaymentNote());
		if(invoice.getPaymentType() != null) { //can be null if the invoice is derived from an estimation
			getView().getPayment().setSelectedIndex(invoice.getPaymentType().ordinal()+1);
		}

	}

}
