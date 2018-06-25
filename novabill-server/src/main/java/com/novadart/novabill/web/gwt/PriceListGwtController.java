package com.novadart.novabill.web.gwt;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.novadart.novabill.annotation.HandleGWTServiceAccessDenied;
import com.novadart.novabill.service.web.PriceListService;
import com.novadart.novabill.shared.client.dto.PriceDTO;
import com.novadart.novabill.shared.client.dto.PriceListDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.PriceListGwtService;
import com.novadart.novabill.shared.client.tuple.Pair;

@HandleGWTServiceAccessDenied
public class PriceListGwtController extends AbstractGwtController implements PriceListGwtService {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private PriceListService priceListService;

	@Override
	public List<PriceListDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return priceListService.getAll(businessID);
	}

	@Override
	public PriceListDTO get(Long id) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException, FreeUserAccessForbiddenException {
		return priceListService.get(id);
	}

	@Override
	public Long add(PriceListDTO priceListDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException {
		return priceListService.add(priceListDTO);
	}

	@Override
	public void update(PriceListDTO priceListDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException {
		priceListService.update(priceListDTO);
	}

	@Override
	public void remove(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException, DataIntegrityException, FreeUserAccessForbiddenException {
		priceListService.remove(businessID, id);
	}

	@Override
	public Map<String, Pair<String, PriceDTO>> getPrices(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return priceListService.getPrices(businessID, id);
	}

	@Override
	public Long clonePriceList(Long businessID, Long id, String priceListName) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException, ValidationException, FreeUserAccessForbiddenException {
		return priceListService.clonePriceList(businessID, id, priceListName);
	}


}
