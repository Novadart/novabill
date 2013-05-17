package com.novadart.novabill.frontend.client.view.center;

import java.util.List;

import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class VatCell extends SelectionCell {

	public VatCell(List<String> options) {
		super(options);
	}

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context, String value, SafeHtmlBuilder sb) {
		if(value == null || value.isEmpty()) {
			sb.appendHtmlConstant("");
		} else {
			super.render(context, value, sb);
		}
	}
	
	
}
