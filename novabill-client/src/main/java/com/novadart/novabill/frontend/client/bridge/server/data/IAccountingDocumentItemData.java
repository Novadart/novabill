package com.novadart.novabill.frontend.client.bridge.server.data;

import java.util.List;

import com.novadart.novabill.shared.client.dto.IAccountingDocumentItemDTO;

public interface IAccountingDocumentItemData {

	public List<IAccountingDocumentItemDTO> getItems();

	public void setItems(List<IAccountingDocumentItemDTO> items);

}