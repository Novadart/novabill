package com.novadart.novabill.frontend.client.view.center;

import java.math.BigDecimal;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.novadart.gwtshared.client.cell.LargeEditTextCell;
import com.novadart.gwtshared.client.validation.Validation;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.i18n.I18NM;
import com.novadart.novabill.frontend.client.util.CalcUtils;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;

public class ItemTable extends CellTable<AccountingDocumentItemDTO> {

	private static final int DESCRIPTION_MAX_LENGTH = 500;
	private static final int UNIT_OF_MEASURE_MAX_SIZE = 255;


	public static interface Handler{
		public void onDelete(AccountingDocumentItemDTO item);
		public void onUpdate(AccountingDocumentItemDTO item);
		public void onMoveUp(AccountingDocumentItemDTO value);
		public void onMoveDown(AccountingDocumentItemDTO value);
	}

	private final Handler handler;
	private boolean locked = false;

	private final MoveUpDownCell moveUpDownCell;

	public ItemTable(boolean displayWeight, Handler handler) {
		super(10000);

		this.handler = handler;


		moveUpDownCell = new MoveUpDownCell(this.handler);
		final Column<AccountingDocumentItemDTO, AccountingDocumentItemDTO> moveUpDown = 
				new Column<AccountingDocumentItemDTO, AccountingDocumentItemDTO>(moveUpDownCell) {

			@Override
			public AccountingDocumentItemDTO getValue(AccountingDocumentItemDTO object) {
				return object;
			}
		};
		addColumn(moveUpDown);


		//Sku
		final TextCell skuCell = new TextCell();
		final Column<AccountingDocumentItemDTO, String> sku = 
				new Column<AccountingDocumentItemDTO, String>(skuCell) {

			@Override
			public String getValue(AccountingDocumentItemDTO object) {
				String sku = object.getSku();
				return sku==null||sku.startsWith("::") ? "" : sku;
			}

		};
		addColumn(sku, I18N.INSTANCE.sku());

		//Name & Description
		final LargeEditTextCell descEditCell = new LargeEditTextCell();
		final Column<AccountingDocumentItemDTO, String> nameDescription = 
				new Column<AccountingDocumentItemDTO, String>(descEditCell) {

			@Override
			public String getValue(AccountingDocumentItemDTO object) {
				return object.getDescription();
			}

		};
		nameDescription.setFieldUpdater(new FieldUpdater<AccountingDocumentItemDTO, String>() {

			@Override
			public void update(int index, AccountingDocumentItemDTO object, String value) {
				if(locked){
					Notification.showMessage(I18N.INSTANCE.lockedItemsTableAlert());
					descEditCell.clearViewData(object);
					redrawRow(index);
					return;
				}
				if(Validation.isEmpty(value)){
					Notification.showMessage(I18N.INSTANCE.errorClientData());
				} else if(!Validation.isWithinSize(value, DESCRIPTION_MAX_LENGTH)){
					Notification.showMessage(I18NM.get.textLengthError(DESCRIPTION_MAX_LENGTH));
				} else {
					object.setDescription(value);
					ItemTable.this.handler.onUpdate(object);

				}

				descEditCell.clearViewData(object);
				redrawRow(index);

			}
		});
		addColumn(nameDescription, I18N.INSTANCE.nameDescription());

		//unity of measure
		final EditTextCell unitEditCell = new EditTextCell();
		Column<AccountingDocumentItemDTO, String> unitOfMeasure =
				new Column<AccountingDocumentItemDTO, String>(unitEditCell) {

			@Override
			public String getValue(AccountingDocumentItemDTO object) {
				return object.getUnitOfMeasure();
			}
		};
		unitOfMeasure.setFieldUpdater(new FieldUpdater<AccountingDocumentItemDTO, String>() {

			@Override
			public void update(int index, AccountingDocumentItemDTO object, String value) {
				if(locked){
					Notification.showMessage(I18N.INSTANCE.lockedItemsTableAlert());
					unitEditCell.clearViewData(object);
					redrawRow(index);
					return;
				}

				if(!Validation.isWithinSize(value, UNIT_OF_MEASURE_MAX_SIZE)){
					Notification.showMessage(I18NM.get.textLengthError(UNIT_OF_MEASURE_MAX_SIZE));
				} else {
					object.setUnitOfMeasure(value);
					ItemTable.this.handler.onUpdate(object);
				}

				unitEditCell.clearViewData(object);
				redrawRow(index);
			}
		});
		SafeHtmlBuilder shb = new SafeHtmlBuilder();
		shb.appendHtmlConstant("<span title=\""+I18N.INSTANCE.unityOfMeasureExtended()+"\">");
		shb.appendEscaped(I18N.INSTANCE.unityOfMeasure());
		shb.appendHtmlConstant("</span>");
		addColumn(unitOfMeasure, shb.toSafeHtml());


		//quantity
		final EditTextCell qtyEditCell = new EditTextCell();
		Column<AccountingDocumentItemDTO, String> quantity =
				new Column<AccountingDocumentItemDTO, String>(qtyEditCell) {

			@Override
			public String getValue(AccountingDocumentItemDTO object) {
				if(object.getQuantity() == null) {
					return "";
				}

				return NumberFormat.getDecimalFormat().format(object.getQuantity());
			}
		};
		quantity.setFieldUpdater(new FieldUpdater<AccountingDocumentItemDTO, String>() {

			@Override
			public void update(int index, AccountingDocumentItemDTO object, String value) {
				if(locked){
					Notification.showMessage(I18N.INSTANCE.lockedItemsTableAlert());
					qtyEditCell.clearViewData(object);
					redrawRow(index);
					return;
				}

				try{

					BigDecimal newQty = CalcUtils.parseValue(value);
					object.setQuantity(newQty);
					ItemTable.this.handler.onUpdate(object);

				} catch(NumberFormatException e){

					Notification.showMessage(I18N.INSTANCE.errorClientData());

				}

				qtyEditCell.clearViewData(object);
				redrawRow(index);
			}
		});
		addColumn(quantity, I18N.INSTANCE.quantity());

		Column<AccountingDocumentItemDTO, String> weight = null;
		if(displayWeight) {

			//weight
			final EditTextCell weightEditCell = new EditTextCell();
			weight = new Column<AccountingDocumentItemDTO, String>(weightEditCell) {

				@Override
				public String getValue(AccountingDocumentItemDTO object) {
					if(object.getWeight() == null) {
						return "";
					}

					return NumberFormat.getDecimalFormat().format(object.getWeight());
				}
			};
			weight.setFieldUpdater(new FieldUpdater<AccountingDocumentItemDTO, String>() {

				@Override
				public void update(int index, AccountingDocumentItemDTO object, String value) {
					if(locked){
						Notification.showMessage(I18N.INSTANCE.lockedItemsTableAlert());
						weightEditCell.clearViewData(object);
						redrawRow(index);
						return;
					}

					try{

						if(value.isEmpty()){
							object.setDiscount(null);
						}  else {
							BigDecimal newWeight = CalcUtils.parseValue(value);
							object.setWeight(newWeight);
							ItemTable.this.handler.onUpdate(object);
						}
						
					} catch(NumberFormatException e){

						Notification.showMessage(I18N.INSTANCE.errorClientData());

					}

					weightEditCell.clearViewData(object);
					redrawRow(index);
				}
			});
			addColumn(weight, I18N.INSTANCE.weight());
		}

		//VAT
		final EditTextCell taxEditCell = new EditTextCell();
		Column<AccountingDocumentItemDTO, String> tax =
				new Column<AccountingDocumentItemDTO, String>(taxEditCell) {

			@Override
			public String getValue(AccountingDocumentItemDTO object) {
				if(object.getTax() == null){
					return null;
				}

				return NumberFormat.getDecimalFormat().format(object.getTax());
			}
		};
		tax.setFieldUpdater(new FieldUpdater<AccountingDocumentItemDTO, String>() {

			@Override
			public void update(int index, AccountingDocumentItemDTO object, String value) {
				if(locked){
					Notification.showMessage(I18N.INSTANCE.lockedItemsTableAlert());
					taxEditCell.clearViewData(object);
					redrawRow(index);
					return;
				}

				try{

					BigDecimal newTax = CalcUtils.parseValue(value);
					object.setTax(newTax);
					ItemTable.this.handler.onUpdate(object);

				} catch(NumberFormatException e){

					Notification.showMessage(I18N.INSTANCE.errorClientData());

				}

				taxEditCell.clearViewData(object);
				redrawRow(index);
			}
		});
		addColumn(tax, "%"+I18N.INSTANCE.vat());


		//price
		final EditTextCell priceEditCell = new EditTextCell();
		Column<AccountingDocumentItemDTO, String> price =
				new Column<AccountingDocumentItemDTO, String>(priceEditCell) {

			@Override
			public String getValue(AccountingDocumentItemDTO object) {
				if(object.getPrice() == null){
					return "";
				}

				return String.valueOf(NumberFormat.getCurrencyFormat().format(object.getPrice()));
			}
		};
		price.setFieldUpdater(new FieldUpdater<AccountingDocumentItemDTO, String>() {

			@Override
			public void update(int index, AccountingDocumentItemDTO object, String value) {
				if(locked){
					Notification.showMessage(I18N.INSTANCE.lockedItemsTableAlert());
					priceEditCell.clearViewData(object);
					redrawRow(index);
					return;
				}

				try{

					BigDecimal newPrice = CalcUtils.parseCurrency(value);
					object.setPrice(newPrice);
					ItemTable.this.handler.onUpdate(object);

				} catch(NumberFormatException e){

					Notification.showMessage(I18N.INSTANCE.errorClientData());

				}
				priceEditCell.clearViewData(object);
				redrawRow(index);
			}
		});
		addColumn(price, I18N.INSTANCE.price());


		// discount
		final EditTextCell discountEditCell = new EditTextCell();
		Column<AccountingDocumentItemDTO, String> discount =
				new Column<AccountingDocumentItemDTO, String>(discountEditCell) {

			@Override
			public String getValue(AccountingDocumentItemDTO object) {
				if(object.getDiscount() == null || object.getDiscount().intValue() == 0 ) {
					return "";
				}

				return NumberFormat.getDecimalFormat().format(object.getDiscount());
			}
		};
		discount.setFieldUpdater(new FieldUpdater<AccountingDocumentItemDTO, String>() {

			@Override
			public void update(int index, AccountingDocumentItemDTO object, String value) {
				if(locked){
					Notification.showMessage(I18N.INSTANCE.lockedItemsTableAlert());
					discountEditCell.clearViewData(object);
					redrawRow(index);
					return;
				}

				try{
					if(value.isEmpty()){
						object.setDiscount(null);
					} else {
						BigDecimal newDiscount = CalcUtils.parseValue(value);
						object.setDiscount(newDiscount);
					}
					ItemTable.this.handler.onUpdate(object);
				} catch(NumberFormatException e){

					Notification.showMessage(I18N.INSTANCE.errorClientData());

				}

				discountEditCell.clearViewData(object);
				redrawRow(index);
			}
		});
		addColumn(discount, I18N.INSTANCE.discountLabel());


		//Total Before Taxes
		TextColumn<AccountingDocumentItemDTO> totalBeforeTaxes =
				new TextColumn<AccountingDocumentItemDTO>() {

			@Override
			public String getValue(AccountingDocumentItemDTO object) {
				if(object.getPrice() == null){
					return "";
				}

				return NumberFormat.getCurrencyFormat().format(object.getTotalBeforeTax());
			}
		};
		addColumn(totalBeforeTaxes, I18N.INSTANCE.totalBeforeTaxesForItem());

		//delete button
		ActionCell.Delegate<AccountingDocumentItemDTO> delegate = new ActionCell.Delegate<AccountingDocumentItemDTO>() {

			@Override
			public void execute(AccountingDocumentItemDTO object) {
				if(locked){
					Notification.showMessage(I18N.INSTANCE.lockedItemsTableAlert());
					return;
				}
				ItemTable.this.handler.onDelete(object);
			}
		};

		Column<AccountingDocumentItemDTO, AccountingDocumentItemDTO> delete =
				new Column<AccountingDocumentItemDTO, AccountingDocumentItemDTO>(new ActionCell<AccountingDocumentItemDTO>(I18N.INSTANCE.delete(), delegate)) {

			@Override
			public AccountingDocumentItemDTO getValue(AccountingDocumentItemDTO object) {
				return object;
			}
		};
		addColumn(delete);

		setWidth("99%");
		setColumnWidth(sku, 5, Unit.PCT);
		setColumnWidth(nameDescription, 37, Unit.PCT);
		setColumnWidth(unitOfMeasure, 6, Unit.PCT);
		setColumnWidth(quantity, 6, Unit.PCT);
		if(displayWeight) {
			setColumnWidth(weight, 6, Unit.PCT);
		}
		setColumnWidth(price, 6, Unit.PCT);
		setColumnWidth(discount, 6, Unit.PCT);
		setColumnWidth(tax, 6, Unit.PCT);
		setColumnWidth(totalBeforeTaxes, 8, Unit.PCT);
		setColumnWidth(delete, 10, Unit.PCT);

		setLoadingIndicator(null);
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
		this.moveUpDownCell.setLocked(locked);
		redraw();
	}
}
