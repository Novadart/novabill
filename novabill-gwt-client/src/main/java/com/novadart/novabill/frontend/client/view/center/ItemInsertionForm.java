package com.novadart.novabill.frontend.client.view.center;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.resources.client.CssResource;
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
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.novadart.gwtshared.client.validation.TextLengthValidation;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextArea;
import com.novadart.novabill.frontend.client.SharedComparators;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.i18n.I18NM;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.view.HasUILocking;
import com.novadart.novabill.frontend.client.widget.notification.InlineNotification;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.tax.TaxWidget;
import com.novadart.novabill.frontend.client.widget.tip.TipFactory;
import com.novadart.novabill.frontend.client.widget.tip.Tips;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.PriceListDTO;
import com.novadart.novabill.shared.client.tuple.Pair;

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
	@UiField(provided=true) ValidatedTextArea item;
	@UiField TextBox quantity;
	@UiField TextBox unitOfMeasure;
	@UiField TextBox price;
	@UiField TaxWidget tax;
	@UiField TextBox discount;
	@UiField(provided=true) ItemTable itemTable;
	@UiField SimplePanel tip;
	@UiField CheckBox textOnlyAccountingItem;

	@UiField VerticalPanel quantityContainer;
	@UiField VerticalPanel unitOfMeasureContainer;
	@UiField VerticalPanel priceContainer;
	@UiField VerticalPanel taxContainer;
	@UiField VerticalPanel discountContainer;

	@UiField Button add;
	
	@UiField NotificationCss nf;

	private final Handler handler;
	private Long clientId;
	private List<CommodityDTO> commodities;
	private List<PriceListDTO> listOfPriceLists;
	private PriceListDTO priceList;
	
	private final CommoditySearchPanel commoditySearchPanel = new CommoditySearchPanel(this);
	private final SingleSelectionModel<CommodityDTO> model = new SingleSelectionModel<CommodityDTO>();
	
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
				if(!item.isDescriptionOnly()) { 
					DocumentUtils.updateTotals(item);
				}
				ItemInsertionForm.this.handler.onItemListUpdated(getItems());
			}

		});
		accountingDocumentItems.addDataDisplay(itemTable);

		initWidget(uiBinder.createAndBindUi(this));

		notification.addStyleName(nf.notification());
		TipFactory.show(Tips.item_insertion_form, tip);
		
		commoditySearchPanel.setSelectionModel(model);
		
		model.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				commoditySearchPanel.hide();
				CommodityDTO com = model.getSelectedObject();
				updateItemFromJS(com.getDescription(), com.getUnitOfMeasure(), 
						DocumentUtils.calculatePriceForCommodity(com, priceList.getName()).toString(), com.getTax().toString());
			}
		});
	}
	
	public void setClientId(Long clientId) {
		this.clientId = clientId;
		commoditySearchPanel.setClientId(clientId.toString());
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		
		setLocked(true);
		
		ServerFacade.INSTANCE.getBatchfetcherService().fetchSelectCommodityForDocItemOpData(clientId, 
				new ManagedAsyncCallback<Pair<PriceListDTO,List<PriceListDTO>>>() {

			@Override
			public void onSuccess(Pair<PriceListDTO, List<PriceListDTO>> result) {
				priceList = result.getFirst();
				commodities = result.getFirst().getCommodities();
				Collections.sort(commodities, SharedComparators.COMMODITY_COMPARATOR);
				listOfPriceLists = result.getSecond();
				setLocked(false);
			}
		});
	}
	
	private List<CommodityDTO> filterCommodities(String key){
		if(key.isEmpty()){
			return new ArrayList<CommodityDTO>();
		}
		String nKey = key.toLowerCase();
		
		List<CommodityDTO> result = new ArrayList<CommodityDTO>();
		for (CommodityDTO c : commodities) {
			if(c.getSku().toLowerCase().contains(nKey) || c.getDescription().toLowerCase().contains(nKey)){
				result.add(c);
			}
		}
		return result;
	}
	
	@UiHandler("item")
	void onKeyUp(KeyUpEvent event){
		if(event.getNativeKeyCode() == KeyCodes.KEY_UP && commoditySearchPanel.isShowing()){
			commoditySearchPanel.setFocus(true);
			return;
		}
		
		List<CommodityDTO> list = filterCommodities(item.getText());
		if(list.isEmpty()){
			commoditySearchPanel.hide();
			return;
		}
		
		commoditySearchPanel.setCommodities(list);
		commoditySearchPanel.positionOnTop(item);
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
					quantity.getText(), unitOfMeasure.getText(), tax.getValue(), discount.getText());
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
					quantity.getText(), unitOfMeasure.getText(), tax.getValue(), discount.getText());
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
		discountContainer.setVisible(!event.getValue());
	}
	
	private void updateItemFromJS(String description, String unitOfMeasure, String price, String vat){
		item.setText(description);
		this.unitOfMeasure.setText(unitOfMeasure);
		this.price.setText(price.replace('.', ','));
		tax.setValue(new BigDecimal(vat));
	}

	native void openSelectCommodityDialog(ItemInsertionForm insForm, String clientId)/*-{
		$wnd.GWT_Hook_nSelectCommodityDialog(clientId, {
			onOk : function(commodity, priceValue){
				insForm.@com.novadart.novabill.frontend.client.view.center.ItemInsertionForm::updateItemFromJS(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(
					commodity.description,
					commodity.unitOfMeasure,
					String(priceValue),
					String(commodity.tax)
				);
			},
			onCancel : function(){},
		});
	}-*/;
	
	
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
		discount.setText("");
		tax.reset();
		model.clear();
		
		textOnlyAccountingItem.setValue(false);
		quantityContainer.setVisible(true);
		unitOfMeasureContainer.setVisible(true);
		priceContainer.setVisible(true);
		taxContainer.setVisible(true);
		discountContainer.setVisible(true);
		setLocked(false);
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
		item.setEnabled(!value);
		quantity.setEnabled(!value);
		unitOfMeasure.setEnabled(!value);
		price.setEnabled(!value);
		discount.setEnabled(!value);
		tax.setEnabled(!value);
		add.setEnabled(!value);
	}

}
