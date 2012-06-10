package com.novadart.novabill.frontend.client.ui.widget.list.impl;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.novadart.novabill.frontend.client.ui.widget.list.PreviewCell;
import com.novadart.novabill.shared.client.dto.EstimationDTO;

public class EstimationPreviewCell extends PreviewCell<EstimationDTO> {

	@Override
	protected boolean itemsAreEqual(EstimationDTO item1, EstimationDTO item2) {
		return item1.getId().equals(item2.getId());
	}

	@Override
	protected void render(com.google.gwt.cell.client.Cell.Context context,
			EstimationDTO value, SafeHtmlBuilder sb, boolean isSelected) {
		
		if(isSelected){
			sb.appendHtmlConstant("<div class='invoice clicked'>");
		} else {
			sb.appendHtmlConstant("<div class='invoice'>");
		}
		sb.appendHtmlConstant("<span class='id'>");
		sb.append(value.getDocumentID());
		sb.appendHtmlConstant("</span>");

		sb.appendHtmlConstant("<span class='date'>");
		sb.appendEscaped(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_LONG).format(value.getAccountingDocumentDate()));
		sb.appendHtmlConstant("</span>");

		sb.appendHtmlConstant("<span class='name'>");
		sb.appendEscaped(value.getClient().getName());
		sb.appendHtmlConstant("</span>");

		sb.appendHtmlConstant("</div>");
	}

}
