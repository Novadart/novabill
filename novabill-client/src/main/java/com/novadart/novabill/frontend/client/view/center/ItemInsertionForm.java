package com.novadart.novabill.frontend.client.view.center;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.novadart.gwtshared.client.validation.TextLengthValidation;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextArea;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.i18n.I18NM;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.view.HasUILocking;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.tax.TaxWidget;
import com.novadart.novabill.frontend.client.widget.tip.TipFactory;
import com.novadart.novabill.frontend.client.widget.tip.Tips;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;

public class ItemInsertionForm extends Composite implements HasUILocking {

	private static ItemListUiBinder uiBinder = GWT
			.create(ItemListUiBinder.class);

	interface ItemListUiBinder extends UiBinder<Widget, ItemInsertionForm> {
	}

	private ListDataProvider<AccountingDocumentItemDTO> accountingDocumentItems = 
			new ListDataProvider<AccountingDocumentItemDTO>();

	public static interface Handler {
		public void onItemListUpdated(List<AccountingDocumentItemDTO> items);
	}

	@UiField ScrollPanel itemTableScroller;
	@UiField(provided=true) ValidatedTextArea item;
	@UiField TextBox quantity;
	@UiField TextBox unitOfMeasure;
	@UiField TextBox price;
	@UiField TaxWidget tax;
	@UiField(provided=true) ItemTable itemTable;
	@UiField SimplePanel tip;
	@UiField CheckBox textOnlyAccountingItem;

	@UiField VerticalPanel quantityContainer;
	@UiField VerticalPanel unitOfMeasureContainer;
	@UiField VerticalPanel priceContainer;
	@UiField VerticalPanel taxContainer;

	@UiField Button add;

	private final Handler handler;

	public ItemInsertionForm(Handler handler) {
		this.handler = handler;

		item = new ValidatedTextArea(GlobalBundle.INSTANCE.validatedWidget(), new TextLengthValidation(500) {

			@Override
			public String getErrorMessage() {
				return I18NM.get.textLengthError(500);
			}
		});

		itemTable = new ItemTable(new ItemTable.Handler() {

			@Override
			public void onDelete(AccountingDocumentItemDTO item) {
				accountingDocumentItems.getList().remove(item);
				updateFields();
			}

			@Override
			public void onUpdate(AccountingDocumentItemDTO item) {
				DocumentUtils.updateTotals(item);
				ItemInsertionForm.this.handler.onItemListUpdated(getItems());
			}

		});
		accountingDocumentItems.addDataDisplay(itemTable);

		initWidget(uiBinder.createAndBindUi(this));

		TipFactory.show(Tips.item_insertion_form, tip);
	}

	@UiHandler("add")
	void onAddClicked(ClickEvent e){
		String validationError = null;

		if(textOnlyAccountingItem.getValue()) {
			validationError = item.getText().isEmpty() 
					? I18NM.get.errorCheckField(I18N.INSTANCE.nameDescription())
							: null;
		} else {
			tax.validate();
			validationError = DocumentUtils.validateAccountingDocumentItem(item.getText(), price.getText(), 
					quantity.getText(), unitOfMeasure.getText(), tax.getValue());
		}

		if(validationError != null) {
			Notification.showMessage(validationError);
			return;
		}

		AccountingDocumentItemDTO ii = null;

		if(textOnlyAccountingItem.getValue()) { 
			ii = DocumentUtils.createAccountingDocumentItem(item.getText());
		} else {
			ii = DocumentUtils.createAccountingDocumentItem(item.getText(), price.getText(), 
					quantity.getText(), unitOfMeasure.getText(), tax.getValue());
		}

		accountingDocumentItems.getList().add(ii);
		updateFields();
		itemTableScroller.scrollToBottom();
	}

	@UiHandler("textOnlyAccountingItem")
	void onTextOnlyAccountingItemChange(ValueChangeEvent<Boolean> event){
		quantityContainer.setVisible(!event.getValue());
		unitOfMeasureContainer.setVisible(!event.getValue());
		priceContainer.setVisible(!event.getValue());
		taxContainer.setVisible(!event.getValue());
	}

	private void updateFields(){
		resetItemTableForm();
		accountingDocumentItems.refresh();
		handler.onItemListUpdated(getItems());
	}

	private void resetItemTableForm(){
		item.setText("");
		quantity.setText("");
		unitOfMeasure.setText("");
		price.setText("");
		tax.reset();
		
		textOnlyAccountingItem.setValue(false);
		quantityContainer.setVisible(true);
		unitOfMeasureContainer.setVisible(true);
		priceContainer.setVisible(true);
		taxContainer.setVisible(true);
		setLocked(false);
	}

	public void reset(){
		accountingDocumentItems.getList().clear();
		updateFields();
	}

	public List<AccountingDocumentItemDTO> getItems() {
		return accountingDocumentItems.getList();
	}

	public void setItems(List<AccountingDocumentItemDTO> items) {
		accountingDocumentItems.setList(items);
		updateFields();
	}

	@UiFactory
	I18N getI18N(){
		return I18N.INSTANCE;
	}

	@Override
	public void setLocked(boolean value) {
		item.setEnabled(!value);
		quantity.setEnabled(!value);
		unitOfMeasure.setEnabled(!value);
		price.setEnabled(!value);
		tax.setEnabled(!value);
		add.setEnabled(!value);
	}

}
