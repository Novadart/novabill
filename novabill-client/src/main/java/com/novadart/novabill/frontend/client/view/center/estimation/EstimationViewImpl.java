package com.novadart.novabill.frontend.client.view.center.estimation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.novadart.gwtshared.client.LoaderButton;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.event.DocumentAddEvent;
import com.novadart.novabill.frontend.client.event.DocumentUpdateEvent;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.place.ClientPlace.DOCUMENTS;
import com.novadart.novabill.frontend.client.place.invoice.FromEstimationInvoicePlace;
import com.novadart.novabill.frontend.client.presenter.Presenter;
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.view.HasUILocking;
import com.novadart.novabill.frontend.client.view.center.AccountDocument;
import com.novadart.novabill.frontend.client.view.center.ItemInsertionForm;
import com.novadart.novabill.frontend.client.widget.ValidatedTextArea;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.notification.NotificationCallback;
import com.novadart.novabill.frontend.client.widget.validation.ValidationKit;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.exception.ValidationException;

public class EstimationViewImpl extends AccountDocument implements EstimationView, HasUILocking {

	private static EstimationViewImplUiBinder uiBinder = GWT
			.create(EstimationViewImplUiBinder.class);

	interface EstimationViewImplUiBinder extends UiBinder<Widget, EstimationViewImpl> {
	}
	
	@UiField Label titleLabel;
	@UiField FlowPanel docControls;
	@UiField ScrollPanel docScroll;

	@UiField Label clientName;
	@UiField(provided=true) ValidatedTextBox number;
	@UiField(provided=true) DateBox date;
	@UiField(provided=true) DateBox validTill;
	@UiField ValidatedTextArea note;
	@UiField ValidatedTextArea paymentNote;
	@UiField ValidatedTextArea limitations;
	
	@UiField(provided=true) ItemInsertionForm itemInsertionForm;
	
	@UiField Label totalBeforeTaxes;
	@UiField Label totalTax;
	@UiField Label totalAfterTaxes;
	
	@UiField LoaderButton modifyDocument;
	@UiField LoaderButton createEstimation;
	@UiField LoaderButton convertToInvoice;
	@UiField Button abort;
	
	
	private Presenter presenter;
	private EventBus eventBus;
	private EstimationDTO estimation;
	private ClientDTO client;

	public EstimationViewImpl() {
		itemInsertionForm = new ItemInsertionForm(new ItemInsertionForm.Handler() {
			
			@Override
			public void onItemListUpdated(List<AccountingDocumentItemDTO> items) {
				DocumentUtils.calculateTotals(itemInsertionForm.getItems(), totalTax, totalBeforeTaxes, totalAfterTaxes);
			}
			
		});
		
		number = new ValidatedTextBox(ValidationKit.NUMBER);

		date = new DateBox();
		date.setFormat(new DateBox.DefaultFormat
				(DateTimeFormat.getFormat("dd MMMM yyyy")));
		validTill = new DateBox();
		validTill.setFormat(new DateBox.DefaultFormat
				(DateTimeFormat.getFormat("dd MMMM yyyy")));
		initWidget(uiBinder.createAndBindUi(this));
		setStyleName("AccountDocumentView");
		
		modifyDocument.getButton().setStyleName("modifyButton button");
		createEstimation.getButton().setStyleName("createButton button");
		convertToInvoice.getButton().setStyleName("convertToInvoice button");
		
	}
	
	@Override
	protected Element getBody() {
		return docScroll.getElement();
	}
	
	@Override
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}
	
	@Override
	protected Element[] getNonBodyElements() {
		return new Element[]{titleLabel.getElement(), docControls.getElement()};
	}

	@UiFactory
	I18N getI18N(){
		return I18N.INSTANCE;
	}

	@Override
	protected ScrollPanel getDocScroll() {
		return docScroll;
	}
	
	@Override
	protected ValidatedTextBox getNumber() {
		return number;
	}


	@UiHandler("convertToInvoice")
	void onConvertToInvoice(ClickEvent e){
		if(!validateEstimation()){
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}
		
		convertToInvoice.showLoader(true);
		setLocked(true);
		modifyDocument.getButton().setEnabled(false);
		createEstimation.getButton().setEnabled(false);
		
		final EstimationDTO estimation = createEstimation(this.estimation);
		
		if(this.estimation == null) {
			
			ServerFacade.estimation.add(estimation, new ManagedAsyncCallback<Long>() {

				@Override
				public void onSuccess(Long result) {
					convertToInvoice.showLoader(false);
					FromEstimationInvoicePlace pl = new FromEstimationInvoicePlace();
					pl.setEstimationId(result);
					presenter.goTo(pl);
					
					setLocked(false);
					modifyDocument.getButton().setEnabled(true);
					createEstimation.getButton().setEnabled(true);
				}

				@Override
				public void onFailure(Throwable caught) {
					convertToInvoice.showLoader(false);
					super.onFailure(caught);
					
					setLocked(false);
					modifyDocument.getButton().setEnabled(true);
					createEstimation.getButton().setEnabled(true);
				}
			});
			
		} else {
			
			ServerFacade.estimation.update(estimation, new ManagedAsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					convertToInvoice.showLoader(false);
					FromEstimationInvoicePlace pl = new FromEstimationInvoicePlace();
					pl.setEstimationId(EstimationViewImpl.this.estimation.getId());
					presenter.goTo(pl);
					
					setLocked(false);
					modifyDocument.getButton().setEnabled(true);
					createEstimation.getButton().setEnabled(true);
				}
				
				
				@Override
				public void onFailure(Throwable caught) {
					convertToInvoice.showLoader(false);
					super.onFailure(caught);
					
					setLocked(false);
					modifyDocument.getButton().setEnabled(true);
					createEstimation.getButton().setEnabled(true);
				}
			});
			
		}
		
	}


	@UiHandler("createEstimation")
	void onCreateEstimationClicked(ClickEvent e){
		if(!validateEstimation()){
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}
		
		createEstimation.showLoader(true);
		setLocked(true);
		modifyDocument.getButton().setEnabled(false);
		convertToInvoice.getButton().setEnabled(false);

		final EstimationDTO estimation = createEstimation(null);
		ServerFacade.estimation.add(estimation, new ManagedAsyncCallback<Long>() {

			@Override
			public void onSuccess(Long result) {
				createEstimation.showLoader(false);
				Notification.showMessage(I18N.INSTANCE.estimationCreationSuccess());

				eventBus.fireEvent(new DocumentAddEvent(estimation));

				ClientPlace cp = new ClientPlace();
				cp.setClientId(client.getId());
				cp.setDocs(DOCUMENTS.estimations);
				presenter.goTo(cp);
				
				setLocked(false);
				modifyDocument.getButton().setEnabled(true);
				convertToInvoice.getButton().setEnabled(true);
			}

			@Override
			public void onFailure(Throwable caught) {
				createEstimation.showLoader(false);
				if(caught instanceof ValidationException){
					handleServerValidationException((ValidationException) caught);
				} else {
					super.onFailure(caught);
				}
				
				setLocked(false);
				modifyDocument.getButton().setEnabled(true);
				convertToInvoice.getButton().setEnabled(true);
			}
		});

	}



	private EstimationDTO createEstimation(EstimationDTO estimation){
		EstimationDTO es;

		if(estimation != null){
			es = estimation;
		} else {
			es = new EstimationDTO();
			es.setBusiness(Configuration.getBusiness());
			es.setClient(client);
		}

		es.setDocumentID(Long.parseLong(number.getText()));
		
		es.setAccountingDocumentDate(date.getValue());
		es.setValidTill(validTill.getValue());
		List<AccountingDocumentItemDTO> invItems = new ArrayList<AccountingDocumentItemDTO>();
		for (AccountingDocumentItemDTO itemDTO : itemInsertionForm.getItems()) {
			invItems.add(itemDTO);
		}
		es.setItems(invItems);
		es.setNote(note.getText());
		es.setPaymentNote(paymentNote.getText());
		es.setLimitations(limitations.getText());
		DocumentUtils.calculateTotals(invItems, es);
		return es;
	}


	

	@UiHandler("modifyDocument")
	void onModifyEstimationClicked(ClickEvent e){
		if(!validateEstimation()){
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}
		
		Notification.showConfirm(I18N.INSTANCE.saveModificationsConfirm(), new NotificationCallback<Boolean>() {
			
			@Override
			public void onNotificationClosed(Boolean value) {
				if(value){
					
					modifyDocument.showLoader(true);
					setLocked(true);
					createEstimation.getButton().setEnabled(false);
					convertToInvoice.getButton().setEnabled(false);
					
					
					final EstimationDTO es = createEstimation(estimation);

					ServerFacade.estimation.update(es, new ManagedAsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							modifyDocument.showLoader(false);
							if(caught instanceof ValidationException){
								handleServerValidationException((ValidationException) caught);
							} else {
								super.onFailure(caught);
							}
							
							setLocked(false);
							createEstimation.getButton().setEnabled(true);
							convertToInvoice.getButton().setEnabled(true);
						}

						@Override
						public void onSuccess(Void result) {
							modifyDocument.showLoader(false);
							
							Notification.showMessage(I18N.INSTANCE.estimationUpdateSuccess(), new NotificationCallback<Void>() {
								
								@Override
								public void onNotificationClosed(Void value) {
									eventBus.fireEvent(new DocumentUpdateEvent(es));

									ClientPlace cp = new ClientPlace();
									cp.setClientId(es.getClient().getId());
									cp.setDocs(DOCUMENTS.estimations);
									presenter.goTo(cp);
									
									setLocked(false);
									createEstimation.getButton().setEnabled(true);
									convertToInvoice.getButton().setEnabled(true);
								}
							});
						}
					});
				}
			}
		});

	}

	@UiHandler("abort")
	void onCancelClicked(ClickEvent e){
		Notification.showConfirm(I18N.INSTANCE.cancelModificationsConfirmation(), new NotificationCallback<Boolean>() {
			
			@Override
			public void onNotificationClosed(Boolean value) {
				if(value){
					ClientPlace cp = new ClientPlace();
					cp.setClientId(client.getId());
					cp.setDocs(DOCUMENTS.estimations);
					presenter.goTo(cp);
				}
			}
		});
	}


	@Override
	public void setDataForNewEstimation(ClientDTO client, Long progressiveId) {
		this.client =client;

		clientName.setText(client.getName());
		number.setText(progressiveId.toString());
		Date now = new Date();
		date.setValue(now);
		validTill.setValue(new Date(now.getTime() + 2592000000L));

		createEstimation.setVisible(true);
		convertToInvoice.setVisible(true);
	}

	@Override
	public void setDataForNewEstimation(ClientDTO client, Long progressiveId,
			EstimationDTO estimation) {
		setDataForNewEstimation(client, progressiveId);
		setEstimation(estimation, true);
	}


	private void setEstimation(EstimationDTO estimation, boolean cloning) {
		if(!cloning){
			this.estimation = estimation;
			this.client = estimation.getClient();
			date.setValue(estimation.getAccountingDocumentDate());
			validTill.setValue(estimation.getValidTill());
			clientName.setText(estimation.getClient().getName());
			modifyDocument.setVisible(true);

			convertToInvoice.setVisible(true);
			
			titleLabel.setText(I18N.INSTANCE.modifyEstimation());
		}

		List<AccountingDocumentItemDTO> items = null;
		if(cloning) {
			items = new ArrayList<AccountingDocumentItemDTO>(estimation.getItems().size());
			for (AccountingDocumentItemDTO i : estimation.getItems()) {
				items.add(i.clone());
			}
		} else {
			items = estimation.getItems();
		}
		
		if(!cloning && estimation.getDocumentID() != null){
			number.setText(estimation.getDocumentID().toString());
		} 

		itemInsertionForm.setItems(items);
		note.setText(estimation.getNote());
		paymentNote.setText(estimation.getPaymentNote());
		limitations.setText(estimation.getLimitations());
	}


	@Override
	public void setEstimation(EstimationDTO estimation) {
		setEstimation(estimation, false);
	}



	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}


	private boolean validateEstimation(){
		if(date.getTextBox().getText().isEmpty() || date.getValue() == null 
				|| validTill.getTextBox().getText().isEmpty() || validTill.getValue() == null){
			return false;
		} 
		
		if(itemInsertionForm.getItems().isEmpty()){
			return false;
		}
		number.validate();
		return number.isValid();
	}


	@Override
	public void clean() {
		//clean internal data		
		this.client = null;
		this.estimation = null;
		
		number.reset();

		//reset widget statuses
		createEstimation.setVisible(false);
		modifyDocument.setVisible(false);
		convertToInvoice.setVisible(false);

		//reset widget contents		
		note.setText("");
		paymentNote.setText("");
		limitations.setText("");
		totalTax.setText("");
		totalBeforeTaxes.setText("");
		totalAfterTaxes.setText("");
		itemInsertionForm.reset();
		
		titleLabel.setText(I18N.INSTANCE.newEstimationCreation());
		
		modifyDocument.reset();
		createEstimation.reset();
		convertToInvoice.reset();
		setLocked(false);
	}

	@Override
	public void setLocked(boolean value) {
		number.setEnabled(!value);
		date.setEnabled(!value);
		validTill.setEnabled(!value);
		note.setEnabled(!value);
		paymentNote.setEnabled(!value);
		limitations.setEnabled(!value);
		
		itemInsertionForm.setLocked(value);
		
		abort.setEnabled(!value);
	}

}
