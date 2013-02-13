package com.novadart.novabill.frontend.client.view.west.standard;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class ClientCell extends AbstractCell<ClientDTO> {

	public interface Style extends CssResource {
		String client();
		String name();
	}
	
	public interface Handler {
		public void onClientSelected(ClientDTO client);
	}

	private Handler handler;
	private Style style;
	
	public ClientCell(Style style) {
		super("click");
		this.style = style;
		style.ensureInjected();
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	
	@Override
	public void render(Cell.Context context, ClientDTO value, SafeHtmlBuilder sb) {

		sb.appendHtmlConstant("<div class='"+style.client()+"'>");

		sb.appendHtmlConstant("<span class='"+style.name()+"'>");
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
