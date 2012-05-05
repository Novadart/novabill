package com.novadart.novabill.frontend.client.ui.west;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class SimpleClientCell extends AbstractCell<ClientDTO> {

	public interface Handler {
		public void onClientSelected(ClientDTO client);
	}

	private Handler handler;
	
	public SimpleClientCell() {
		super("click");
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	
	@Override
	public void render(Cell.Context context, ClientDTO value, SafeHtmlBuilder sb) {

		sb.appendHtmlConstant("<div class='client-simple'>");
		sb.appendHtmlConstant("<div class='main'>");

		sb.appendHtmlConstant("<span class='name'>");
		sb.appendEscaped(value.getName());
		sb.appendHtmlConstant("</span>");

		sb.appendHtmlConstant("</div>");

	}
	
	@Override
	public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context,
			Element parent, ClientDTO value, NativeEvent event,
			ValueUpdater<ClientDTO> valueUpdater) {
		super.onBrowserEvent(context, parent, value, event, valueUpdater);
		
		if(value == null){
			return;
		}
		
		handler.onClientSelected(value);
	}

}
