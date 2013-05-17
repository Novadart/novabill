package com.novadart.novabill.frontend.client.presenter.center.invoice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
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
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;

public abstract class AbstractInvoicePresenter extends DocumentPresenter<InvoiceView> implements InvoiceView.Presenter {

	private InvoiceDTO invoice;
	
	private PaymentTypeDTO cachedDefaultPaymentTypeDTO = null;

	public AbstractInvoicePresenter(PlaceController placeController, EventBus eventBus,	InvoiceView view) {
		super(placeController, eventBus, view);
	}

	@Override
	public void onDateChanged(Date date){
		getView().getInvoiceNumberSuffix().setText(" / "+ getYearFormat().format(date));
		getView().getPayment().setDocumentCreationDate(date);
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
		getView().getDate().validate();
		getView().getNumber().validate();
		getView().getPayment();
		
		if(getView().getItemInsertionForm().getItems().isEmpty()){
			return false;
		}

		if(!getView().getDate().isValid() || !getView().getNumber().isValid() || !getView().getPayment().isValid()){
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
		inv.setPaymentTypeName(getView().getPayment().getSelectedPayment().getName());
		inv.setPaymentDateDelta(getView().getPayment().getSelectedPayment().getPaymentDateDelta());
		inv.setPaymentDateGenerator(getView().getPayment().getSelectedPayment().getPaymentDateGenerator());
		inv.setPaymentDueDate(getView().getPayment().getPaymentDueDate());
		inv.setPaymentNote(getView().getPaymentNote().getText());
		DocumentUtils.calculateTotals(invItems, inv);
		return inv;
	}
	
	@Override
	public void onPaymentSelected(final PaymentTypeDTO payment) {
		/*
		 * when the payment is selected, we will show the checkbox to make the payment as default, if necessary
		 */
		ManagedAsyncCallback<PaymentTypeDTO> cb = new ManagedAsyncCallback<PaymentTypeDTO>() {

			@Override
			public void onSuccess(PaymentTypeDTO result) {
				cachedDefaultPaymentTypeDTO = result;
				if(cachedDefaultPaymentTypeDTO == null || !cachedDefaultPaymentTypeDTO.getId().equals(payment.getId())) {
					getView().getMakePaymentAsDefault().setVisible(true);
				}
			}
		};
		
		//if this client's default payment type was never fetched, we fetch it
		if(getClient().getDefaultPaymentTypeID() != null && this.cachedDefaultPaymentTypeDTO == null){
			ServerFacade.INSTANCE.getPaymentService().get(getClient().getDefaultPaymentTypeID(), cb);
		} else {
			//otherwise we use the cached one
			cb.onSuccess(cachedDefaultPaymentTypeDTO);
		}
		
		/*
		 * update the payment note if necessary		
		 */
		if(payment.getDefaultPaymentNote().trim().isEmpty()){
			return;
		}
		
		if(getView().getPaymentNote().isEmpty()){
			getView().getPaymentNote().setText(payment.getDefaultPaymentNote());
		} else {
			
			if(getView().getPaymentNote().getText().equals(payment.getDefaultPaymentNote())){
				return;
			}
			
			SafeHtmlBuilder shb = new SafeHtmlBuilder();
			shb.appendHtmlConstant("<div>");
			shb.appendEscaped(I18N.INSTANCE.overridePaymentNoteQuestion());
			shb.appendHtmlConstant("</div>");
			shb.appendHtmlConstant("<div style=\"font-style: italic;\">");
			shb.appendEscaped(payment.getDefaultPaymentNote());
			shb.appendHtmlConstant("</div>");
			Notification.showConfirm(shb.toSafeHtml(), new NotificationCallback<Boolean>() {
				
				@Override
				public void onNotificationClosed(Boolean value) {
					if(value){
						getView().getPaymentNote().setText(payment.getDefaultPaymentNote());
					}
				}
			});
		}
	}
	
	@Override
	public void onPaymentClear() {
		getView().getMakePaymentAsDefault().setVisible(false);
		getView().getMakePaymentAsDefault().setValue(false);
	}

}
