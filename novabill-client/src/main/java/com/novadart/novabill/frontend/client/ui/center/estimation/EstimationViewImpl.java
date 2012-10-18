package com.novadart.novabill.frontend.client.ui.center.estimation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.datawatcher.DataWatcher;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.place.ClientPlace.DOCUMENTS;
import com.novadart.novabill.frontend.client.place.InvoicePlace;
import com.novadart.novabill.frontend.client.ui.center.AccountDocument;
import com.novadart.novabill.frontend.client.ui.center.EstimationView;
import com.novadart.novabill.frontend.client.ui.center.ItemTable;
import com.novadart.novabill.frontend.client.ui.widget.notification.Notification;
import com.novadart.novabill.frontend.client.util.CalcUtils;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.validation.ErrorObject;

public class EstimationViewImpl extends AccountDocument implements EstimationView {

	private static EstimationViewImplUiBinder uiBinder = GWT
			.create(EstimationViewImplUiBinder.class);

	interface EstimationViewImplUiBinder extends UiBinder<Widget, EstimationViewImpl> {
	}
	
	@UiField FlowPanel docControls;
	@UiField ScrollPanel docScroll;

	@UiField(provided=true) ListBox tax;
	@UiField(provided=true) ItemTable itemTable;
	@UiField ScrollPanel itemTableScroller;

	@UiField TextBox item;
	@UiField TextBox quantity;
	@UiField TextBox unitOfMeasure;
	@UiField TextBox price;
	@UiField Label clientName;
	@UiField(provided=true) DateBox date;
	@UiField TextArea note;
	@UiField Button createEstimation;
	@UiField Button modifyDocument;
	@UiField Button convertToInvoice;

	@UiField Label totalBeforeTaxes;
	@UiField Label totalTax;
	@UiField Label totalAfterTaxes;


	private Presenter presenter;
	private EstimationDTO estimation;
	private ListDataProvider<AccountingDocumentItemDTO> accountingDocumentItems = new ListDataProvider<AccountingDocumentItemDTO>();
	private ClientDTO client;

	public EstimationViewImpl() {
		tax = new ListBox();
		for (String item : I18N.INSTANCE.vatItems()) {
			tax.addItem(item+"%", item);
		}
		itemTable = new ItemTable(new ItemTable.Handler() {

			@Override
			public void onDelete(AccountingDocumentItemDTO item) {
				accountingDocumentItems.getList().remove(item);
				accountingDocumentItems.refresh();
				updateFields();
			}
		});
		accountingDocumentItems.addDataDisplay(itemTable);

		date = new DateBox();
		date.setFormat(new DateBox.DefaultFormat
				(DateTimeFormat.getFormat("dd MMMM yyyy")));
		initWidget(uiBinder.createAndBindUi(this));
		setStyleName("AccountDocumentView");
	}
	
	@Override
	protected Element getBody() {
		return docScroll.getElement();
	}
	
	@Override
	protected Element getHeader() {
		return docControls.getElement();
	}

	@UiFactory
	I18N getI18N(){
		return I18N.INSTANCE;
	}



	@UiHandler("convertToInvoice")
	void onConvertToInvoice(ClickEvent e){
		if(!validateEstimation()){
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}

		final EstimationDTO estimation = createEstimation(this.estimation);
		
		ServerFacade.invoice.getNextInvoiceDocumentID(new WrappedAsyncCallback<Long>() {

			@Override
			public void onSuccess(Long result) {
				InvoicePlace ip = new InvoicePlace();
				ip.setDataForNewInvoice(result, estimation);
				presenter.goTo(ip);
			}

			@Override
			public void onException(Throwable caught) {
			}
		});
	}


	@UiHandler("createEstimation")
	void onCreateEstimationClicked(ClickEvent e){
		if(!validateEstimation()){
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}

		EstimationDTO estimation = createEstimation(null);

		ServerFacade.estimation.add(estimation, new WrappedAsyncCallback<Long>() {

			@Override
			public void onSuccess(Long result) {
				Notification.showMessage(I18N.INSTANCE.estimationCreationSuccess());

				DataWatcher.getInstance().fireEstimationEvent();

				ClientPlace cp = new ClientPlace();
				cp.setClientId(client.getId());
				cp.setDocumentsListing(DOCUMENTS.estimations);
				presenter.goTo(cp);
			}

			@Override
			public void onException(Throwable caught) {
				if(caught instanceof ValidationException){
					handleServerValidationException((ValidationException) caught, false);
				} else {
					Notification.showMessage(I18N.INSTANCE.estimationCreationFailure());
				}
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

		es.setAccountingDocumentDate(date.getValue());
		List<AccountingDocumentItemDTO> invItems = new ArrayList<AccountingDocumentItemDTO>();
		for (AccountingDocumentItemDTO itemDTO : accountingDocumentItems.getList()) {
			invItems.add(itemDTO);
		}
		es.setItems(invItems);
		es.setNote(note.getText());
		CalcUtils.calculateTotals(invItems, es);
		return es;
	}


	@UiHandler("add")
	void onAddClicked(ClickEvent e){
		AccountingDocumentItemDTO ii = CalcUtils.createAccountingDocumentItem(item.getText(), price.getText(), 
				quantity.getText(), unitOfMeasure.getText(), tax.getValue(tax.getSelectedIndex()));
		
		if(ii == null) {
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}
		
		accountingDocumentItems.getList().add(ii);
		updateFields();
		itemTableScroller.scrollToBottom();
	}

	@UiHandler("modifyDocument")
	void onModifyEstimationClicked(ClickEvent e){
		if(!validateEstimation()){
			return;
		}

		if(Notification.showYesNoRequest(I18N.INSTANCE.saveModificationsConfirm()) ){
			final EstimationDTO es = createEstimation(estimation);

			ServerFacade.estimation.update(es, new WrappedAsyncCallback<Void>() {

				@Override
				public void onException(Throwable caught) {
					if(caught instanceof ValidationException){
						handleServerValidationException((ValidationException) caught, false);
					} else {
						Notification.showMessage(I18N.INSTANCE.estimationUpdateFailure());
					}
				}

				@Override
				public void onSuccess(Void result) {
					Notification.showMessage(I18N.INSTANCE.estimationUpdateSuccess());

					DataWatcher.getInstance().fireEstimationEvent();

					ClientPlace cp = new ClientPlace();
					cp.setClientId(es.getClient().getId());
					cp.setDocumentsListing(DOCUMENTS.estimations);
					presenter.goTo(cp);
				}
			});
		}
	}

	@UiHandler("abort")
	void onCancelClicked(ClickEvent e){
		if(Notification.showYesNoRequest(I18N.INSTANCE.cancelModificationsConfirmation()) ){
			ClientPlace cp = new ClientPlace();
			cp.setClientId(client.getId());
			presenter.goTo(cp);
		}
	}


	@Override
	public void setDataForNewEstimation(ClientDTO client) {
		this.client =client;

		clientName.setText(client.getName());
		date.setValue(new Date());

		createEstimation.setVisible(true);
		convertToInvoice.setVisible(true);
	}

	@Override
	public void setDataForNewEstimation(ClientDTO client,
			EstimationDTO estimation) {
		setDataForNewEstimation(client);
		setEstimation(estimation, true);
	}


	private void setEstimation(EstimationDTO estimation, boolean cloning) {
		if(!cloning){
			this.estimation = estimation;
			this.client = estimation.getClient();
			date.setValue(estimation.getAccountingDocumentDate());
			clientName.setText(estimation.getClient().getName());

			modifyDocument.setVisible(true);

			convertToInvoice.setVisible(true);
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

		accountingDocumentItems.setList(items);
		note.setText(estimation.getNote());

		updateFields();
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
		if(date.getTextBox().getText().isEmpty() || date.getValue() == null){
			return false;
		} else if(accountingDocumentItems.getList().isEmpty()){
			return false;
		}
		return true;
	}


	private void updateFields(){
		CalcUtils.calculateTotals(accountingDocumentItems.getList(), totalTax, totalBeforeTaxes, totalAfterTaxes);
		resetItemTableForm();
		accountingDocumentItems.refresh();
	}

	private void resetItemTableForm(){
		item.setText("");
		quantity.setText("");
		unitOfMeasure.setText("");
		price.setText("");
		tax.setSelectedIndex(0);
	}

	@Override
	public void setClean() {
		//clean internal data		
		this.client = null;
		this.estimation = null;

		//reset widget statuses
		createEstimation.setVisible(false);
		modifyDocument.setVisible(false);
		convertToInvoice.setVisible(false);

		//reset widget contents		
		note.setText("");
		accountingDocumentItems.getList().clear();
		accountingDocumentItems.refresh();
		totalTax.setText("");
		totalBeforeTaxes.setText("");
		totalAfterTaxes.setText("");
		resetItemTableForm();
	}

	private void handleServerValidationException(ValidationException ex, boolean isInvoice){
		for (ErrorObject eo : ex.getErrors()) {
			switch(eo.getErrorCode()){

			default:
				break;
			}
		}
	}

}
