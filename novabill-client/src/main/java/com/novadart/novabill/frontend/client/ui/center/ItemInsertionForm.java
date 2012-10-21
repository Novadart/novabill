package com.novadart.novabill.frontend.client.ui.center;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.ui.widget.notification.Notification;
import com.novadart.novabill.frontend.client.util.CalcUtils;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;

public class ItemInsertionForm extends Composite {

	private static ItemListUiBinder uiBinder = GWT
			.create(ItemListUiBinder.class);

	interface ItemListUiBinder extends UiBinder<Widget, ItemInsertionForm> {
	}

	private ListDataProvider<AccountingDocumentItemDTO> accountingDocumentItems = 
			new ListDataProvider<AccountingDocumentItemDTO>();
	
	public static interface Handler {
		public void onItemAdded(List<AccountingDocumentItemDTO> items);
	}

	@UiField ScrollPanel itemTableScroller;
	@UiField TextArea item;
	@UiField TextBox quantity;
	@UiField TextBox unitOfMeasure;
	@UiField TextBox price;
	@UiField(provided=true) ListBox tax;
	@UiField(provided=true) ItemTable itemTable;

	private final Handler handler;
	
	public ItemInsertionForm(Handler handler) {
		this.handler = handler;
		
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
		
		initWidget(uiBinder.createAndBindUi(this));
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

	private void updateFields(){
		resetItemTableForm();
		accountingDocumentItems.refresh();
		handler.onItemAdded(getItems());
	}
	
	private void resetItemTableForm(){
		item.setText("");
		quantity.setText("");
		unitOfMeasure.setText("");
		price.setText("");
		tax.setSelectedIndex(0);
	}

	public void reset(){
		resetItemTableForm();
		accountingDocumentItems.getList().clear();
		accountingDocumentItems.refresh();
	}
	
	public List<AccountingDocumentItemDTO> getItems() {
		return accountingDocumentItems.getList();
	}
	
	public void setItems(List<AccountingDocumentItemDTO> items) {
		accountingDocumentItems.setList(items);
		updateFields();
	}
}
