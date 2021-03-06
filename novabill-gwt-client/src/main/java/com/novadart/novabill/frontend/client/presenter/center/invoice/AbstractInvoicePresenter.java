package com.novadart.novabill.frontend.client.presenter.center.invoice;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.gwtshared.client.validation.widget.ValidatedWidget;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.bridge.BridgeUtils;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.presenter.center.DocumentPresenter;
import com.novadart.novabill.frontend.client.util.CalcUtils;
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.view.center.invoice.InvoiceView;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.dto.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class AbstractInvoicePresenter extends DocumentPresenter<InvoiceView> implements InvoiceView.Presenter {

	private InvoiceDTO invoice;
	private PaymentTypeDTO cachedDefaultPaymentTypeDTO = null;
	private final List<DocumentIDClassDTO> documentIDClasses;

	public AbstractInvoicePresenter(PlaceController placeController, EventBus eventBus,	InvoiceView view,
									List<DocumentIDClassDTO> documentIDClassesDTOs, JavaScriptObject callback) {
		super(placeController, eventBus, view, callback);
		this.documentIDClasses = documentIDClassesDTOs;
	}

	public List<DocumentIDClassDTO> getDocumentIDClasses() {
		return documentIDClasses;
	}

	@Override
	public void onDateChanged(Date date){
		getView().getInvoiceNumberSuffix().setText(" / "+ getYearFormat().format(date));
		getView().getPayment().setDocumentCreationDate(date);
	}


	@Override
	public void onCancelClicked() {
		Notification.showConfirm(I18N.INSTANCE.cancelModificationsConfirmation(), new NotificationCallback() {

			@Override
			public void onNotificationClosed(boolean value) {
				if (value) {
					BridgeUtils.invokeJSCallback(Boolean.FALSE, getCallback());
				}
			}
		});
	}

	@Override
	public void onDocumentIdClassChange() {
		String docIdClass = getDocIdClass();
		if(invoice == null){
			getView().getNumber().setEnabled(false);
			ServerFacade.INSTANCE.getInvoiceService().getNextInvoiceDocumentID(docIdClass, new ManagedAsyncCallback<Long>() {
				@Override
				public void onSuccess(Long aLong) {
					getView().getNumber().setText(String.valueOf(aLong));
					getView().getNumber().setEnabled(true);
				}

				@Override
				public void onFailure(Throwable caught) {
					super.onFailure(caught);
					getView().getNumber().setEnabled(true);
				}
			});
		} else {
			if( (docIdClass == null && invoice.getDocumentIDSuffix() == null)
					|| (docIdClass != null && docIdClass.equalsIgnoreCase(invoice.getDocumentIDSuffix())) ){
				getView().getNumber().setText(String.valueOf(invoice.getDocumentID()));
			} else {
				getView().getNumber().setEnabled(false);
				ServerFacade.INSTANCE.getInvoiceService().getNextInvoiceDocumentID(docIdClass, new ManagedAsyncCallback<Long>() {
					@Override
					public void onSuccess(Long aLong) {
						getView().getNumber().setText(String.valueOf(aLong));
						getView().getNumber().setEnabled(true);
					}

					@Override
					public void onFailure(Throwable caught) {
						super.onFailure(caught);
						getView().getNumber().setEnabled(true);
					}
				});
			}
		}
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

		if(!getView().getItemInsertionForm().isValid()){
			return false;
		}

		if(!getView().getDate().isValid() || !getView().getNumber().isValid() || !getView().getPayment().isValid()){
			return false;
		}
		
        boolean validation = true;

		if(getView().getSetToAddress().getValue()){
		    for (ValidatedWidget<?> vw : new ValidatedWidget<?>[]{getView().getToAddrCountry(), getView().getToAddrCity(), 
				    getView().getToAddrCompanyName(), getView().getToAddrPostCode(),	getView().getToAddrStreetName()}) {
			    vw.validate();
			    validation = validation && vw.isValid();
		    }
	    }

        if(getView().getSetToAddress().getValue() && getView().getToAddrCountry().getSelectedItemValue().equalsIgnoreCase("IT")){
			getView().getToAddrProvince().validate();
			validation = validation && getView().getToAddrProvince().isValid();
		}

		return validation;
	}

	protected InvoiceDTO createInvoice(InvoiceDTO invoice){
		InvoiceDTO inv;

		if(invoice != null){
			inv = invoice;
		} else {
			inv = new InvoiceDTO();
			inv.setLayoutType(Configuration.getBusiness().getSettings().getDefaultLayoutType());
			inv.setBusiness(Configuration.getBusiness());
			inv.setClient(getClient());
		}

		inv.setDocumentID(Long.parseLong(getView().getNumber().getText()));
		inv.setDocumentIDSuffix(getDocIdClass());

		inv.setAccountingDocumentDate(DocumentUtils.createNormalizedDate(getView().getDate().getValue()) );
		List<AccountingDocumentItemDTO> invItems = new ArrayList<AccountingDocumentItemDTO>();
		for (AccountingDocumentItemDTO itemDTO : getView().getItemInsertionForm().getItems()) {
			invItems.add(itemDTO);
		}
		inv.setItems(invItems);
		inv.setNote(getView().getNote().getText());

		if(getView().getSetWithholdingTax().getValue()){
			inv.setWitholdTax(true);
			inv.setWitholdTaxPercentFirstLevel(new BigDecimal(getView().getWithholdingTaxFirstLevel().getText()));
			inv.setWitholdTaxPercentSecondLevel(new BigDecimal(getView().getWithholdingTaxSecondLevel().getText()));
		} else {
			inv.setWitholdTax(false);
			inv.setWitholdTaxPercentFirstLevel(null);
			inv.setWitholdTaxPercentSecondLevel(null);
		}
		
		if(!getView().getSetToAddress().getValue()){
			getView().getToAddrCity().setText(getClient().getCity());
			getView().getToAddrCompanyName().setText(getClient().getName());
			getView().getToAddrPostCode().setText(getClient().getPostcode());
			getView().getToAddrProvince().setText(getClient().getProvince());
			getView().getToAddrStreetName().setText(getClient().getAddress());
			getView().getToAddrCountry().setSelectedItemByValue(getClient().getCountry());
		}

		EndpointDTO loc = new EndpointDTO();
		loc.setCompanyName(getView().getToAddrCompanyName().getText());
		loc.setCity(getView().getToAddrCity().getText());
		loc.setPostcode(getView().getToAddrPostCode().getText());
		loc.setProvince(getView().getToAddrProvince().getText());
		loc.setStreet(getView().getToAddrStreetName().getText());
		loc.setCountry(getView().getToAddrCountry().getSelectedItemValue());
		inv.setToEndpoint(loc);
		
		inv.setPaymentTypeName(getView().getPayment().getSelectedPayment().getName());
		inv.setPaymentDateDelta(getView().getPayment().getSelectedPayment().getPaymentDateDelta());
		inv.setPaymentDateGenerator(getView().getPayment().getSelectedPayment().getPaymentDateGenerator());
		inv.setPaymentDeltaType(getView().getPayment().getSelectedPayment().getPaymentDeltaType());
		inv.setSecondaryPaymentDateDelta(getView().getPayment().getSelectedPayment().getSecondaryPaymentDateDelta());
		inv.setPaymentDueDate(getView().getPayment().getPaymentDueDate());
		inv.setSplitPayment(getView().getSelectSplitPayment().isItemSelected(1));
		inv.setPaymentNote(getView().getPaymentNote().getText());

		CalcUtils.calculateTotals(invItems, inv);
		return inv;
	}

	private String getDocIdClass(){
		int docIdSelectedIndex = getView().getDocumentIDClassListBox().getSelectedIndex();
		return docIdSelectedIndex == 0 ? null : getView().getDocumentIDClassListBox().getValue(docIdSelectedIndex);
	}


	@Override
	public void onPaymentSelected(final PaymentTypeDTO payment) {

		//it is null when the "Paid in full" is selected
		if(payment.getId() != null) {
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
			Notification.showConfirm(I18N.INSTANCE.overridePaymentNoteQuestion() + "\""+payment.getDefaultPaymentNote()+"\"?", new NotificationCallback() {

				@Override
				public void onNotificationClosed(boolean value) {
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
