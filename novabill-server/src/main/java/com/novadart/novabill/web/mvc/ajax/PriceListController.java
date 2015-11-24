package com.novadart.novabill.web.mvc.ajax;

import com.novadart.novabill.annotation.RestExceptionProcessingMixin;
import com.novadart.novabill.service.web.PriceListService;
import com.novadart.novabill.shared.client.dto.PriceDTO;
import com.novadart.novabill.shared.client.dto.PriceListDTO;
import com.novadart.novabill.shared.client.exception.*;
import com.novadart.novabill.shared.client.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RestExceptionProcessingMixin
@RequestMapping("/private/ajax/businesses/{businessID}/pricelists")
public class PriceListController {

	@Autowired
	private PriceListService priceListService;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<PriceListDTO> getAll(@PathVariable Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return priceListService.getAll(businessID);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public PriceListDTO get(@PathVariable Long id) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException, FreeUserAccessForbiddenException {
		return priceListService.get(id);
	}
	
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public PriceListDTO add(@RequestBody PriceListDTO priceListDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException{
		Long priceListId = priceListService.add(priceListDTO);
		PriceListDTO priceList = new PriceListDTO();
		priceList.setId(priceListId);
		return priceList;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void update(@RequestBody PriceListDTO priceListDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
		priceListService.update(priceListDTO);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void remove(@PathVariable Long businessID, @PathVariable Long id) throws NotAuthenticatedException, DataAccessException, DataIntegrityException, FreeUserAccessForbiddenException {
		priceListService.remove(businessID, id);
	}
	
	@RequestMapping(value = "/{id}/prices", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String, Pair<String, PriceDTO>> getPrices(@PathVariable Long businessID, @PathVariable Long id) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return priceListService.getPrices(businessID, id);
	}
	
	@RequestMapping(value = "/{id}/clone", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public PriceListDTO clonePriceList(@PathVariable Long businessID, @PathVariable Long id, @RequestParam String priceListName) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException, ValidationException, FreeUserAccessForbiddenException {
		Long clonedPriceListId = priceListService.clonePriceList(businessID, id, priceListName);
		PriceListDTO priceList = new PriceListDTO();
		priceList.setId(clonedPriceListId);
		return priceList;
	}
	
}
