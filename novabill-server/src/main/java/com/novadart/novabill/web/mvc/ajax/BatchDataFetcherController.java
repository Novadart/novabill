package com.novadart.novabill.web.mvc.ajax;

import com.novadart.novabill.annotation.RestExceptionProcessingMixin;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.service.web.PriceListService;
import com.novadart.novabill.shared.client.dto.PriceListDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/private/ajax/businesses/{businessID}")
@RestExceptionProcessingMixin
public class BatchDataFetcherController {

	@Autowired
	private PriceListService priceListService;
	
	@RequestMapping(value = "/commodityselectdata/{clientID}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Pair<PriceListDTO, List<PriceListDTO>> fetchSelectCommodityForDocItemOpData(@PathVariable Long businessID, @PathVariable Long clientID)
			throws NotAuthenticatedException, NoSuchObjectException, DataAccessException {
		return new Pair<>(priceListService.get(Client.findClient(clientID).getDefaultPriceList().getId()), priceListService.getAll(businessID));
	}
	
}
