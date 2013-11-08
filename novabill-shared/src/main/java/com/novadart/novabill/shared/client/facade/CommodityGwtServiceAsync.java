package com.novadart.novabill.shared.client.facade;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;

public interface CommodityGwtServiceAsync {

	void getAll(Long businessID, AsyncCallback<List<CommodityDTO>> callback);

	void add(CommodityDTO paymentTypeDTO, AsyncCallback<Long> callback);

	void remove(Long businessID, Long id, AsyncCallback<Void> callback);

	void update(CommodityDTO paymentTypeDTO, AsyncCallback<Void> callback);

	void searchCommodities(Long businessID, String query, int start,
			int offset, AsyncCallback<PageDTO<CommodityDTO>> callback);

}
