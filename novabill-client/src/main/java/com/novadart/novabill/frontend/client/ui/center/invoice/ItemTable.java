package com.novadart.novabill.frontend.client.ui.center.invoice;

import java.math.BigDecimal;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.shared.client.dto.InvoiceItemDTO;

public class ItemTable extends CellTable<InvoiceItemDTO> {
	
	
	public static interface Handler{
		public void delete(InvoiceItemDTO item);
	}

	private final Handler handler;
	
	public ItemTable(Handler handler) {

		this.handler = handler;
		
		//Name & Description
		Column<InvoiceItemDTO, SafeHtml> nameDescription = 
				new Column<InvoiceItemDTO, SafeHtml>(new SafeHtmlCell()) {

					@Override
					public SafeHtml getValue(InvoiceItemDTO object) {
						SafeHtmlBuilder shb = new SafeHtmlBuilder();
						shb.appendEscaped(object.getDescription());
						return shb.toSafeHtml();
					}

		};
		addColumn(nameDescription, I18N.get.nameDescription());

		
		
		//quantity
		Column<InvoiceItemDTO, String> quantity =
				new Column<InvoiceItemDTO, String>(new TextCell()) {

			@Override
			public String getValue(InvoiceItemDTO object) {
				return NumberFormat.getDecimalFormat().format(object.getQuantity());
			}
		};
	
		addColumn(quantity, I18N.get.quantity());


		//unity of measure
		Column<InvoiceItemDTO, String> unityOfMeasure =
				new Column<InvoiceItemDTO, String>(new TextCell()) {

			@Override
			public String getValue(InvoiceItemDTO object) {
				return object.getUnitOfMeasure();
			}
		};
		addColumn(unityOfMeasure, I18N.get.unityOfMeasure());



		//price
		Column<InvoiceItemDTO, String> price =
				new Column<InvoiceItemDTO, String>(new TextCell()) {

			@Override
			public String getValue(InvoiceItemDTO object) {
				return String.valueOf(NumberFormat.getCurrencyFormat().format(object.getPrice()));
			}
		};
		addColumn(price, I18N.get.price());


		//VAT
		Column<InvoiceItemDTO, String> tax =
				new Column<InvoiceItemDTO, String>(new TextCell()) {

			@Override
			public String getValue(InvoiceItemDTO object) {
				return String.valueOf(object.getTax())+"%";
			}
		};
		addColumn(tax, I18N.get.vat());


		//Total Before Taxes
		TextColumn<InvoiceItemDTO> totalBeforeTaxes =
				new TextColumn<InvoiceItemDTO>() {

			@Override
			public String getValue(InvoiceItemDTO object) {
				BigDecimal totalPrice = InvoiceUtils.calculateTotalBeforeTaxesForItem(object);
				return NumberFormat.getCurrencyFormat().format(totalPrice.doubleValue());
			}
		};
		addColumn(totalBeforeTaxes, I18N.get.totalBeforeTaxesForItem());

		//Total Before Taxes
		TextColumn<InvoiceItemDTO> totalVat =
				new TextColumn<InvoiceItemDTO>() {

			@Override
			public String getValue(InvoiceItemDTO object) {
				BigDecimal totalVat = InvoiceUtils.calculateTaxesForItem(object);
				return NumberFormat.getCurrencyFormat().format(totalVat.doubleValue());
			}
		};
		addColumn(totalVat, I18N.get.totalTaxForItem());


		//Total Before Taxes
		TextColumn<InvoiceItemDTO> totalAfterTaxes =
				new TextColumn<InvoiceItemDTO>() {

			@Override
			public String getValue(InvoiceItemDTO object) {
				BigDecimal total = InvoiceUtils.calculateTotalAfterTaxesForItem(object); 
				return NumberFormat.getCurrencyFormat().format(total.doubleValue());
			}
		};
		addColumn(totalAfterTaxes, I18N.get.totalAfterTaxesForItem());


		//delete button
		ActionCell.Delegate<InvoiceItemDTO> delegate = new ActionCell.Delegate<InvoiceItemDTO>() {

			@Override
			public void execute(InvoiceItemDTO object) {
				ItemTable.this.handler.delete(object);
			}
		};

		Column<InvoiceItemDTO, InvoiceItemDTO> delete =
				new Column<InvoiceItemDTO, InvoiceItemDTO>(new ActionCell<InvoiceItemDTO>(I18N.get.delete(), delegate)) {

			@Override
			public InvoiceItemDTO getValue(InvoiceItemDTO object) {
				return object;
			}
		};
		addColumn(delete);

		setLoadingIndicator(null);
	}

}
