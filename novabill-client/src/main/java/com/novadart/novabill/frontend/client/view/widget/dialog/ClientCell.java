package com.novadart.novabill.frontend.client.view.widget.dialog;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.novadart.novabill.shared.client.dto.ClientDTO;

public class ClientCell extends AbstractCell<ClientDTO> {

	@Override
	public void render(Cell.Context context, ClientDTO value, SafeHtmlBuilder sb) {

		sb.appendHtmlConstant("<div class='client'>");

		sb.appendHtmlConstant("<span class='name'>");
		sb.appendEscaped(value.getName());
		sb.appendHtmlConstant("</span>");

		sb.appendHtmlConstant("</div>");

	}
	
}
