package com.novadart.novabill.web.gwt;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.novadart.novabill.annotation.HandleGWTServiceAccessDenied;
import com.novadart.novabill.service.web.CommodityService;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.PriceDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.InvalidArgumentException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.CommodityGwtService;

@HandleGWTServiceAccessDenied
public class CommodityGwtController extends AbstractGwtController implements CommodityGwtService {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private CommodityService commodityService;

	@Override
	public List<CommodityDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return commodityService.getAll(businessID);
	}

	@Override
	public Long add(CommodityDTO commodityDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException {
		return commodityService.add(commodityDTO);
	}

	@Override
	public void update(CommodityDTO commodityDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException {
		commodityService.update(commodityDTO);
	}

	@Override
	public void remove(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		commodityService.remove(businessID, id);
	}

	public PageDTO<CommodityDTO> searchCommodities(Long businessID, String query, int start, int length) throws InvalidArgumentException, NotAuthenticatedException, DataAccessException {
		return commodityService.searchCommodities(businessID, query, start, length);
	}

	@Override
	public CommodityDTO get(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		CommodityDTO commodityDTO = commodityService.get(businessID, id);
		commodityDTO.setPrices(commodityService.getPrices(businessID, id));
		return commodityDTO;
	}
	
	@Override
	public Long addOrUpdatePrice(Long businessID, PriceDTO priceDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException {
		return commodityService.addOrUpdatePrice(businessID, priceDTO);
	}

	@Override
	public void removePrice(Long businessID, Long priceListID, Long commodityID) throws NotAuthenticatedException, DataAccessException, UnsupportedOperationException, FreeUserAccessForbiddenException {
		commodityService.removePrice(businessID, priceListID, commodityID);
	}
	
	@Override
	public List<Long> addOrUpdatePrices(Long businessID, List<PriceDTO> priceDTOs) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException {
		List<Long> ids = new ArrayList<>(priceDTOs.size());
		for(PriceDTO priceDTO: priceDTOs)
			ids.add(commodityService.addOrUpdatePrice(businessID, priceDTO));
		return ids;
	}

}
