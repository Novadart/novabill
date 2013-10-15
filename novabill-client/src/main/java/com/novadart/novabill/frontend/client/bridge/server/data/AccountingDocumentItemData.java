package com.novadart.novabill.frontend.client.bridge.server.data;

import java.util.List;

import com.novadart.novabill.shared.client.dto.IAccountingDocumentItemDTO;

public class AccountingDocumentItemData implements IAccountingDocumentItemData {

	private List<IAccountingDocumentItemDTO> items;

	@Override
	public List<IAccountingDocumentItemDTO> getItems() {
		return items;
	}

	@Override
	public void setItems(List<IAccountingDocumentItemDTO> items) {
		this.items = items;
	}
}
