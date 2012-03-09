package com.novadart.novabill.frontend.client.ui.widget.list;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public abstract class QuickViewCell<T> extends AbstractCell<T> {

	public static interface Handler<ListItem> {
		public void onRowSelected(Cell.Context context, ListItem value);
	}

	private T selected = null;
	private Handler<T> handler;

	protected QuickViewCell() {
		super("click");
	}

	void setHandler(Handler<T> handler) {
		this.handler = handler;
	}

	private boolean isSelected(T item){
		if(selected == null){
			selected = item;
			return true;
		} else {
			return itemsAreEqual(selected, item);
		}
	}
	
	protected abstract boolean itemsAreEqual(T item1, T item2);
	
	protected abstract void render(Cell.Context context, T value, SafeHtmlBuilder sb, boolean isSelected);
	
	@Override
	public final void render(Cell.Context context, T value, SafeHtmlBuilder sb) {

		if(value == null){
			return;
		}

		render(context, value, sb, isSelected(value));
	};
	
	
	@Override
	public void onBrowserEvent(Cell.Context context, Element parent, T value, NativeEvent event,
			ValueUpdater<T> valueUpdater) {
		
		super.onBrowserEvent(context, parent, value, event, valueUpdater);

		if(value == null){
			return;
		}
		
		if( "click".equals(event.getType()) || "mouseover".equals(event.getType()) ){
			selected = value;
			handler.onRowSelected(context, value);
		}
		
	}

}
