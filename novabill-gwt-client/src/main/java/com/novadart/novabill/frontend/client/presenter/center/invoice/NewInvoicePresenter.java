package com.novadart.novabill.frontend.client.presenter.center.invoice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.bridge.BridgeUtils;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.i18n.I18NM;
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.view.center.invoice.InvoiceView;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EndpointDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.ValidationException;

public class NewInvoicePresenter extends AbstractInvoicePresenter {

	private List<Long> transportDocumentSources = null;
	
	public NewInvoicePresenter(PlaceController placeController,	EventBus eventBus, InvoiceView view, JavaScriptObject callback) {
		super(placeController, eventBus, view, callback);
	}

	@Override
	protected void setPresenterInView(InvoiceView view) {
		view.setPresenter(this);
	}
	
	public void setTransportDocumentSources(List<Long> transportDocumentSources) {
		this.transportDocumentSources = transportDocumentSources;
	}
	
	private void initData(ClientDTO client, Long progressiveId){
		setClient(client);

		getView().getClientName().setText(client.getName());
		Date d = DocumentUtils.createNormalizedDate(new Date());
		getView().getDate().setValue(d);
		getView().getInvoiceNumberSuffix().setText(" / "+ getYearFormat().format(d));
		getView().getNumber().setText(progressiveId.toString());

		getView().getCreateDocument().setVisible(true);
	}

	public void setDataForNewInvoice(ClientDTO client, Long progressiveId, PaymentTypeDTO paymentType) {
		initData(client, progressiveId);
		setupPayment(paymentType);
	}


	public void setDataForNewInvoice(ClientDTO client, Long progressiveId, InvoiceDTO invoice) {
		initData(client, progressiveId);

		List<AccountingDocumentItemDTO> items = null;
		items = new ArrayList<AccountingDocumentItemDTO>(invoice.getItems().size());
		for (AccountingDocumentItemDTO i : invoice.getItems()) {
			items.add(i.clone());
		}
		getView().getPayment().setDocumentCreationDate(getView().getDate().getValue());
		getView().getPayment().init(invoice.getPaymentTypeName(), invoice.getPaymentDateGenerator(), 
				invoice.getPaymentDateDelta(), invoice.getPaymentDeltaType(), invoice.getSecondaryPaymentDateDelta());
		
		//NOTE obviously in this case we don't load the endpoint as it is specific to the client
		
		//NOTE we don't show the checkbox to set this as the default payment because we don't know its ID
		getView().getItemInsertionForm().setItems(items);
		getView().getNote().setText(invoice.getNote());
		getView().getPaymentNote().setText(invoice.getPaymentNote());
	}


	public void setDataForNewInvoice(Long progressiveId, EstimationDTO estimation, PaymentTypeDTO paymentType) {
		initData(estimation.getClient(), progressiveId);
		setupPayment(paymentType);

		List<AccountingDocumentItemDTO> items = new ArrayList<AccountingDocumentItemDTO>(estimation.getItems().size());
		for (AccountingDocumentItemDTO i : estimation.getItems()) {
			items.add(i.clone());
		}
		getView().getItemInsertionForm().setItems(items);
		
        EndpointDTO loc = estimation.getToEndpoint();
		getView().getToAddrCity().setText(loc.getCity());
		getView().getToAddrCompanyName().setText(loc.getCompanyName());
		getView().getToAddrPostCode().setText(loc.getPostcode());
		getView().getToAddrProvince().setText(loc.getProvince());
		getView().getToAddrStreetName().setText(loc.getStreet());
		getView().getToAddrCountry().setSelectedItemByValue(loc.getCountry());
		getView().getSetToAddress().setValue(true);
		getView().getToAddressContainer().setVisible(true);
		
		getView().getNote().setText(I18NM.get.generatedFromEstimation(estimation.getDocumentID(), 
				DateTimeFormat.getFormat("dd MMMM yyyy").format(estimation.getAccountingDocumentDate())));
	}
	
	
	public void setDataForNewInvoice(Long progressiveId, List<TransportDocumentDTO> transportDocuments, PaymentTypeDTO paymentType) {
		initData(transportDocuments.get(0).getClient(), progressiveId);
		setupPayment(paymentType);

		List<AccountingDocumentItemDTO> items = new ArrayList<AccountingDocumentItemDTO>();
		
		AccountingDocumentItemDTO item;
		for(TransportDocumentDTO td : transportDocuments){
			
			item = new AccountingDocumentItemDTO();
			item.setDescription("##  "
			+ I18N.INSTANCE.transportDocumentShort()
			+" N. "+td.getDocumentID()
			+"  "
			+ I18N.INSTANCE.of() +" "
			+ DocumentUtils.DOCUMENT_DATE_FORMAT.format(td.getAccountingDocumentDate()) 
			+ "  ##");
			
			items.add(item);
			
			for (AccountingDocumentItemDTO i : td.getItems()) {
				items.add(i.clone());
			}
			
		}
		
		getView().getItemInsertionForm().setItems(items);
	}

	

	@Override
	public void onCreateDocumentClicked() {
		if(!validateInvoice()){
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}

		getView().getCreateDocument().showLoader(true);
		getView().setLocked(true);

		final InvoiceDTO invoice = createInvoice(null);
		invoice.setTransportDocumentIDs(transportDocumentSources);
		invoice.setCreatedFromTransportDocuments(transportDocumentSources!=null);

		final ManagedAsyncCallback<Void> updateClientCallback = new ManagedAsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				ServerFacade.INSTANCE.getInvoiceService().add(invoice, new ManagedAsyncCallback<Long>() {

					@Override
					public void onSuccess(Long result) {
						getView().getCreateDocument().showLoader(false);
						Notification.showMessage(I18N.INSTANCE.invoiceCreationSuccess(), new NotificationCallback() {

							@Override
							public void onNotificationClosed(boolean value) {
								getView().setLocked(false);
								BridgeUtils.invokeJSCallback(Boolean.TRUE, getCallback());
							}
						});

					}

					@Override
					public void onFailure(Throwable caught) {
						getView().getCreateDocument().showLoader(false);
						if(caught instanceof ValidationException){
							handleServerValidationException((ValidationException) caught);
						} else {
							Notification.showMessage(I18N.INSTANCE.invoiceCreationFailure());
							super.onFailure(caught);
						}
						getView().setLocked(false);
					}
				});
			}

			@Override
			public void onFailure(Throwable caught) {
				getView().getCreateDocument().showLoader(false);
				Notification.showMessage(I18N.INSTANCE.invoiceCreationFailure());
				super.onFailure(caught);
				getView().setLocked(false);
			}
		};

		if(getView().getMakePaymentAsDefault().getValue()){
			// if the user decides to make this the default payment, update this info then add the invoice
			ClientDTO client = getClient();
			client.setDefaultPaymentTypeID(getView().getPayment().getSelectedPayment().getId());

			ServerFacade.INSTANCE.getClientService().update(Configuration.getBusinessId(), client, updateClientCallback);
		} else {
			// otherwise just add the invoice
			updateClientCallback.onSuccess(null);
		}

	}

	private void setupPayment(PaymentTypeDTO defaultPayment){
		getView().getPayment().setDocumentCreationDate(getView().getDate().getValue());
		if(defaultPayment == null) {
			getView().getPayment().init();
		} else {
			getView().getPayment().init(defaultPayment);
			
			//if the payment note was not inherited from a previous invoice, fill it
			if(getView().getPaymentNote().isEmpty()){
				getView().getPaymentNote().setText(defaultPayment.getDefaultPaymentNote());
			} 
		}
	}


}
