package com.novadart.novabill.frontend.client.ui.widget.list;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public abstract class PreviewCell<T> extends AbstractCell<T> {

	public static interface Handler<T> {
		public void onClick(T value);
	}
	
	private Handler<T> handler;
	private T selected = null;
	
	public PreviewCell() {
		super("click");
	}
	
	public void setHandler(Handler<T> handler) {
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
	public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context,
			Element parent, T value, NativeEvent event,
			ValueUpdater<T> valueUpdater) {
		
		super.onBrowserEvent(context, parent, value, event, valueUpdater);
		
		if(value == null){
			return;
		}
		
		if( "click".equals(event.getType()) ){
			selected = value;
			if(this.handler != null){
				handler.onClick(value);
			}
		}
		
	}

}
