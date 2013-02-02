package com.novadart.novabill.frontend.client.widget.list;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public abstract class QuickViewCell<T> extends AbstractCell<T> {

	public static interface Handler<ListItem> {
		public void onRowSelected(Cell.Context context, ListItem value);
	}

	protected QuickViewCell() {
		super("click");
	}
	
	@Override
	public final void render(Cell.Context context, T value, SafeHtmlBuilder sb) {

		if(value == null){
			return;
		}
		
		sb.appendHtmlConstant("<div class='quickViewCell'>");
		renderVisible(context, value, sb);
		sb.appendHtmlConstant("<div class='details'>");
		renderDetails(context, value, sb);
		sb.appendHtmlConstant("</div>");
		sb.appendHtmlConstant("</div>");
	};
	
	protected abstract void renderVisible(Cell.Context context, T value, SafeHtmlBuilder sb);
	
	protected abstract void renderDetails(Cell.Context context, T value, SafeHtmlBuilder sb);
	
	
	@Override
	public void onBrowserEvent(Cell.Context context, Element parent, T value, NativeEvent event,
			ValueUpdater<T> valueUpdater) {
		
		if("click".equals(event.getType())){
			
			onClick(value, event.getEventTarget());
			
		} else {
			
			super.onBrowserEvent(context, parent, value, event, valueUpdater);
			
		}
	}
	
	protected abstract void onClick(T value, EventTarget eventTarget);

}
