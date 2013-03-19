package com.novadart.novabill.frontend.client.presenter.center.invoice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.novadart.novabill.frontend.client.event.DocumentAddEvent;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.i18n.I18NM;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.place.ClientPlace.DOCUMENTS;
import com.novadart.novabill.frontend.client.view.center.invoice.InvoiceView;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.ValidationException;

public class NewInvoicePresenter extends AbstractInvoicePresenter {

	public NewInvoicePresenter(PlaceController placeController,	EventBus eventBus, InvoiceView view) {
		super(placeController, eventBus, view);
	}

	@Override
	protected void setPresenterInView(InvoiceView view) {
		view.setPresenter(this);
	}
	
	@Override
	public void onLoad() {
		getView().getCreateDocument().setText(I18N.INSTANCE.createInvoice());
		getView().getTitleLabel().setText(I18N.INSTANCE.newInvoiceCreation());
	}
	
	private void initData(ClientDTO client, Long progressiveId){
		setClient(client);

		getView().getClientName().setText(client.getName());
		Date d = new Date();
		getView().getDate().setValue(d);
		getView().getPayment().setDocumentCreationDate(d);
		getView().getInvoiceNumberSuffix().setText(" / "+ getYearFormat().format(d));
		getView().getNumber().setText(progressiveId.toString());

		getView().getCreateDocument().setVisible(true);
	}
	
	public void setDataForNewInvoice(ClientDTO client, Long progressiveId) {
		initData(client, progressiveId);
		getView().getPayment().init();
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
				invoice.getPaymentDateDelta());
		getView().getItemInsertionForm().setItems(items);
		getView().getNote().setText(invoice.getNote());
		getView().getPaymentNote().setText(invoice.getPaymentNote());
	}

	
	public void setDataForNewInvoice(Long progressiveId, EstimationDTO estimation) {
		initData(estimation.getClient(), progressiveId);
		getView().getPayment().init();

		List<AccountingDocumentItemDTO> items = new ArrayList<AccountingDocumentItemDTO>(estimation.getItems().size());
		for (AccountingDocumentItemDTO i : estimation.getItems()) {
			items.add(i.clone());
		}

		getView().getItemInsertionForm().setItems(items);
		getView().getNote().setText(I18NM.get.generatedFromEstimation(estimation.getDocumentID(), 
				DateTimeFormat.getFormat("dd MMMM yyyy").format(estimation.getAccountingDocumentDate())));
	}

	
	public void setDataForNewInvoice(Long progressiveId, TransportDocumentDTO transportDocument) {
		initData(transportDocument.getClient(), progressiveId);
		getView().getPayment().init();

		List<AccountingDocumentItemDTO> items = new ArrayList<AccountingDocumentItemDTO>(transportDocument.getItems().size());
		for (AccountingDocumentItemDTO i : transportDocument.getItems()) {
			items.add(i.clone());
		}

		getView().getItemInsertionForm().setItems(items);
		getView().getNote().setText(transportDocument.getNote());
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

		ServerFacade.invoice.add(invoice, new ManagedAsyncCallback<Long>() {

			@Override
			public void onSuccess(Long result) {
				getView().getCreateDocument().showLoader(false);
				Notification.showMessage(I18N.INSTANCE.invoiceCreationSuccess(), new NotificationCallback<Void>() {

					@Override
					public void onNotificationClosed(Void value) {
						getEventBus().fireEvent(new DocumentAddEvent(invoice));

						ClientPlace cp = new ClientPlace();
						cp.setClientId(getClient().getId());
						cp.setDocs(DOCUMENTS.invoices);
						goTo(cp);

						getView().setLocked(false);
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

}
