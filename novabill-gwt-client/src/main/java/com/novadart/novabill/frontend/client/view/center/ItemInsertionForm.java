package com.novadart.novabill.frontend.client.view.center;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.util.CalcUtils;
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.view.HasUILocking;
import com.novadart.novabill.frontend.client.widget.notification.InlineNotification;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;

public class ItemInsertionForm extends Composite implements HasUILocking {

	interface NotificationCss extends CssResource {
		String notification();
	}

	private static ItemListUiBinder uiBinder = GWT
			.create(ItemListUiBinder.class);

	interface ItemListUiBinder extends UiBinder<Widget, ItemInsertionForm> {
	}

	private ListDataProvider<AccountingDocumentItemDTO> accountingDocumentItems = 
			new ListDataProvider<AccountingDocumentItemDTO>();

	public static interface Handler {
		public void onItemListUpdated(List<AccountingDocumentItemDTO> items);
	}

	@UiField InlineNotification notification;

	@UiField ScrollPanel itemTableScroller;
	@UiField HTMLPanel newItemContainer;
	@UiField(provided=true) ItemTable itemTable;

	@UiField NotificationCss nf;

	private final Handler handler;
	private boolean manageWeight;
	private Long clientId;

	public ItemInsertionForm(Handler handler) {
		this(false, false, handler);
	}

	public ItemInsertionForm(boolean manageWeight, boolean readonly, Handler handler) {
		this.handler = handler;
		this.manageWeight = manageWeight;

		itemTable = new ItemTable(manageWeight, readonly, new ItemTable.Handler() {

			@Override
			public void onDelete(AccountingDocumentItemDTO item) {
				accountingDocumentItems.getList().remove(item);
				updateFields();
			}

			@Override
			public void onUpdate(AccountingDocumentItemDTO item) {
				if(!item.isDescriptionOnly()) { 
					CalcUtils.updateTotals(item);
				}
				ItemInsertionForm.this.handler.onItemListUpdated(getItems());
			}

			@Override
			public void onMoveUp(AccountingDocumentItemDTO value) {
				int index = accountingDocumentItems.getList().indexOf(value);
				if(index > 0){
					AccountingDocumentItemDTO item = accountingDocumentItems.getList().remove(index);
					accountingDocumentItems.getList().add(index-1, item);
				}
			}

			@Override
			public void onMoveDown(AccountingDocumentItemDTO value) {
				int index = accountingDocumentItems.getList().indexOf(value);
				if(index >= 0 && index < accountingDocumentItems.getList().size()-1){
					AccountingDocumentItemDTO item = accountingDocumentItems.getList().remove(index);
					accountingDocumentItems.getList().add(index+1, item);
				}
			}

		});
		accountingDocumentItems.addDataDisplay(itemTable);

		initWidget(uiBinder.createAndBindUi(this));

		notification.addStyleName(nf.notification());
		newItemContainer.setVisible(!readonly);
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	@Override
	protected void onLoad() {
		super.onLoad();

		Element elm = newItemContainer.getElement().getElementsByTagName("n-item-form").getItem(0);
		elm.setAttribute("client-id", String.valueOf(this.clientId));
		elm.setAttribute("manage-weight", String.valueOf(this.manageWeight));
		elm.setAttribute("explicit-discount", String.valueOf( !Configuration.getBusiness().getSettings().isPriceDisplayInDocsMonolithic() ));
		
		initAngularItemForm();
		initAngularItemFormCallback(this);
	}

	private native void initAngularItemForm()/*-{
		$wnd.Angular_ItemFormInit();
	}-*/;
	
	
	private native void initAngularItemFormCallback(ItemInsertionForm insForm)/*-{
		$wnd.Angular_ItemFormInit_callback = function(textOnly, item){
			if(!textOnly){
				insForm.@com.novadart.novabill.frontend.client.view.center.ItemInsertionForm::addItem(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(
			
				);
			} else {
				insForm.@com.novadart.novabill.frontend.client.view.center.ItemInsertionForm::addItem(Ljava/lang/String;)(item.description);
			}
		}
	}-*/;
	
	private void addItem(String sku, String description, String price, String quantity, 
			String weight, String unitOfMeasure, String tax, String discount){
		AccountingDocumentItemDTO ii = DocumentUtils.createAccountingDocumentItem(sku, description, price, 
					quantity, this.manageWeight ? weight : null, unitOfMeasure, tax, discount);

		if(ii == null) {
			return;
		}

		accountingDocumentItems.getList().add(ii);
		updateFields();
		itemTableScroller.scrollToBottom();
	}
	
	private void addItem(String text){
		AccountingDocumentItemDTO ii = DocumentUtils.createAccountingDocumentItem( text );
		
		if(ii == null) {
			return;
		}
		
		accountingDocumentItems.getList().add(ii);
		updateFields();
		itemTableScroller.scrollToBottom();
	}

	private void updateFields(){
		accountingDocumentItems.refresh();
		handler.onItemListUpdated(getItems());
	}
	
	public void reset(){
		accountingDocumentItems.getList().clear();
		notification.hide();
		updateFields();
	}

	public boolean isValid(){
		if(getItems().isEmpty()){
			notification.showMessage(I18N.INSTANCE.errorAddAtLeastOneItem());
			return false;
		} else {
			notification.hide();
			return true;
		}
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
	}
	
}
