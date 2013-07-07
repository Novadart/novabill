package com.novadart.novabill.web.gwt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.novadart.novabill.annotation.HandleGWTServiceAccessDenied;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.InvalidArgumentException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.CommodityService;

@HandleGWTServiceAccessDenied
public class CommodityServiceProxy extends AbstractGwtController implements CommodityService {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	@Qualifier("commodityServiceImpl")
	private CommodityService commodityService;

	@Override
	public List<CommodityDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return commodityService.getAll(businessID);
	}

	@Override
	public Long add(CommodityDTO paymentTypeDTO) throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException {
		return commodityService.add(paymentTypeDTO);
	}

	@Override
	public void update(CommodityDTO commodityDTO) throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException {
		commodityService.update(commodityDTO);
	}

	@Override
	public void remove(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException {
		commodityService.remove(businessID, id);
	}
	
	public PageDTO<CommodityDTO> searchCommodities(Long businessID, String query, int start, int offset)
			throws InvalidArgumentException, NotAuthenticatedException, DataAccessException {
		return commodityService.searchCommodities(businessID, query, start, offset);
	}

}
