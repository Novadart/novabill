package com.novadart.novabill.web.mvc;

import com.novadart.novabill.domain.Client;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.web.PriceListService;
import com.novadart.novabill.shared.client.dto.PriceListDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/private/json/")
@Deprecated
public class JsonServicesController {
	
	/**
	 * TODO To be removed together with GWT  
	 */
	
	@Autowired
	private PriceListService priceListService;
	
	@Autowired
	private UtilsService utilsService;
	
	@RequestMapping(method = RequestMethod.GET, value = "pricelists/{id}", produces = "application/json")
	@ResponseBody
	public PriceListDTO get(@PathVariable Long id) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException, FreeUserAccessForbiddenException {
		return priceListService.get(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "comm-select-data/{clientID}", produces = "application/json")
	@ResponseBody
	public Pair<PriceListDTO, List<PriceListDTO>> fetchSelectCommodityForDocItemOpData(@PathVariable Long clientID) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException, FreeUserAccessForbiddenException {
		return new Pair<>(priceListService.get(Client.findClient(clientID).getDefaultPriceList().getId()),
				priceListService.getAll(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId()));
	}
	
}
