package com.novadart.novabill.shared.client.facade;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;

public interface CommodityService extends RemoteService {
	
	public List<CommodityDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException;

}
