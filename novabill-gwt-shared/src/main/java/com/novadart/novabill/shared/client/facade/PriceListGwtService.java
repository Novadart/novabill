package com.novadart.novabill.shared.client.facade;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.XsrfProtect;
import com.novadart.novabill.shared.client.dto.PriceDTO;
import com.novadart.novabill.shared.client.dto.PriceListDTO;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.tuple.Pair;

@XsrfProtect
@RemoteServiceRelativePath("pricelist.rpc")
public interface PriceListGwtService extends RemoteService {
	
	public List<PriceListDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public PriceListDTO get(Long id) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException, FreeUserAccessForbiddenException;
	
	public Long add(PriceListDTO priceListDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException;
	
	public void update(PriceListDTO priceListDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException;
	
	public void remove(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException, DataIntegrityException, FreeUserAccessForbiddenException;
	
	public Map<String, Pair<String, PriceDTO>> getPrices(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException;
	
	public Long clonePriceList(Long businessID, Long id, String priceListName) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException, ValidationException, FreeUserAccessForbiddenException;

}
