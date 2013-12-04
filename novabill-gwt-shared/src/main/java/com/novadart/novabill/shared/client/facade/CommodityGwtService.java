package com.novadart.novabill.shared.client.facade;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.XsrfProtect;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.PriceDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.InvalidArgumentException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@XsrfProtect
@RemoteServiceRelativePath("commodity.rpc")
public interface CommodityGwtService extends RemoteService {
	
	public List<CommodityDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException;
	
	public CommodityDTO get(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException;
	
	public Long add(CommodityDTO commodityDTO) throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException;
	
	public void update(CommodityDTO commodityDTO) throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException;
	
	public void remove(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException;
	
	public PageDTO<CommodityDTO> searchCommodities(Long businessID, String query, int start, int offset) throws InvalidArgumentException, NotAuthenticatedException, DataAccessException;
	
	public Long addOrUpdatePrice(Long businessID, PriceDTO priceDTO) throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException;
	
	public void removePrice(Long businessID, Long priceListID, Long commodityID) throws NotAuthenticatedException, DataAccessException;
	
}
