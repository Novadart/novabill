package com.novadart.novabill.frontend.client.view.center;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.novadart.gwtshared.client.validation.TextLengthValidation;
import com.novadart.gwtshared.client.validation.widget.ValidatedTextArea;
import com.novadart.novabill.frontend.client.Configuration;
import com.novadart.novabill.frontend.client.SharedComparators;
import com.novadart.novabill.frontend.client.facade.ManagedAsyncCallback;
import com.novadart.novabill.frontend.client.facade.ServerFacade;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.i18n.I18NM;
import com.novadart.novabill.frontend.client.resources.GlobalBundle;
import com.novadart.novabill.frontend.client.util.CalcUtils;
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.view.HasUILocking;
import com.novadart.novabill.frontend.client.widget.notification.InlineNotification;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.frontend.client.widget.tax.TaxWidget;
import com.novadart.novabill.shared.client.data.PriceType;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.PriceDTO;
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

	@UiField Label itemLabel;	
	
	@UiField ScrollPanel itemTableScroller;
	@UiField(provided=true) ValidatedTextArea sku;
	@UiField(provided=true) ValidatedTextArea item;
	@UiField TextBox quantity;
	@UiField TextBox unitOfMeasure;
	@UiField TextBox price;
	@UiField TextBox weight;
	@UiField TaxWidget tax;
	@UiField TextBox discount;
	@UiField(provided=true) ItemTable itemTable;
	@UiField CheckBox textOnlyAccountingItem;

	@UiField VerticalPanel skuContainer;
	@UiField VerticalPanel quantityContainer;
	@UiField VerticalPanel unitOfMeasureContainer;
	@UiField VerticalPanel priceContainer;
	@UiField VerticalPanel taxContainer;
	@UiField VerticalPanel discountContainer;
	@UiField VerticalPanel weightContainer;

	@UiField FlowPanel newItemContainer;

	@UiField Button add;
	@UiField Button browse;

	@UiField CheckBox overrideDiscountInDocsExplicit;

	@UiField NotificationCss nf;

	private final Handler handler;
	private Long clientId;
	private List<CommodityDTO> commodities;
	private PriceListDTO priceList;
	private boolean manageWeight;
	private boolean insertedFromCommoditySearchPanel = false;
	private boolean contentAssistEnabled = true;
	private boolean locked = false;

	private enum FILTER_TYPE {
		SKU, DESCRIPTION
	};

	private final CommoditySearchPanel commoditySearchPanel = 
			new CommoditySearchPanel(this, new CommoditySearchPanel.Handler() {

				@Override
				public void onCommodityClicked(CommodityDTO commodity) {
					commoditySearchPanel.hide();
					if(commodity != null){
						updateItemFromCommodity(commodity, priceList.getName());
					}
				}
			} );

	public ItemInsertionForm(Handler handler) {
		this(false, false, handler);
	}

	public ItemInsertionForm(boolean manageWeight, boolean readonly, Handler handler) {
		this.handler = handler;
		this.manageWeight = manageWeight;

		sku = new ValidatedTextArea(GlobalBundle.INSTANCE.validatedWidget(), new TextLengthValidation(50) {

			@Override
			public String getErrorMessage() {
				return I18NM.get.textLengthError(50);
			}
		});

		item = new ValidatedTextArea(GlobalBundle.INSTANCE.validatedWidget(), new TextLengthValidation(500) {

			@Override
			public String getErrorMessage() {
				return I18NM.get.textLengthError(500);
			}
		});

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
		commoditySearchPanel.setClientId(clientId.toString());
	}

	@Override
	protected void onLoad() {
		super.onLoad();

		weightContainer.setVisible(this.manageWeight);

		lockUI(true);

		ServerFacade.INSTANCE.getBatchfetcherService().fetchSelectCommodityForDocItemOpData(clientId, 
				new ManagedAsyncCallback<Pair<PriceListDTO,List<PriceListDTO>>>() {

			@Override
			public void onSuccess(Pair<PriceListDTO, List<PriceListDTO>> result) {
				priceList = result.getFirst();
				commodities = result.getFirst().getCommodities();
				Collections.sort(commodities, SharedComparators.COMMODITY_COMPARATOR);
				unlockIfNotLocked();

				sku.setFocus(true);
			}
		});

		overrideDiscountInDocsExplicit.setVisible(!Configuration.getBusiness().getSettings().isPriceDisplayInDocsMonolithic());
		overrideDiscountInDocsExplicit.setValue(!Configuration.getBusiness().getSettings().isPriceDisplayInDocsMonolithic());
	}

	private boolean discountMustBeExplicit(){
		return overrideDiscountInDocsExplicit.isVisible() && overrideDiscountInDocsExplicit.getValue();
	}

	@Override
	protected void onUnload() {
		super.onUnload();
		commoditySearchPanel.hide();
	}

	private String prevKey = "";
	private Stack<List<CommodityDTO>> searchStack = new Stack<List<CommodityDTO>>();
	private FILTER_TYPE prevFilterType = null;

	private List<CommodityDTO> filterCommodities(FILTER_TYPE filterType, String key, List<CommodityDTO> commodities){
		//assert: key is already lowercase

		List<CommodityDTO> result = new ArrayList<CommodityDTO>();

		switch (filterType) {
		case DESCRIPTION:
			for (CommodityDTO c : commodities) {
				if(c.getDescription().toLowerCase().startsWith(key)){
					result.add(c);
				}
			}
			break;

		default:
		case SKU:
			for (CommodityDTO c : commodities) {
				if(c.getSku().toLowerCase().startsWith(key)){
					result.add(c);
				}
			}
			break;
		}

		return result;
	}

	private List<CommodityDTO> filterCommodities(FILTER_TYPE filterType, String key){
		if(key.isEmpty()){
			return new ArrayList<CommodityDTO>();
		}
		String nKey = key.toLowerCase();
		if(!filterType.equals(prevFilterType)){
			searchStack.clear();
			prevFilterType = filterType;
			prevKey = "";
		}

		int diffSize = nKey.length() - prevKey.length();

		switch (diffSize) {
		case 0:
			if(!nKey.equals(prevKey)){
				searchStack.clear();
				searchStack.push(filterCommodities(filterType, nKey, commodities));
			}
			break;

		case 1:
			if(nKey.startsWith(prevKey)){
				List<CommodityDTO> prevCommodities = searchStack.isEmpty() ? commodities : searchStack.peek();
				searchStack.push( filterCommodities(filterType, nKey, prevCommodities) );
			} else {
				searchStack.clear();
				searchStack.push(filterCommodities(filterType, nKey, commodities));
			}
			break;

		case -1:
			if(prevKey.startsWith(nKey)){
				searchStack.pop();
				if(searchStack.isEmpty()){
					searchStack.push(filterCommodities(filterType, nKey, commodities));
				}
			} else {
				searchStack.clear();
				searchStack.push(filterCommodities(filterType, nKey, commodities));
			}
			break;

		default:
			searchStack.clear();
			searchStack.push(filterCommodities(filterType, nKey, commodities));
			break;
		}

		prevKey = nKey;
		return searchStack.peek();
	}

	private void preloadValues(FILTER_TYPE filterType, String text, CommodityDTO commodity){
		if(!sku.getText().equals(commodity.getSku()) || !item.getText().equals(commodity.getDescription())) {
			updateItemFromCommodity(commodity, priceList.getName());
		}


		switch (filterType) {
		case DESCRIPTION:
			if(commodity.getDescription().length() > text.length()){
				item.setSelectionRange(text.length(), commodity.getDescription().length()-text.length());
			}
			break;

		default:
		case SKU:
			if(commodity.getSku().length() > text.length()){
				sku.setSelectionRange(text.length(), commodity.getSku().length()-text.length());
			}
			break;
		}
	}


	private void resetForm(FILTER_TYPE filterType){
		switch (filterType) {
		case DESCRIPTION:
			sku.setText("");
			tax.reset();
			quantity.setText("");
			weight.setText("");
			discount.setText("");
			price.setText("");
			unitOfMeasure.setText("");
			break;

		default:
		case SKU:
			item.setText("");
			tax.reset();
			quantity.setText("");
			weight.setText("");
			discount.setText("");
			price.setText("");
			unitOfMeasure.setText("");
			break;
		}
	}



	private void handleKeyUpEvent(FILTER_TYPE filterType, KeyUpEvent event){
		String text = "";
		int selectionLength = 0;
		
		switch (filterType) {
		case DESCRIPTION:
			selectionLength = item.getSelectionLength();
			text = item.getText().substring(0, item.getText().length() - selectionLength);
			break;

		default:
			selectionLength = sku.getSelectionLength();
			text = sku.getText().substring(0, sku.getText().length() - selectionLength);
			break;
		}

		switch (event.getNativeKeyCode()) {
		case KeyCodes.KEY_SHIFT:
		case KeyCodes.KEY_ALT:
		case KeyCodes.KEY_CTRL:
		case KeyCodes.KEY_UP:
		case KeyCodes.KEY_RIGHT:
		case KeyCodes.KEY_PAGEUP:
		case KeyCodes.KEY_PAGEDOWN:
		case KeyCodes.KEY_LEFT:
		case KeyCodes.KEY_HOME:
		case KeyCodes.KEY_END:
		case KeyCodes.KEY_DOWN:
		case KeyCodes.KEY_DELETE:
			break;

		case KeyCodes.KEY_ESCAPE:
			commoditySearchPanel.hide();
			break;

		case KeyCodes.KEY_TAB:
		case KeyCodes.KEY_ENTER:

			commoditySearchPanel.hide();
			if(!insertedFromCommoditySearchPanel && !text.isEmpty()){
				List<CommodityDTO> commodities = filterCommodities(filterType, text);
				if(!commodities.isEmpty()){
					CommodityDTO commodity = commodities.get(0);
					updateItemFromCommodity(commodity, priceList.getName());
				}
			} 
			break;


		case KeyCodes.KEY_BACKSPACE:
			insertedFromCommoditySearchPanel = false;
			commoditySearchPanel.hide();
			resetForm(filterType);
			break;

		default:
			insertedFromCommoditySearchPanel = false;

			List<CommodityDTO> list = filterCommodities(filterType, text);
			if(list.isEmpty()){
				commoditySearchPanel.hide();
				resetForm(filterType);
				return;
			}

			commoditySearchPanel.setCommodities(list);
			commoditySearchPanel.positionOnTop(FILTER_TYPE.DESCRIPTION.equals(filterType) ? item : sku);
			if(!list.isEmpty()) {
				preloadValues(filterType, text, list.get(0));
			}
			break;
		}
	}

	@UiHandler("sku")
	void onSkuKeyUp(KeyUpEvent event){
		if(contentAssistEnabled){
			handleKeyUpEvent(FILTER_TYPE.SKU, event);
		}
	}

	@UiHandler("item")
	void onItemKeyUp(KeyUpEvent event){
		if(contentAssistEnabled){
			handleKeyUpEvent(FILTER_TYPE.DESCRIPTION, event);
		}
	}

	@UiHandler("sku")
	void onSkuFocus(FocusEvent event){
		commoditySearchPanel.hide();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				sku.selectAll();
			}
		});
	}

	@UiHandler("item")
	void onItemFocus(FocusEvent event){
		commoditySearchPanel.hide();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				item.selectAll();
			}
		});

	}

	@UiHandler("quantity")
	void onQuantityFocus(FocusEvent event){
		commoditySearchPanel.hide();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				quantity.selectAll();
			}
		});
	}

	@UiHandler("weight")
	void onWeightFocus(FocusEvent event){
		commoditySearchPanel.hide();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				weight.selectAll();
			}
		});
	}

	@UiHandler("unitOfMeasure")
	void onUnitOfMeasureFocus(FocusEvent event){
		commoditySearchPanel.hide();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				unitOfMeasure.selectAll();
			}
		});
	}

	@UiHandler("price")
	void onPriceFocus(FocusEvent event){
		commoditySearchPanel.hide();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				price.selectAll();
			}
		});
	}

	@UiHandler("discount")
	void onDiscountFocus(FocusEvent event){
		commoditySearchPanel.hide();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				discount.selectAll();
			}
		});
	}

	@UiHandler("add")
	void onAddClicked(ClickEvent e){
		String validationError = null;
		commoditySearchPanel.hide();

		if(textOnlyAccountingItem.getValue()) {
			validationError = item.getText().isEmpty() 
					? I18NM.get.errorCheckField(I18N.INSTANCE.nameDescription())
							: null;
		} else {
			tax.validate();
			validationError = DocumentUtils.validateAccountingDocumentItem(item.getText(), price.getText(), 
					quantity.getText(), this.manageWeight ? weight.getText() : null, unitOfMeasure.getText(), tax.getValue(), discount.getText());
		}

		if(validationError != null) {
			Notification.showMessage(validationError);
			return;
		}

		AccountingDocumentItemDTO ii = null;

		if(textOnlyAccountingItem.getValue()) { 
			ii = DocumentUtils.createAccountingDocumentItem(item.getText());
		} else {
			ii = DocumentUtils.createAccountingDocumentItem(sku.getText(), item.getText(), price.getText(), 
					quantity.getText(), this.manageWeight ? weight.getText() : null, unitOfMeasure.getText(), tax.getValue(), discount.getText());
		}

		if(ii == null) {
			return;
		}

		accountingDocumentItems.getList().add(ii);
		updateFields();
		itemTableScroller.scrollToBottom();
		sku.setFocus(true);
	}

	@UiHandler("browse")
	void onBrowseClicked(ClickEvent e){
		commoditySearchPanel.hide();
		openSelectCommodityDialog(this, String.valueOf(this.clientId));
	}

	@UiHandler("textOnlyAccountingItem")
	void onTextOnlyAccountingItemChange(ValueChangeEvent<Boolean> event){
		quantityContainer.setVisible(!event.getValue());
		unitOfMeasureContainer.setVisible(!event.getValue());
		priceContainer.setVisible(!event.getValue());
		taxContainer.setVisible(!event.getValue());
		discountContainer.setVisible(!event.getValue());
		skuContainer.setVisible(!event.getValue());
		weightContainer.setVisible(this.manageWeight && !event.getValue());
		browse.setVisible(!event.getValue());
		contentAssistEnabled = !event.getValue();
		itemLabel.setText(event.getValue() ? I18N.INSTANCE.text() : I18N.INSTANCE.nameDescription());
	}

	private void updateItemFromJS(String skuVal, String description, String unitOfMeasure, String weight, String vat, 
			String defaultPrice, String priceListName, String priceType, String price){
		CommodityDTO commodity = new CommodityDTO();
		commodity.setSku(skuVal);
		commodity.setDescription(description);
		commodity.setUnitOfMeasure(unitOfMeasure);
		commodity.setWeight(weight == null ? null : new BigDecimal(weight));
		commodity.setTax(new BigDecimal(vat));

		Map<String, PriceDTO> prices = new HashMap<String, PriceDTO>();

		PriceDTO p = new PriceDTO();
		p.setId(-1L);
		p.setPriceType(priceType!=null ? PriceType.valueOf(priceType) : null);
		p.setPriceValue(price != null ? new BigDecimal(price) : null);
		prices.put(priceListName, p);

		p = new PriceDTO();
		p.setPriceType(PriceType.FIXED);
		p.setPriceValue(new BigDecimal(defaultPrice));
		prices.put("::default", p);

		commodity.setPrices(prices);

		updateItemFromCommodity(commodity, priceListName);
	}

	private void updateItemFromCommodity(CommodityDTO commodity, String priceListName){
		sku.setText(commodity.getSku().startsWith("::") ? "" : commodity.getSku());
		item.setText(commodity.getDescription());
		unitOfMeasure.setText(commodity.getUnitOfMeasure());
		tax.setValue(commodity.getTax());
		if(this.manageWeight){
			weight.setText(commodity.getWeight() != null ? NumberFormat.getDecimalFormat().format(commodity.getWeight()) : "");
		}

		PriceType priceType = commodity.getPrices().get(priceListName).getPriceType();

		if(priceType != null && PriceType.DISCOUNT_PERCENT.equals(priceType) && discountMustBeExplicit()) {

			PriceDTO defaultPrice = commodity.getPrices().get("::default");
			PriceDTO pr = commodity.getPrices().get(priceListName);

			price.setText(defaultPrice.getPriceValue().toString().replace('.', ','));
			discount.setText(pr.getPriceValue().toString().replace('.', ','));

		} else {
			price.setText(CalcUtils.calculatePriceForCommodity(commodity, priceListName).toString().replace('.', ','));
			discount.setText("");
		}

		insertedFromCommoditySearchPanel = true;
	}

	native void openSelectCommodityDialog(ItemInsertionForm insForm, String clientId)/*-{
		var instance = $wnd.GWT_Hook_nSelectCommodityDialog(clientId);
		instance.result.then(
			function(result){
					insForm.@com.novadart.novabill.frontend.client.view.center.ItemInsertionForm::updateItemFromJS(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(
						result.commodity.sku,
						result.commodity.description,
						result.commodity.unitOfMeasure,
						result.commodity.weight != null ? String(result.commodity.weight) : null,
						String(result.commodity.tax),
						String(result.defaultPriceValue),
						result.priceListName,
						result.priceType,
						result.priceValue != null ? String(result.priceValue) : null
					);
			},
			function(error){}
			);
	}-*/;


	private void updateFields(){
		resetItemTableForm();
		accountingDocumentItems.refresh();
		handler.onItemListUpdated(getItems());
	}

	private void resetItemTableForm(){
		sku.setText("");
		item.setText("");
		quantity.setText("");
		weight.setText("");
		unitOfMeasure.setText("");
		price.setText("");
		discount.setText("");
		tax.reset();
		prevKey = "";
		searchStack.clear();
		prevFilterType = null;
		
		itemLabel.setText(I18N.INSTANCE.nameDescription());

		contentAssistEnabled = true;
		browse.setVisible(true);
		skuContainer.setVisible(true);
		textOnlyAccountingItem.setValue(false);
		quantityContainer.setVisible(true);
		unitOfMeasureContainer.setVisible(true);
		priceContainer.setVisible(true);
		taxContainer.setVisible(true);
		discountContainer.setVisible(true);
		weightContainer.setVisible(this.manageWeight);
		setLocked(false);
		insertedFromCommoditySearchPanel = false;
	}

	public void reset(){
		accountingDocumentItems.getList().clear();
		notification.hide();
		overrideDiscountInDocsExplicit.setVisible(false);
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
		locked = value;
		lockUI(value);
	}
	
	private void lockUI(boolean value){
		sku.setEnabled(!value);
		browse.setEnabled(!value);
		item.setEnabled(!value);
		quantity.setEnabled(!value);
		weight.setEnabled(!value);
		unitOfMeasure.setEnabled(!value);
		price.setEnabled(!value);
		discount.setEnabled(!value);
		tax.setEnabled(!value);
		add.setEnabled(!value);
		overrideDiscountInDocsExplicit.setEnabled(!value);
	}
	
	
	private void unlockIfNotLocked(){
		if(!locked) {
			setLocked(false);
		}
	}

}
