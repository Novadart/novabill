package com.novadart.novabill.frontend.client.ui.widget.list.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.novadart.novabill.frontend.client.ui.View.Presenter;
import com.novadart.novabill.frontend.client.ui.widget.list.Preview;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;

public class InvoicePreview extends Composite implements Preview<InvoiceDTO> {

	private static InvoicePreviewUiBinder uiBinder = GWT
			.create(InvoicePreviewUiBinder.class);

	interface InvoicePreviewUiBinder extends UiBinder<Widget, InvoicePreview> {
	}

	public InvoicePreview() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void updatePreview(InvoiceDTO value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		// TODO Auto-generated method stub
		
	}

}
