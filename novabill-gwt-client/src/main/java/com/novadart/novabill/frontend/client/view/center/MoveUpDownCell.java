package com.novadart.novabill.frontend.client.view.center;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.novadart.novabill.frontend.client.i18n.I18N;
import com.novadart.novabill.frontend.client.resources.ImageResources;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;

public class MoveUpDownCell extends AbstractCell<AccountingDocumentItemDTO> {

	private final ItemTable.Handler handler;
	private boolean locked = false;
	
	public MoveUpDownCell(ItemTable.Handler handler) {
		super("click");
		this.handler = handler;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	@Override
	public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context,
			Element parent, AccountingDocumentItemDTO value, NativeEvent event,
			ValueUpdater<AccountingDocumentItemDTO> valueUpdater) {
		// Let AbstractCell handle the keydown event.
		super.onBrowserEvent(context, parent, value, event, valueUpdater);

		// Handle the click event.
		if ( !locked && "click".equals(event.getType())) {
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
		
		if(!locked) {
			sb.appendHtmlConstant("<table><tr>");
			sb.appendHtmlConstant("<td><img title='"+I18N.INSTANCE.moveUp()+"' class='moveUp "+AccountDocument.CSS.upDownButton()+"' src='"+ImageResources.INSTANCE.arrow_up().getSafeUri().asString()+"'></td>");
			sb.appendHtmlConstant("<td><img title='"+I18N.INSTANCE.moveDown()+"' class='moveDown "+AccountDocument.CSS.upDownButton()+"' src='"+ImageResources.INSTANCE.arrow_down().getSafeUri().asString()+"'></td>");
			sb.appendHtmlConstant("</tr></table>");
		}
	}

}


