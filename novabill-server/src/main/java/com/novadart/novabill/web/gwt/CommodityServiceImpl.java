package com.novadart.novabill.web.gwt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.facade.BusinessService;
import com.novadart.novabill.shared.client.facade.CommodityService;

public class CommodityServiceImpl implements CommodityService {

	@Autowired
	private BusinessService businessService;
	
	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public List<CommodityDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return businessService.getCommodities(businessID);
	}

}
