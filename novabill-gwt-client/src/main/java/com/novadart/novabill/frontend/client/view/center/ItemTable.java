package com.novadart.novabill.frontend.client.view.center;

import java.math.BigDecimal;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
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
import com.novadart.novabill.frontend.client.util.DocumentUtils;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;

public class ItemTable extends CellTable<AccountingDocumentItemDTO> {
	
	private static final int DESCRIPTION_MAX_LENGTH = 500;
	private static final int UNIT_OF_MEASURE_MAX_SIZE = 255;


	public static interface Handler{
		public void onDelete(AccountingDocumentItemDTO item);
		public void onUpdate(AccountingDocumentItemDTO item);
	}

	private final Handler handler;


	public ItemTable(Handler handler) {
		super(2000);

		this.handler = handler;

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
				if(object.getQuantity() != null){
					try{

						BigDecimal newQty = DocumentUtils.parseValue(value);
						object.setQuantity(newQty);
						ItemTable.this.handler.onUpdate(object);

					} catch(NumberFormatException e){

						Notification.showMessage(I18N.INSTANCE.errorClientData());

					}
				}			

				qtyEditCell.clearViewData(object);
				redrawRow(index);
			}
		});
		addColumn(quantity, I18N.INSTANCE.quantity());


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
				if(!Validation.isWithinSize(value, UNIT_OF_MEASURE_MAX_SIZE)){
					Notification.showMessage(I18NM.get.textLengthError(UNIT_OF_MEASURE_MAX_SIZE));
				} else if(object.getUnitOfMeasure() != null){
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
				if(object.getPrice() != null){
					try{

						BigDecimal newPrice = DocumentUtils.parseCurrency(value);
						object.setPrice(newPrice);
						ItemTable.this.handler.onUpdate(object);

					} catch(NumberFormatException e){

						Notification.showMessage(I18N.INSTANCE.errorClientData());

					}
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
				if(object.getDiscount() == null || object.getDiscount().equals(BigDecimal.ZERO) ) {
					return "";
				}

				return NumberFormat.getDecimalFormat().format(object.getDiscount());
			}
		};
		discount.setFieldUpdater(new FieldUpdater<AccountingDocumentItemDTO, String>() {

			@Override
			public void update(int index, AccountingDocumentItemDTO object, String value) {
				if(object.getDiscount() != null){
					try{

						BigDecimal newDiscount = DocumentUtils.parseValue(value);
						object.setDiscount(newDiscount);
						ItemTable.this.handler.onUpdate(object);

					} catch(NumberFormatException e){

						Notification.showMessage(I18N.INSTANCE.errorClientData());

					}
				}			

				discountEditCell.clearViewData(object);
				redrawRow(index);
			}
		});
		addColumn(discount, I18N.INSTANCE.discountLabel());


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
				if(object.getTax() != null){
					try{

						BigDecimal newTax = DocumentUtils.parseValue(value);
						object.setTax(newTax);
						ItemTable.this.handler.onUpdate(object);

					} catch(NumberFormatException e){

						Notification.showMessage(I18N.INSTANCE.errorClientData());

					}
				}			

				taxEditCell.clearViewData(object);
				redrawRow(index);
			}
		});
		addColumn(tax, "%"+I18N.INSTANCE.vat());


		//Total Before Taxes
		TextColumn<AccountingDocumentItemDTO> totalBeforeTaxes =
				new TextColumn<AccountingDocumentItemDTO>() {

			@Override
			public String getValue(AccountingDocumentItemDTO object) {
				if(object.getPrice() == null){
					return "";
				}

				BigDecimal totalPrice = DocumentUtils.calculateTotalBeforeTaxesForItem(object);
				return NumberFormat.getCurrencyFormat().format(totalPrice.doubleValue());
			}
		};
		addColumn(totalBeforeTaxes, I18N.INSTANCE.totalBeforeTaxesForItem());

		//Total Before Taxes
		TextColumn<AccountingDocumentItemDTO> totalVat =
				new TextColumn<AccountingDocumentItemDTO>() {

			@Override
			public String getValue(AccountingDocumentItemDTO object) {
				if(object.getPrice() == null){
					return "";
				}

				BigDecimal totalVat = DocumentUtils.calculateTaxesForItem(object);
				return NumberFormat.getCurrencyFormat().format(totalVat.doubleValue());
			}
		};
		addColumn(totalVat, I18N.INSTANCE.totalTaxForItem());


		//Total Before Taxes
		TextColumn<AccountingDocumentItemDTO> totalAfterTaxes =
				new TextColumn<AccountingDocumentItemDTO>() {

			@Override
			public String getValue(AccountingDocumentItemDTO object) {
				if(object.getPrice() == null){
					return "";
				}

				BigDecimal total = DocumentUtils.calculateTotalAfterTaxesForItem(object); 
				return NumberFormat.getCurrencyFormat().format(total.doubleValue());
			}
		};
		addColumn(totalAfterTaxes, I18N.INSTANCE.totalAfterTaxesForItem());


		//delete button
		ActionCell.Delegate<AccountingDocumentItemDTO> delegate = new ActionCell.Delegate<AccountingDocumentItemDTO>() {

			@Override
			public void execute(AccountingDocumentItemDTO object) {
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
		setColumnWidth(nameDescription, 40, Unit.PCT);
		setColumnWidth(quantity, 6, Unit.PCT);
		setColumnWidth(unitOfMeasure, 7, Unit.PCT);
		setColumnWidth(price, 7, Unit.PCT);
		setColumnWidth(discount, 6, Unit.PCT);
		setColumnWidth(tax, 6, Unit.PCT);
		setColumnWidth(totalBeforeTaxes, 8, Unit.PCT);
		setColumnWidth(totalVat, 8, Unit.PCT);
		setColumnWidth(totalAfterTaxes, 8, Unit.PCT);
		setColumnWidth(delete, 10, Unit.PCT);

		setLoadingIndicator(null);
	}

}
