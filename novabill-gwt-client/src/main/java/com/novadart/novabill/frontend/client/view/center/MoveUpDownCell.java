package com.novadart.novabill.frontend.client.view.center;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.novadart.novabill.frontend.client.resources.ImageResources;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;

public class MoveUpDownCell extends AbstractCell<AccountingDocumentItemDTO> {

	private final ItemTable.Handler handler;
	
	public MoveUpDownCell(ItemTable.Handler handler) {
		super("click");
		this.handler = handler;
	}


	@Override
	public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context,
			Element parent, AccountingDocumentItemDTO value, NativeEvent event,
			ValueUpdater<AccountingDocumentItemDTO> valueUpdater) {
		// Let AbstractCell handle the keydown event.
		super.onBrowserEvent(context, parent, value, event, valueUpdater);

		// Handle the click event.
		if ("click".equals(event.getType())) {
			// Ignore clicks that occur outside of the outermost element.
			EventTarget eventTarget = event.getEventTarget();
			Element elm = Element.as(eventTarget);
			
			if( parent.getFirstChildElement().isOrHasChild(elm) ) {
				if(elm.getClassName().contains("moveUp")){
					this.handler.onMoveUp(value);
				} else if(elm.getClassName().contains("moveDown")){
					this.handler.onMoveDown(value);
				}
			}
		}
	}

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			AccountingDocumentItemDTO value, SafeHtmlBuilder sb) {

		if (value == null) {
			return;
		}
		
		sb.appendHtmlConstant("<table><tr>");
		sb.appendHtmlConstant("<td><img class='moveUp "+AccountDocument.CSS.upDownButton()+"' src='"+ImageResources.INSTANCE.arrow_up().getSafeUri().asString()+"'></td>");
		sb.appendHtmlConstant("<td><img class='moveDown "+AccountDocument.CSS.upDownButton()+"' src='"+ImageResources.INSTANCE.arrow_down().getSafeUri().asString()+"'></td>");
		sb.appendHtmlConstant("</tr></table>");
	}

}


