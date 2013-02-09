package com.novadart.novabill.frontend.client.presenter.center.invoice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.place.ClientPlace.DOCUMENTS;
import com.novadart.novabill.frontend.client.presenter.center.DocumentPresenter;
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.view.center.invoice.InvoiceView;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PaymentType;

public abstract class AbstractInvoicePresenter extends DocumentPresenter<InvoiceView> implements InvoiceView.Presenter {

	private InvoiceDTO invoice;
	
	public AbstractInvoicePresenter(PlaceController placeController, EventBus eventBus,	InvoiceView view) {
		super(placeController, eventBus, view);
	}

	@Override
	public void onDateChanged(Date date){
		getView().getInvoiceNumberSuffix().setText(" / "+ getYearFormat().format(date));
	}


	@Override
	public void onCancelClicked() {
		Notification.showConfirm(I18N.INSTANCE.cancelModificationsConfirmation(), new NotificationCallback<Boolean>() {

			@Override
			public void onNotificationClosed(Boolean value) {
				if(value){
					ClientPlace cp = new ClientPlace();
					cp.setClientId(getClient().getId());
					cp.setDocs(DOCUMENTS.invoices);
					goTo(cp);
				}
			}
		});
	}
	
	
	protected void setInvoice(InvoiceDTO invoice) {
		this.invoice = invoice;
	}
	
	protected InvoiceDTO getInvoice() {
		return invoice;
	}
	
	protected boolean validateInvoice(){
		if(getView().getDate().getTextBox().getText().isEmpty() || getView().getDate().getValue() == null){
			return false;
		} 
		
		if(getView().getItemInsertionForm().getItems().isEmpty()){
			return false;
		}
		
		getView().getNumber().validate();
		getView().getPayment().validate();
		if(!getView().getNumber().isValid() || !getView().getPayment().isValid()){
			return false;
		}

		return true;
	}

	protected InvoiceDTO createInvoice(InvoiceDTO invoice){
		InvoiceDTO inv;

		if(invoice != null){
			inv = invoice;
		} else {
			inv = new InvoiceDTO();
			inv.setBusiness(Configuration.getBusiness());
			inv.setClient(getClient());
		}


		inv.setDocumentID(Long.parseLong(getView().getNumber().getText()));
		inv.setAccountingDocumentDate(getView().getDate().getValue());
		List<AccountingDocumentItemDTO> invItems = new ArrayList<AccountingDocumentItemDTO>();
		for (AccountingDocumentItemDTO itemDTO : getView().getItemInsertionForm().getItems()) {
			invItems.add(itemDTO);
		}
		inv.setItems(invItems);
		inv.setNote(getView().getNote().getText());
		inv.setPaymentType(PaymentType.values()[getView().getPayment().getSelectedIndex()-1]);
		if(getView().getPayment().getSelectedIndex() > 0){
			inv.setPaymentDueDate(DocumentUtils.calculatePaymentDueDate(inv.getAccountingDocumentDate(), inv.getPaymentType()));  
		} else {
			inv.setPaymentDueDate(null);
		}

		inv.setPaymentNote(getView().getPaymentNote().getText());
		DocumentUtils.calculateTotals(invItems, inv);
		return inv;
	}

}
