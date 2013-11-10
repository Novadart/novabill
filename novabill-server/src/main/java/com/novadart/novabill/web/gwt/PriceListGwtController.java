package com.novadart.novabill.web.gwt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.novadart.novabill.annotation.HandleGWTServiceAccessDenied;
import com.novadart.novabill.service.web.PriceListService;
import com.novadart.novabill.shared.client.dto.PriceListDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.PriceListGwtService;

@HandleGWTServiceAccessDenied
public class PriceListGwtController extends AbstractGwtController implements PriceListGwtService {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private PriceListService priceListService;

	@Override
	public List<PriceListDTO> getAll(Long businessID) throws NotAuthenticatedException, DataAccessException {
		return priceListService.getAll(businessID);
	}

	@Override
	public PriceListDTO get(Long id) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException {
		return priceListService.get(id);
	}

	@Override
	public Long add(PriceListDTO priceListDTO) throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException {
		return priceListService.add(priceListDTO);
	}

	@Override
	public void update(PriceListDTO priceListDTO) throws NotAuthenticatedException, ValidationException, AuthorizationException, DataAccessException, NoSuchObjectException {
		priceListService.update(priceListDTO);
	}

	@Override
	public void remove(Long businessID, Long id) throws NotAuthenticatedException, DataAccessException {
		priceListService.remove(businessID, id);
	}

}
