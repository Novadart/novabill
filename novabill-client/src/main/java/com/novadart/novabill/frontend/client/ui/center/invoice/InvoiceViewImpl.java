package com.novadart.novabill.frontend.client.ui.center.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;
import com.novadart.gwtshared.client.validation.widget.ValidatedListBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.datawatcher.DataWatcher;
import com.novadart.novabill.frontend.client.facade.AuthAwareAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.place.InvoicePlace;
import com.novadart.novabill.frontend.client.ui.center.InvoiceView;
import com.novadart.novabill.frontend.client.ui.widget.notification.Notification;
import com.novadart.novabill.frontend.client.ui.widget.validation.NumberValidation;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.InvoiceItemDTO;
import com.novadart.novabill.shared.client.dto.PaymentType;

public class InvoiceViewImpl extends Composite implements InvoiceView {

	private static InvoiceViewImplUiBinder uiBinder = GWT
			.create(InvoiceViewImplUiBinder.class);

	interface InvoiceViewImplUiBinder extends UiBinder<Widget, InvoiceViewImpl> {
	}

	@UiField Label paymentLabel;
	@UiField(provided=true) ValidatedListBox payment;
	@UiField(provided=true) ListBox tax;
	@UiField(provided=true) ItemTable itemTable;
	@UiField ScrollPanel itemTableScroller;

	@UiField TextBox item;
	@UiField TextBox quantity;
	@UiField TextBox unitOfMeasure;
	@UiField TextBox price;
	@UiField Label clientName;
	@UiField(provided=true) DateBox date;
	@UiField Label invoiceNumber;
	@UiField(provided=true) ValidatedTextBox number;
	@UiField Label paymentNoteLabel;
	@UiField TextArea paymentNote;
	@UiField TextArea note;
	@UiField Button createEstimate;
	@UiField Button createInvoice;
	@UiField Button modifyDocument;
	@UiField Button convertToInvoice;

	@UiField Label totalBeforeTaxes;
	@UiField Label totalTax;
	@UiField Label totalAfterTaxes;


	private Presenter presenter;
	private InvoiceDTO invoice;
	private EstimationDTO estimation;
	private ListDataProvider<InvoiceItemDTO> invoiceItems = new ListDataProvider<InvoiceItemDTO>();
	private ClientDTO client;

	public InvoiceViewImpl() {
		payment = new ValidatedListBox(I18N.INSTANCE.notEmptyValidationError());
		for (String item : I18N.INSTANCE.paymentItems()) {
			payment.addItem(item);
		}
		tax = new ListBox();
		for (String item : I18N.INSTANCE.vatItems()) {
			tax.addItem(item+"%", item);
		}
		number = new ValidatedTextBox(new NumberValidation());
		itemTable = new ItemTable(new ItemTable.Handler() {

			@Override
			public void delete(InvoiceItemDTO item) {
				invoiceItems.getList().remove(item);
				invoiceItems.refresh();
				updateFields();
			}
		});
		invoiceItems.addDataDisplay(itemTable);

		date = new DateBox();
		date.setFormat(new DateBox.DefaultFormat
				(DateTimeFormat.getFormat("dd MMMM yyyy")));
		initWidget(uiBinder.createAndBindUi(this));
		setStyleName("InvoiceView");
	}

	@UiFactory
	I18N getI18N(){
		return I18N.INSTANCE;
	}

	@UiHandler("createInvoice")
	void onCreateInvoiceClicked(ClickEvent e){
		if(!validateInvoice()){
			Notification.showMessage(I18N.INSTANCE.errorInvoiceData());
			return;
		}

		InvoiceDTO invoice = createInvoice(null);

		ServerFacade.invoice.add(invoice, new AuthAwareAsyncCallback<Long>() {

			@Override
			public void onSuccess(Long result) {
				Notification.showMessage(I18N.INSTANCE.invoiceCreationSuccess());

				DataWatcher.getInstance().fireInvoiceEvent();
				DataWatcher.getInstance().fireStatsEvent();

				ClientPlace cp = new ClientPlace();
				cp.setClientId(client.getId());
				presenter.goTo(cp);
			}

			@Override
			public void onException(Throwable caught) {
				Notification.showMessage(I18N.INSTANCE.invoiceCreationFailure());
			}
		});

	}

	@UiHandler("convertToInvoice")
	void onConvertToInvoice(ClickEvent e){
		if(!validateEstimation()){
			Notification.showMessage(I18N.INSTANCE.errorEstimationData());
			return;
		}

		EstimationDTO estimation = createEstimation(this.estimation);
		ServerFacade.invoice.createFromEstimation(estimation, new AuthAwareAsyncCallback<InvoiceDTO>() {

			@Override
			public void onSuccess(InvoiceDTO result) {
				DataWatcher.getInstance().fireInvoiceEvent();
				DataWatcher.getInstance().fireStatsEvent();

				InvoicePlace ip = new InvoicePlace();
				ip.setInvoiceId(result.getId());
				presenter.goTo(ip);
			}

			@Override
			public void onException(Throwable caught) {
				Notification.showMessage(I18N.INSTANCE.invoiceCreationFailure());
			}
		});
	}


	@UiHandler("createEstimate")
	void onCreateEstimateClicked(ClickEvent e){
		if(!validateEstimation()){
			Notification.showMessage(I18N.INSTANCE.errorEstimationData());
			return;
		}

		EstimationDTO estimation = createEstimation(null);

		ServerFacade.estimation.add(estimation, new AuthAwareAsyncCallback<Long>() {

			@Override
			public void onSuccess(Long result) {
				Notification.showMessage(I18N.INSTANCE.estimationCreationSuccess());

				DataWatcher.getInstance().fireEstimationEvent();

				ClientPlace cp = new ClientPlace();
				cp.setClientId(client.getId());
				presenter.goTo(cp);
			}

			@Override
			public void onException(Throwable caught) {
				Notification.showMessage(I18N.INSTANCE.estimationCreationFailure());
			}
		});

	}


	private InvoiceDTO createInvoice(InvoiceDTO invoice){
		InvoiceDTO inv;

		if(invoice != null){
			inv = invoice;
		} else {
			inv = new InvoiceDTO();
			inv.setBusiness(Configuration.getBusiness());
			inv.setClient(client);
		}


		inv.setDocumentID(Long.parseLong(number.getText()));
		inv.setAccountingDocumentDate(date.getValue());
		List<InvoiceItemDTO> invItems = new ArrayList<InvoiceItemDTO>();
		for (InvoiceItemDTO invoiceItemDTO : invoiceItems.getList()) {
			invItems.add(invoiceItemDTO);
		}
		inv.setItems(invItems);
		inv.setNote(note.getText());
		inv.setPaymentType(PaymentType.values()[payment.getSelectedIndex()-1]);
		if(payment.getSelectedIndex() > 0){
			inv.setPaymentDueDate(InvoiceUtils.calculatePaymentDueDate(inv.getAccountingDocumentDate(), inv.getPaymentType()));  
		} else {
			inv.setPaymentDueDate(null);
		}

		inv.setPaymentNote(paymentNote.getText());

		BigDecimal totBeforeTaxes = BigDecimal.ZERO;
		BigDecimal totTaxes = BigDecimal.ZERO;
		for (InvoiceItemDTO item : invoiceItems.getList()) {
			totBeforeTaxes = totBeforeTaxes.add(InvoiceUtils.calculateTotalBeforeTaxesForItem(item));
			totTaxes = totTaxes.add(InvoiceUtils.calculateTaxesForItem(item));
		}
		BigDecimal totAfterTaxes = totBeforeTaxes.add(totTaxes);

		inv.setTotalBeforeTax(totBeforeTaxes);
		inv.setTotalTax(totTaxes);
		inv.setTotal(totAfterTaxes);

		return inv;
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
		List<InvoiceItemDTO> invItems = new ArrayList<InvoiceItemDTO>();
		for (InvoiceItemDTO invoiceItemDTO : invoiceItems.getList()) {
			invItems.add(invoiceItemDTO);
		}
		es.setItems(invItems);
		es.setNote(note.getText());

		BigDecimal totBeforeTaxes = BigDecimal.ZERO;
		BigDecimal totTaxes = BigDecimal.ZERO;
		for (InvoiceItemDTO item : invoiceItems.getList()) {
			totBeforeTaxes = totBeforeTaxes.add(InvoiceUtils.calculateTotalBeforeTaxesForItem(item));
			totTaxes = totTaxes.add(InvoiceUtils.calculateTaxesForItem(item));
		}
		BigDecimal totAfterTaxes = totBeforeTaxes.add(totTaxes);

		es.setTotalBeforeTax(totBeforeTaxes);
		es.setTotalTax(totTaxes);
		es.setTotal(totAfterTaxes);

		return es;
	}


	@UiHandler("add")
	void onAddClicked(ClickEvent e){
		if(tableEntryBasicValidation()){
			InvoiceItemDTO ii = new InvoiceItemDTO();
			double tmpVal;

			try {
				ii.setDescription(item.getText());
				tmpVal = NumberFormat.getDecimalFormat().parse(price.getText());
				ii.setPrice(new BigDecimal( tmpVal ) );
				tmpVal = NumberFormat.getDecimalFormat().parse(quantity.getText());
				ii.setQuantity(new BigDecimal(tmpVal));
				ii.setUnitOfMeasure(unitOfMeasure.getText());
				ii.setTax(new BigDecimal(tax.getValue(tax.getSelectedIndex())));
			} catch (NumberFormatException ex) {
				return;
			}
			BigDecimal totBeforeTaxesForItem = InvoiceUtils.calculateTotalBeforeTaxesForItem(ii);
			BigDecimal totTaxesForItem = InvoiceUtils.calculateTaxesForItem(ii);
			ii.setTotal(totBeforeTaxesForItem.add(totTaxesForItem));
			ii.setTotalTax(totTaxesForItem);
			ii.setTotalBeforeTax(totBeforeTaxesForItem);

			invoiceItems.getList().add(ii);
			updateFields();
			itemTableScroller.scrollToBottom();
		}
	}

	private boolean tableEntryBasicValidation(){
		for (TextBox tbox : new TextBox[]{item,quantity,unitOfMeasure,price}) {
			if(tbox.getText().isEmpty()){
				return false;
			}
		}

		return true;
	}

	@UiHandler("modifyDocument")
	void onModifyInvoiceClicked(ClickEvent e){
		if(!validateEstimation()){
			return;
		}

		if(invoice != null){
			onModifyInvoice();
		} else {
			onModifyEstimation();
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

	private void onModifyInvoice(){
		if(!validateInvoice()){
			return;
		}

		if(Notification.showYesNoRequest(I18N.INSTANCE.saveModificationsConfirm()) ){
			final InvoiceDTO inv = createInvoice(invoice);

			ServerFacade.invoice.update(inv, new AuthAwareAsyncCallback<Void>() {

				@Override
				public void onException(Throwable caught) {
					Notification.showMessage(I18N.INSTANCE.invoiceUpdateFailure());
				}

				@Override
				public void onSuccess(Void result) {
					Notification.showMessage(I18N.INSTANCE.invoiceUpdateSuccess());

					DataWatcher.getInstance().fireInvoiceEvent();
					DataWatcher.getInstance().fireStatsEvent();

					ClientPlace cp = new ClientPlace();
					cp.setClientId(inv.getClient().getId());
					presenter.goTo(cp);
				}
			});

		} 




	}

	private void onModifyEstimation(){
		if(Notification.showYesNoRequest(I18N.INSTANCE.saveModificationsConfirm()) ){
			final EstimationDTO es = createEstimation(estimation);

			ServerFacade.estimation.update(es, new AuthAwareAsyncCallback<Void>() {

				@Override
				public void onException(Throwable caught) {
					Notification.showMessage(I18N.INSTANCE.estimationUpdateFailure());
				}

				@Override
				public void onSuccess(Void result) {
					Notification.showMessage(I18N.INSTANCE.estimationUpdateSuccess());

					DataWatcher.getInstance().fireEstimationEvent();

					ClientPlace cp = new ClientPlace();
					cp.setClientId(es.getClient().getId());
					presenter.goTo(cp);
				}
			});
		} 
	}




	@Override
	public void setInvoice(InvoiceDTO invoice) {
		this.invoice = invoice;
		this.client = invoice.getClient();
		invoiceItems.setList(invoice.getItems());
		if(invoice.getDocumentID() != null){
			number.setText(invoice.getDocumentID().toString());
		} 
		date.setValue(invoice.getAccountingDocumentDate());
		note.setText(invoice.getNote());
		paymentNote.setText(invoice.getPaymentNote());
		if(invoice.getPaymentType() != null) { //can be null if the invoice is derived from an estimation
			payment.setSelectedIndex(invoice.getPaymentType().ordinal()+1);
		}
		clientName.setText(invoice.getClient().getName());
		modifyDocument.setVisible(true);

		updateFields();
	}


	@Override
	public void setDataForNewInvoice(ClientDTO client, Long progressiveId) {
		this.client = client;

		clientName.setText(client.getName());
		date.setValue(new Date());
		number.setText(progressiveId.toString());

		createInvoice.setVisible(true);
		createEstimate.setVisible(true);
	}

	@Override
	public void setDataForNewEstimation(ClientDTO client) {
		this.client =client;

		clientName.setText(client.getName());
		date.setValue(new Date());

		createEstimate.setVisible(true);
		convertToInvoice.setVisible(true);

		number.setVisible(false);
		invoiceNumber.setVisible(false);
		paymentNoteLabel.setVisible(false);
		paymentNote.setVisible(false);
		paymentLabel.setVisible(false);
		payment.setVisible(false);

	}


	@Override
	public void setEstimation(EstimationDTO estimation) {
		this.estimation = estimation;
		this.client = estimation.getClient();

		invoiceItems.setList(estimation.getItems());
		date.setValue(estimation.getAccountingDocumentDate());
		note.setText(estimation.getNote());
		clientName.setText(estimation.getClient().getName());

		modifyDocument.setVisible(true);

		convertToInvoice.setVisible(true);

		number.setVisible(false);
		invoiceNumber.setVisible(false);
		paymentNoteLabel.setVisible(false);
		paymentNote.setVisible(false);
		paymentLabel.setVisible(false);
		payment.setVisible(false);
		updateFields();
	}


	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	private boolean validateInvoice(){
		if(!validateEstimation()){
			return false;
		}
		number.validate();
		payment.validate();
		if(!number.isValid() || !payment.isValid()){
			return false;
		}

		return true;
	}


	private boolean validateEstimation(){
		if(date.getTextBox().getText().isEmpty() || date.getValue() == null){
			return false;
		} else if(invoiceItems.getList().isEmpty()){
			return false;
		}
		return true;
	}


	private void updateFields(){
		BigDecimal totBeforeTaxes = BigDecimal.ZERO;
		BigDecimal totTaxes = BigDecimal.ZERO;
		for (InvoiceItemDTO item : invoiceItems.getList()) {
			totBeforeTaxes = totBeforeTaxes.add(InvoiceUtils.calculateTotalBeforeTaxesForItem(item));
			totTaxes = totTaxes.add(InvoiceUtils.calculateTaxesForItem(item));
		}
		BigDecimal totAfterTaxes = totBeforeTaxes.add(totTaxes);

		totalTax.setText(NumberFormat.getCurrencyFormat().format(totTaxes.doubleValue()));
		totalBeforeTaxes.setText(NumberFormat.getCurrencyFormat().format(totBeforeTaxes.doubleValue()));
		totalAfterTaxes.setText(NumberFormat.getCurrencyFormat().format(totAfterTaxes.doubleValue()));
		resetItemTableForm();
		invoiceItems.refresh();
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
		this.invoice = null;
		this.client = null;
		this.estimation = null;

		//reset widget statuses
		number.reset();
		number.setVisible(true);
		payment.setVisible(true);
		createEstimate.setVisible(false);
		createInvoice.setVisible(false);
		modifyDocument.setVisible(false);
		convertToInvoice.setVisible(false);
		paymentNote.setVisible(true);
		invoiceNumber.setVisible(true);
		paymentNoteLabel.setVisible(true);
		paymentLabel.setVisible(true);

		//reset widget contents		
		payment.setSelectedIndex(0);
		paymentNote.setText("");
		note.setText("");
		invoiceItems.getList().clear();
		invoiceItems.refresh();
		totalTax.setText("");
		totalBeforeTaxes.setText("");
		totalAfterTaxes.setText("");
		resetItemTableForm();
	}


}
