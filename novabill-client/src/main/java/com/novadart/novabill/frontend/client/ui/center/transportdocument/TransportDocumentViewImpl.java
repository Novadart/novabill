package com.novadart.novabill.frontend.client.ui.center.transportdocument;

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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;
import com.novadart.gwtshared.client.textbox.RichTextBox;
import com.novadart.gwtshared.client.validation.ValidationBundle;
import com.novadart.gwtshared.client.validation.widget.ValidatedListBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextBox;
import com.novadart.gwtshared.client.validation.widget.ValidatedWidget;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.datawatcher.DataWatcher;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.facade.WrappedAsyncCallback;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.place.ClientPlace;
import com.novadart.novabill.frontend.client.place.ClientPlace.DOCUMENTS;
import com.novadart.novabill.frontend.client.ui.center.AccountDocument;
import com.novadart.novabill.frontend.client.ui.center.TransportDocumentView;
import com.novadart.novabill.frontend.client.ui.widget.notification.Notification;
import com.novadart.novabill.frontend.client.ui.widget.table.ItemTable;
import com.novadart.novabill.frontend.client.ui.widget.validation.NumberValidation;
import com.novadart.novabill.frontend.client.util.CalcUtils;
import com.novadart.novabill.shared.client.data.Province;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.EndpointDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.validation.ErrorObject;

public class TransportDocumentViewImpl extends AccountDocument implements TransportDocumentView {

	private static TransportDocumentViewImplUiBinder uiBinder = GWT
			.create(TransportDocumentViewImplUiBinder.class);

	interface TransportDocumentViewImplUiBinder extends UiBinder<Widget, TransportDocumentViewImpl> {
	}
	
	@UiField HorizontalPanel docControls;
	@UiField ScrollPanel docScroll;

	@UiField(provided=true) RichTextBox fromAddrCompanyName;
	@UiField(provided=true) RichTextBox fromAddrStreetName;
	@UiField(provided=true) RichTextBox fromAddrPostCode;
	@UiField(provided=true) RichTextBox fromAddrCity;
	@UiField(provided=true) ValidatedListBox fromAddrProvince;
	
	@UiField(provided=true) RichTextBox toAddrCompanyName;
	@UiField(provided=true) RichTextBox toAddrStreetName;
	@UiField(provided=true) RichTextBox toAddrPostCode;
	@UiField(provided=true) RichTextBox toAddrCity;
	@UiField(provided=true) ValidatedListBox toAddrProvince;
	
	@UiField(provided=true) ValidatedTextBox numberOfPackages;
	@UiField(provided=true) ValidatedTextBox transporter;
	
	@UiField(provided=true) DateBox transportStartDate;
	@UiField(provided=true) ValidatedListBox hour;
	@UiField(provided=true) ValidatedListBox minute;
	
	@UiField(provided=true) ListBox tax;
	@UiField(provided=true) ItemTable itemTable;
	@UiField ScrollPanel itemTableScroller;

	@UiField TextBox item;
	@UiField TextBox quantity;
	@UiField TextBox unitOfMeasure;
	@UiField TextBox price;
	
	@UiField TextBox transportationResponsibility;
	@UiField TextBox tradeZone;
	
	@UiField Label clientName;
	@UiField(provided=true) DateBox date;
	@UiField TextArea note;
	@UiField Button createTransportDocument;
	@UiField Button modifyDocument;

	@UiField Label totalBeforeTaxes;
	@UiField Label totalTax;
	@UiField Label totalAfterTaxes;


	private Presenter presenter;
	private TransportDocumentDTO transportDocument;
	private ListDataProvider<AccountingDocumentItemDTO> accountingDocumentItems = new ListDataProvider<AccountingDocumentItemDTO>();
	private ClientDTO client;

	public TransportDocumentViewImpl() {
		ValidationBundle<String> nev = new ValidationBundle<String>() {
			
			@Override
			public boolean isValid(String value) { 	return !value.isEmpty(); }
			
			@Override
			public String getErrorMessage() {	return null; }
		};
		
		numberOfPackages = new ValidatedTextBox(new NumberValidation());
		transporter = new ValidatedTextBox(nev);
		
		fromAddrCity = new RichTextBox(I18N.INSTANCE.city(), nev);
		fromAddrCompanyName = new RichTextBox(I18N.INSTANCE.companyName(),nev);
		fromAddrPostCode = new RichTextBox(I18N.INSTANCE.postcode(), nev);
		fromAddrStreetName = new RichTextBox(I18N.INSTANCE.address(),nev);
		fromAddrProvince = new ValidatedListBox();
		for (Province p : Province.values()) {
			fromAddrProvince.addItem(p.name());
		}
		
		toAddrCity = new RichTextBox(I18N.INSTANCE.city(),nev);
		toAddrCompanyName = new RichTextBox(I18N.INSTANCE.companyName(), nev);
		toAddrPostCode = new RichTextBox(I18N.INSTANCE.postcode(),nev);
		toAddrStreetName = new RichTextBox(I18N.INSTANCE.address(),nev);
		toAddrProvince = new ValidatedListBox();
		for (Province p : Province.values()) {
			toAddrProvince.addItem(p.name());
		}
		
		String str;
		hour = new ValidatedListBox();
		for(int i=0; i<24; i++){
			str = String.valueOf(i);
			hour.addItem(str.length() < 2 ? "0"+str : str);
		}
		
		minute = new ValidatedListBox();
		for(int i=0; i<60; i++){
			str = String.valueOf(i);
			minute.addItem(str.length() < 2 ? "0"+str : str);
		}
		
		date = new DateBox();
		date.setFormat(new DateBox.DefaultFormat
				(DateTimeFormat.getFormat("dd MMMM yyyy")));
		
		tax = new ListBox();
		for (String item : I18N.INSTANCE.vatItems()) {
			tax.addItem(item+"%", item);
		}
		itemTable = new ItemTable(new ItemTable.Handler() {

			@Override
			public void delete(AccountingDocumentItemDTO item) {
				accountingDocumentItems.getList().remove(item);
				accountingDocumentItems.refresh();
				updateFields();
			}
		});
		accountingDocumentItems.addDataDisplay(itemTable);

		transportStartDate = new DateBox();
		transportStartDate.setFormat(new DateBox.DefaultFormat
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
	
	@UiHandler("fromAddrButtonDefault")
	void onFromAddressButtonDefaultCLicked(ClickEvent e){
		BusinessDTO b = Configuration.getBusiness();
		fromAddrCity.setText(b.getCity());
		fromAddrCompanyName.setText(b.getName());
		fromAddrPostCode.setText(b.getPostcode());
		fromAddrProvince.setSelectedItem(b.getProvince());
		fromAddrStreetName.setText(b.getAddress());
	}

	@UiHandler("toAddrButtonDefault")
	void onToAddressButtonDefaultCLicked(ClickEvent e){
		toAddrCity.setText(client.getCity());
		toAddrCompanyName.setText(client.getName());
		toAddrPostCode.setText(client.getPostcode());
		toAddrProvince.setSelectedItem(client.getProvince());
		toAddrStreetName.setText(client.getAddress());
	}
	

	@UiHandler("createTransportDocument")
	void onCreateTransportDocumentClicked(ClickEvent e){
		if(!validateTransportDocument()){
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}

		TransportDocumentDTO transportDocument = createtransportDocument(null);

		ServerFacade.transportDocument.add(transportDocument, new WrappedAsyncCallback<Long>() {

			@Override
			public void onSuccess(Long result) {
				Notification.showMessage(I18N.INSTANCE.transportDocumentCreationSuccess());

				DataWatcher.getInstance().fireTransportDocumentEvent();

				ClientPlace cp = new ClientPlace();
				cp.setClientId(client.getId());
				cp.setDocumentsListing(DOCUMENTS.transportDocuments);
				presenter.goTo(cp);
			}

			@Override
			public void onException(Throwable caught) {
				if(caught instanceof ValidationException){
					handleServerValidationException((ValidationException) caught, false);
				} else {
					Notification.showMessage(I18N.INSTANCE.transportDocumentCreationFailure());
				}
			}
		});
	}



	private TransportDocumentDTO createtransportDocument(TransportDocumentDTO transportDocument){
		TransportDocumentDTO td;

		if(transportDocument != null){
			td = transportDocument;
		} else {
			td = new TransportDocumentDTO();
			td.setBusiness(Configuration.getBusiness());
			td.setClient(client);
		}
		
		EndpointDTO loc = new EndpointDTO();
		loc.setCompanyName(fromAddrCompanyName.getText());
		loc.setCity(fromAddrCity.getText());
		loc.setPostcode(fromAddrPostCode.getText());
		loc.setProvince(fromAddrProvince.getSelectedItemText());
		loc.setStreet(fromAddrStreetName.getText());
		td.setFromEndpoint(loc);
		
		loc = new EndpointDTO();
		loc.setCompanyName(toAddrCompanyName.getText());
		loc.setCity(toAddrCity.getText());
		loc.setPostcode(toAddrPostCode.getText());
		loc.setProvince(toAddrProvince.getSelectedItemText());
		loc.setStreet(toAddrStreetName.getText());
		td.setToEndpoint(loc);
		
		Date tsd = transportStartDate.getValue();
		String dateTime = DateTimeFormat.getFormat("dd MMMM yyyy").format(tsd);
		dateTime += " "+hour.getSelectedItemText()+":"+minute.getSelectedItemText();
		tsd = DateTimeFormat.getFormat("dd MMMM yyyy HH:mm").parse(dateTime);		
		td.setTransportStartDate(tsd);
		
		td.setNumberOfPackages(Integer.valueOf(numberOfPackages.getText()));
		td.setTradeZone(tradeZone.getText());
		td.setTransportationResponsibility(transportationResponsibility.getText());
		td.setTransporter(transporter.getText());
		
		td.setAccountingDocumentDate(date.getValue());
		List<AccountingDocumentItemDTO> invItems = new ArrayList<AccountingDocumentItemDTO>();
		for (AccountingDocumentItemDTO itemDTO : accountingDocumentItems.getList()) {
			invItems.add(itemDTO);
		}
		td.setItems(invItems);
		td.setNote(note.getText());
		CalcUtils.calculateTotals(invItems, td);
		return td;
	}


	@UiHandler("add")
	void onAddClicked(ClickEvent e){
		AccountingDocumentItemDTO ii = CalcUtils.createAccountingDocumentItem(item.getText(), price.getText(), 
				quantity.getText(), unitOfMeasure.getText(), tax.getValue(tax.getSelectedIndex()));
		
		if(ii == null) {
			return;
		}
		
		accountingDocumentItems.getList().add(ii);
		updateFields();
		itemTableScroller.scrollToBottom();
	}

	@UiHandler("modifyDocument")
	void onModifyTransportDocumentClicked(ClickEvent e){
		if(!validateTransportDocument()){
			return;
		}

		if(Notification.showYesNoRequest(I18N.INSTANCE.saveModificationsConfirm()) ){
			final TransportDocumentDTO td = createtransportDocument(transportDocument);

			ServerFacade.transportDocument.update(td, new WrappedAsyncCallback<Void>() {

				@Override
				public void onException(Throwable caught) {
					if(caught instanceof ValidationException){
						handleServerValidationException((ValidationException) caught, false);
					} else {
						Notification.showMessage(I18N.INSTANCE.transportDocumentUpdateFailure());
					}
				}

				@Override
				public void onSuccess(Void result) {
					Notification.showMessage(I18N.INSTANCE.transportDocumentUpdateSuccess());

					DataWatcher.getInstance().fireTransportDocumentEvent();

					ClientPlace cp = new ClientPlace();
					cp.setClientId(td.getClient().getId());
					cp.setDocumentsListing(DOCUMENTS.transportDocuments);
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
	public void setDataForNewTransportDocument(ClientDTO client) {
		this.client =client;

		clientName.setText(client.getName());
		Date d = new Date();
		String hourStr = DateTimeFormat.getFormat("HH").format(d);
		String minuteStr = DateTimeFormat.getFormat("mm").format(d);
		date.setValue(d);
		transportStartDate.setValue(d);
		hour.setSelectedItem(hourStr);
		minute.setSelectedItem(minuteStr);

		createTransportDocument.setVisible(true);
	}
	
	
	
	@Override
	public void setDataForNewTransportDocument(ClientDTO client,
			TransportDocumentDTO document) {
		setDataForNewTransportDocument(client);
		setTransportDocument(document, true);
	}

	private void setTransportDocument(TransportDocumentDTO transportDocument, boolean cloning) {
		if(!cloning){
			this.transportDocument = transportDocument;
			this.client = transportDocument.getClient();
			date.setValue(transportDocument.getAccountingDocumentDate());
			clientName.setText(transportDocument.getClient().getName());
			
			Date d = transportDocument.getTransportStartDate();
			transportStartDate.setValue(d);
			String hourStr = DateTimeFormat.getFormat("HH").format(d);
			String minuteStr = DateTimeFormat.getFormat("mm").format(d);
			hour.setSelectedItem(hourStr);
			minute.setSelectedItem(minuteStr);
			modifyDocument.setVisible(true);
		}

		List<AccountingDocumentItemDTO> items = null;
		if(cloning) {
			items = new ArrayList<AccountingDocumentItemDTO>(transportDocument.getItems().size());
			for (AccountingDocumentItemDTO i : transportDocument.getItems()) {
				items.add(i.clone());
			}
		} else {
			items = transportDocument.getItems();
		}

		accountingDocumentItems.setList(items);
		note.setText(transportDocument.getNote());
		
		numberOfPackages.setText(String.valueOf(transportDocument.getNumberOfPackages()));
		
		EndpointDTO loc = transportDocument.getFromEndpoint();
		fromAddrCity.setText(loc.getCity());
		fromAddrCompanyName.setText(loc.getCompanyName());
		fromAddrPostCode.setText(loc.getPostcode());
		fromAddrProvince.setSelectedItem(loc.getProvince());
		fromAddrStreetName.setText(loc.getStreet());
		
		loc = transportDocument.getToEndpoint();
		toAddrCity.setText(loc.getCity());
		toAddrCompanyName.setText(loc.getCompanyName());
		toAddrPostCode.setText(loc.getPostcode());
		toAddrProvince.setSelectedItem(loc.getProvince());
		toAddrStreetName.setText(loc.getStreet());
		updateFields();
	}


	@Override
	public void setTransportDocument(TransportDocumentDTO document) {
		setTransportDocument(document, false);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}


	private boolean validateTransportDocument(){
		if(date.getTextBox().getText().isEmpty() || date.getValue() == null 
				|| transportStartDate.getTextBox().getText().isEmpty() || transportStartDate.getValue() == null){
			return false;
		} else if(accountingDocumentItems.getList().isEmpty()){
			return false;
		} else {
			boolean validation = true;
			for (ValidatedWidget<?, ?> vw : new ValidatedWidget<?, ?>[]{fromAddrCity, fromAddrCompanyName, fromAddrPostCode,
					fromAddrProvince, fromAddrStreetName, toAddrCity, toAddrCompanyName, toAddrPostCode, toAddrProvince, toAddrStreetName,
					numberOfPackages, hour, minute}) {
				vw.validate();
				validation = validation && vw.isValid();
			}
			if(!validation){
				return false;
			}
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
		this.transportDocument = null;

		//reset widget statuses
		createTransportDocument.setVisible(false);
		modifyDocument.setVisible(false);

		//reset widget contents		
		note.setText("");
		numberOfPackages.setText("");
		transporter.setText("");
		transportationResponsibility.setText("");
		tradeZone.setText("");
		hour.reset();
		minute.reset();
		numberOfPackages.reset();
		
		fromAddrCity.reset();
		fromAddrCompanyName.reset();
		fromAddrPostCode.reset();
		fromAddrStreetName.reset();
		fromAddrProvince.reset();
		
		toAddrCity.reset();
		toAddrCompanyName.reset();
		toAddrPostCode.reset();
		toAddrStreetName.reset();
		toAddrProvince.reset();
		
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
