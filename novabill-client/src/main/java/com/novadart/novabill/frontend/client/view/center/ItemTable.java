package com.novadart.novabill.frontend.client.view.center;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.novadart.gwtshared.client.validation.Validation;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.util.CalcUtils;
import com.novadart.novabill.frontend.client.widget.notification.Notification;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;

public class ItemTable extends CellTable<AccountingDocumentItemDTO> {


	public static interface Handler{
		public void onDelete(AccountingDocumentItemDTO item);
		public void onUpdate(AccountingDocumentItemDTO item);
	}

	private final Handler handler;


	public ItemTable(Handler handler) {
		super(2000);

		this.handler = handler;

		//Name & Description
		final EditTextCell descEditCell = new EditTextCell();
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
					descEditCell.clearViewData(object);
					redraw();
				} else {
					object.setDescription(value);
					ItemTable.this.handler.onUpdate(object);
					redraw();
				}
			}
		});
		addColumn(nameDescription, I18N.INSTANCE.nameDescription());


		//quantity
		final EditTextCell qtyEditCell = new EditTextCell();
		Column<AccountingDocumentItemDTO, String> quantity =
				new Column<AccountingDocumentItemDTO, String>(qtyEditCell) {

			@Override
			public String getValue(AccountingDocumentItemDTO object) {
				return NumberFormat.getDecimalFormat().format(object.getQuantity());
			}
		};
		quantity.setFieldUpdater(new FieldUpdater<AccountingDocumentItemDTO, String>() {

			@Override
			public void update(int index, AccountingDocumentItemDTO object, String value) {
				double newQty = 0;
				try{
					newQty = NumberFormat.getDecimalFormat().parse(value);
				} catch(NumberFormatException e){
					try{
						newQty = Double.parseDouble(value.replace(',', '.'));//NOTE replace comma with dot, to separate decimals
					} catch(NumberFormatException f){
						Notification.showMessage(I18N.INSTANCE.errorClientData());
						qtyEditCell.clearViewData(object);
						redraw();
						return;
					}
				}
				object.setQuantity(new BigDecimal(newQty));
				ItemTable.this.handler.onUpdate(object);
				redraw();
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
				object.setUnitOfMeasure(value);
				ItemTable.this.handler.onUpdate(object);
				redraw();
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
				return String.valueOf(NumberFormat.getCurrencyFormat().format(object.getPrice()));
			}
		};
		price.setFieldUpdater(new FieldUpdater<AccountingDocumentItemDTO, String>() {

			@Override
			public void update(int index, AccountingDocumentItemDTO object, String value) {
				double newPrice = 0;
				try{
					newPrice = NumberFormat.getCurrencyFormat().parse(value);
				} catch(NumberFormatException e){
					try{
						newPrice = Double.parseDouble(value.replace(',', '.'));//NOTE replace comma with dot, to separate decimals
					} catch(NumberFormatException f){
						Notification.showMessage(I18N.INSTANCE.errorClientData());
						priceEditCell.clearViewData(object);
						redraw();
						return;
					}
				}
				object.setPrice(new BigDecimal(newPrice));
				ItemTable.this.handler.onUpdate(object);
				redraw();
			}
		});
		addColumn(price, I18N.INSTANCE.price());


		//VAT
		List<String> vats = new ArrayList<String>();
		for (String v : I18N.INSTANCE.vatItems()) {
			vats.add(v+"%");
		}
		SelectionCell selectionCell = new SelectionCell(vats);
		Column<AccountingDocumentItemDTO, String> tax =
				new Column<AccountingDocumentItemDTO, String>(selectionCell) {

			@Override
			public String getValue(AccountingDocumentItemDTO object) {
				return String.valueOf(object.getTax().intValue())+"%";
			}
		};
		tax.setFieldUpdater(new FieldUpdater<AccountingDocumentItemDTO, String>() {

			@Override
			public void update(int index, AccountingDocumentItemDTO object, String value) {
				object.setTax(CalcUtils.parseValue(value.split("%")[0]));
				ItemTable.this.handler.onUpdate(object);
				redraw();
			}
		});
		addColumn(tax, I18N.INSTANCE.vat());


		//Total Before Taxes
		TextColumn<AccountingDocumentItemDTO> totalBeforeTaxes =
				new TextColumn<AccountingDocumentItemDTO>() {

			@Override
			public String getValue(AccountingDocumentItemDTO object) {
				BigDecimal totalPrice = CalcUtils.calculateTotalBeforeTaxesForItem(object);
				return NumberFormat.getCurrencyFormat().format(totalPrice.doubleValue());
			}
		};
		addColumn(totalBeforeTaxes, I18N.INSTANCE.totalBeforeTaxesForItem());

		//Total Before Taxes
		TextColumn<AccountingDocumentItemDTO> totalVat =
				new TextColumn<AccountingDocumentItemDTO>() {

			@Override
			public String getValue(AccountingDocumentItemDTO object) {
				BigDecimal totalVat = CalcUtils.calculateTaxesForItem(object);
				return NumberFormat.getCurrencyFormat().format(totalVat.doubleValue());
			}
		};
		addColumn(totalVat, I18N.INSTANCE.totalTaxForItem());


		//Total Before Taxes
		TextColumn<AccountingDocumentItemDTO> totalAfterTaxes =
				new TextColumn<AccountingDocumentItemDTO>() {

			@Override
			public String getValue(AccountingDocumentItemDTO object) {
				BigDecimal total = CalcUtils.calculateTotalAfterTaxesForItem(object); 
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
		setColumnWidth(tax, 6, Unit.PCT);
		setColumnWidth(totalBeforeTaxes, 8, Unit.PCT);
		setColumnWidth(totalVat, 8, Unit.PCT);
		setColumnWidth(totalAfterTaxes, 8, Unit.PCT);
		setColumnWidth(delete, 10, Unit.PCT);

		setLoadingIndicator(null);
	}

}
