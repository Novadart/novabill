package com.novadart.novabill.shared.client.facade;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.novadart.novabill.shared.client.dto.PriceListDTO;

public interface PriceListGwtServiceAsync {

	void getAll(Long businessID, AsyncCallback<List<PriceListDTO>> callback);

	void get(Long id, AsyncCallback<PriceListDTO> callback);

	void add(PriceListDTO priceListDTO, AsyncCallback<Long> callback);

	void update(PriceListDTO priceListDTO, AsyncCallback<Void> callback);

	void remove(Long businessID, Long id, AsyncCallback<Void> callback);

}
