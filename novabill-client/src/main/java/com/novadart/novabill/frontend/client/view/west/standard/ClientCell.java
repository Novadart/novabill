package com.novadart.novabill.frontend.client.view.west.standard;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class ClientCell extends AbstractCell<ClientDTO> {

	public interface Style extends CssResource {
		String client();
		String name();
	}
	
	private Style style;
	
	public ClientCell(Style style) {
		this.style = style;
		style.ensureInjected();
	}

	@Override
	public void render(Cell.Context context, ClientDTO value, SafeHtmlBuilder sb) {

		sb.appendHtmlConstant("<div class='"+style.client()+"'>");

		sb.appendHtmlConstant("<span class='"+style.name()+"'>");
		sb.appendEscaped(value.getName());
		sb.appendHtmlConstant("</span>");

		sb.appendHtmlConstant("</div>");

	}
	
}
