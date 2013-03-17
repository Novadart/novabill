package com.novadart.novabill.frontend.client.view.center;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
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
	@UiField(provided=true) ListBox tax;
	@UiField(provided=true) ItemTable itemTable;

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
		
		tax = new ListBox();
		for (String item : I18N.INSTANCE.vatItems()) {
			tax.addItem(item+"%", item);
		}
		
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
	}

	@UiHandler("add")
	void onAddClicked(ClickEvent e){
		AccountingDocumentItemDTO ii = DocumentUtils.createAccountingDocumentItem(item.getText(), price.getText(), 
				quantity.getText(), unitOfMeasure.getText(), tax.getValue(tax.getSelectedIndex()));

		if(ii == null) {
			Notification.showMessage(I18N.INSTANCE.errorDocumentData());
			return;
		}

		accountingDocumentItems.getList().add(ii);
		updateFields();
		itemTableScroller.scrollToBottom();
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
		tax.setSelectedIndex(0);
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
