package com.novadart.novabill.shared.client.facade;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.CommodityDTO;

public interface CommodityServiceAsync {

	void getAll(Long businessID, AsyncCallback<List<CommodityDTO>> callback);

}
