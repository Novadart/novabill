package com.novadart.novabill.frontend.client.view.center;

import com.google.gwt.cell.client.*;
import com.google.gwt.cell.client.TextInputCell.ViewData;
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
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;

import java.math.BigDecimal;

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

	@SuppressWarnings("unchecked")
	public ItemTable(boolean displayWeight, boolean readOnly, Handler handler) {
		super(10000);

		this.handler = handler;

		if(!readOnly) {

			MoveUpDownCell moveUpDownCell = new MoveUpDownCell(this.handler);
			final Column<AccountingDocumentItemDTO, AccountingDocumentItemDTO> moveUpDown = 
					new Column<AccountingDocumentItemDTO, AccountingDocumentItemDTO>(moveUpDownCell) {

				@Override
				public AccountingDocumentItemDTO getValue(AccountingDocumentItemDTO object) {
					return object;
				}
			};
			addColumn(moveUpDown);
		} 

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
		final AbstractCell<String> descEditCell = readOnly ? new TextCell() : new LargeEditTextCell();
		final Column<AccountingDocumentItemDTO, String> nameDescription = 
				new Column<AccountingDocumentItemDTO, String>(descEditCell) {

			@Override
			public String getValue(AccountingDocumentItemDTO object) {
				return object.getDescription();
			}

		};
		if(!readOnly) {
			nameDescription.setFieldUpdater(new FieldUpdater<AccountingDocumentItemDTO, String>() {

				@Override
				public void update(int index, AccountingDocumentItemDTO object, String value) {
					if(Validation.isEmpty(value)){
						Notification.showMessage(I18N.INSTANCE.errorClientData());
					} else if(!Validation.isWithinSize(value, DESCRIPTION_MAX_LENGTH)){
						Notification.showMessage(I18NM.get.textLengthError(DESCRIPTION_MAX_LENGTH));
					} else {
						object.setDescription(value);
						ItemTable.this.handler.onUpdate(object);

					}

					((AbstractEditableCell<String, ViewData>) descEditCell).clearViewData(object);
					redrawRow(index);

				}
			});
		}
		addColumn(nameDescription, I18N.INSTANCE.nameDescription());

		//unity of measure
		final AbstractCell<String> unitEditCell = readOnly ? new TextCell() : new EditTextCell();
		Column<AccountingDocumentItemDTO, String> unitOfMeasure =
				new Column<AccountingDocumentItemDTO, String>(unitEditCell) {

			@Override
			public String getValue(AccountingDocumentItemDTO object) {
				return object.getUnitOfMeasure()==null ? "" : object.getUnitOfMeasure();
			}
		};
		if(!readOnly) {
			unitOfMeasure.setFieldUpdater(new FieldUpdater<AccountingDocumentItemDTO, String>() {

				@Override
				public void update(int index, AccountingDocumentItemDTO object, String value) {
					if(!DocumentUtils.isTextOnly(object)){
						if(!Validation.isWithinSize(value, UNIT_OF_MEASURE_MAX_SIZE)){
							Notification.showMessage(I18NM.get.textLengthError(UNIT_OF_MEASURE_MAX_SIZE));
						} else {
							object.setUnitOfMeasure(value);
							ItemTable.this.handler.onUpdate(object);
						}
					}
					((AbstractEditableCell<String, ViewData>) unitEditCell).clearViewData(object);
					redrawRow(index);
				}
			});
		}
		SafeHtmlBuilder shb = new SafeHtmlBuilder();
		shb.appendHtmlConstant("<span title=\""+I18N.INSTANCE.unityOfMeasureExtended()+"\">");
		shb.appendEscaped(I18N.INSTANCE.unityOfMeasure());
		shb.appendHtmlConstant("</span>");
		addColumn(unitOfMeasure, shb.toSafeHtml());


		//quantity
		final AbstractCell<String> qtyEditCell = readOnly ? new TextCell() : new EditTextCell();
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
		if(!readOnly) {
			quantity.setFieldUpdater(new FieldUpdater<AccountingDocumentItemDTO, String>() {

				@Override
				public void update(int index, AccountingDocumentItemDTO object, String value) {
					if(!DocumentUtils.isTextOnly(object)){
						try{

							BigDecimal newQty = CalcUtils.parseValue(value);
							object.setQuantity(newQty);
							ItemTable.this.handler.onUpdate(object);

						} catch(NumberFormatException e){

							Notification.showMessage(I18N.INSTANCE.errorClientData());

						}
					}
					((AbstractEditableCell<String, ViewData>) qtyEditCell).clearViewData(object);
					redrawRow(index);
				}
			});
		}
		addColumn(quantity, I18N.INSTANCE.quantity());

		Column<AccountingDocumentItemDTO, String> weight = null;
		if(displayWeight) {

			//weight
			final AbstractCell<String> weightEditCell = readOnly ? new TextCell() : new EditTextCell();
			weight = new Column<AccountingDocumentItemDTO, String>(weightEditCell) {

				@Override
				public String getValue(AccountingDocumentItemDTO object) {
					if(object.getWeight() == null) {
						return "";
					}

					return NumberFormat.getDecimalFormat().format(object.getWeight());
				}
			};

			if(!readOnly) {
				weight.setFieldUpdater(new FieldUpdater<AccountingDocumentItemDTO, String>() {

					@Override
					public void update(int index, AccountingDocumentItemDTO object, String value) {
						if(!DocumentUtils.isTextOnly(object)){
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
						}
						((AbstractEditableCell<String, ViewData>) weightEditCell).clearViewData(object);
						redrawRow(index);
					}
				});
			}
			addColumn(weight, I18N.INSTANCE.weight());
		}

		//VAT
		final AbstractCell<String> taxEditCell = readOnly ? new TextCell() : new EditTextCell();
		Column<AccountingDocumentItemDTO, String> tax =
				new Column<AccountingDocumentItemDTO, String>(taxEditCell) {

			@Override
			public String getValue(AccountingDocumentItemDTO object) {
				if(object.getTax() == null){
					return "";
				}

				return NumberFormat.getDecimalFormat().format(object.getTax());
			}
		};
		if(!readOnly) {
			tax.setFieldUpdater(new FieldUpdater<AccountingDocumentItemDTO, String>() {

				@Override
				public void update(int index, AccountingDocumentItemDTO object, String value) {
					if(!DocumentUtils.isTextOnly(object)){
						try{

							BigDecimal newTax = CalcUtils.parseValue(value);
							object.setTax(newTax);
							ItemTable.this.handler.onUpdate(object);

						} catch(NumberFormatException e){

							Notification.showMessage(I18N.INSTANCE.errorClientData());

						}
					}
					((AbstractEditableCell<String, ViewData>) taxEditCell).clearViewData(object);
					redrawRow(index);
				}
			});
		}
		addColumn(tax, "%"+I18N.INSTANCE.vat());


		//price
		final AbstractCell<String> priceEditCell = readOnly ? new TextCell() : new EditTextCell();
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

		if(!readOnly) {
			price.setFieldUpdater(new FieldUpdater<AccountingDocumentItemDTO, String>() {

				@Override
				public void update(int index, AccountingDocumentItemDTO object, String value) {
					if(!DocumentUtils.isTextOnly(object)){
						try{

							BigDecimal newPrice = CalcUtils.parseCurrency(value);
							object.setPrice(newPrice);
							ItemTable.this.handler.onUpdate(object);

						} catch(NumberFormatException e){

							Notification.showMessage(I18N.INSTANCE.errorClientData());

						}
					}
					((AbstractEditableCell<String, ViewData>) priceEditCell).clearViewData(object);
					redrawRow(index);
				}
			});
		}
		addColumn(price, I18N.INSTANCE.price());


		// discount
		final AbstractCell<String> discountEditCell = readOnly ? new TextCell() : new EditTextCell();
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
		if(!readOnly) {
			discount.setFieldUpdater(new FieldUpdater<AccountingDocumentItemDTO, String>() {

				@Override
				public void update(int index, AccountingDocumentItemDTO object, String value) {
					if(!DocumentUtils.isTextOnly(object)){
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
					}

					((AbstractEditableCell<String, ViewData>) discountEditCell).clearViewData(object);
					redrawRow(index);
				}
			});
		}
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

		Column<AccountingDocumentItemDTO, AccountingDocumentItemDTO> delete = null;
		if(!readOnly) {
			//delete button
			ActionCell.Delegate<AccountingDocumentItemDTO> delegate = new ActionCell.Delegate<AccountingDocumentItemDTO>() {

				@Override
				public void execute(AccountingDocumentItemDTO object) {
					ItemTable.this.handler.onDelete(object);
				}
			};

			delete =
					new Column<AccountingDocumentItemDTO, AccountingDocumentItemDTO>(new ActionCell<AccountingDocumentItemDTO>(I18N.INSTANCE.delete(), delegate)) {

				@Override
				public AccountingDocumentItemDTO getValue(AccountingDocumentItemDTO object) {
					return object;
				}
			};
			addColumn(delete);
		}

		setWidth("99%");
		if(!readOnly) {
			setColumnWidth(sku, 5, Unit.PCT);
		}
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
		if(!readOnly) {
			setColumnWidth(delete, 10, Unit.PCT);
		}

		setLoadingIndicator(null);
	}

}
