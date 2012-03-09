package com.novadart.novabill.frontend.client.ui.widget.list;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.NoSelectionModel;

public class QuickViewList<T> extends Composite {
	
	public static interface Handler<T> {
		public void onRowSelected(T value);
	}

	private final QuickViewCell<T> cellRenderer;
	private final CellList<T> list;
	private T selectedInvoice = null;
	private Handler<T> handler;
	
	public QuickViewList(QuickViewCell<T> cell) {
		
		cellRenderer = cell;
		
		getCellRenderer().setHandler(new QuickViewCell.Handler<T>() {

			@Override
			public void onRowSelected(Context context, T value) {
				selectedInvoice = value;
				list.redraw();
				if(handler != null){
					handler.onRowSelected(value);
				}
			}
		});
		
		list = new CellList<T>(getCellRenderer());
		initWidget(list);
		
		list.setSelectionModel(new NoSelectionModel<T>());
		list.setPageSize(10);
		
		setStyleName("InvoiceList");
	}
	
	
	public void setHandler(Handler<T> handler) {
		this.handler = handler;
	}
	
	public HasData<T> getList(){
		return list;
	}
	
	public T getSelectedInvoice() {
		return selectedInvoice;
	}

	public QuickViewCell<T> getCellRenderer() {
		return cellRenderer;
	}
	
}
