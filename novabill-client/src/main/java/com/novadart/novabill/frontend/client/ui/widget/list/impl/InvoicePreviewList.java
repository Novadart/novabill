package com.novadart.novabill.frontend.client.ui.widget.list.impl;

import com.novadart.novabill.frontend.client.ui.widget.list.Preview;
import com.novadart.novabill.frontend.client.ui.widget.list.PreviewCell;
import com.novadart.novabill.frontend.client.ui.widget.list.PreviewList;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class InvoicePreviewList extends PreviewList<InvoiceDTO> {

	@Override
	protected PreviewCell<InvoiceDTO> buildPreviewCell() {
		return new InvoicePreviewCell();
	}

	@Override
	protected Preview<InvoiceDTO> buildPreview() {
		return new InvoicePreview();
	}

}
