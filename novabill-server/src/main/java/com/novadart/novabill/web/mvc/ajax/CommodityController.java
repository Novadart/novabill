package com.novadart.novabill.web.mvc.ajax;

import com.google.common.collect.ImmutableMap;
import com.novadart.novabill.annotation.RestExceptionProcessingMixin;
import com.novadart.novabill.service.web.CommodityService;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.PriceDTO;
import com.novadart.novabill.shared.client.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RestExceptionProcessingMixin
@RequestMapping("/private/ajax/businesses/{businessID}/commodities")
public class CommodityController {

	@Autowired
	private CommodityService commodityService;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<CommodityDTO> getCommodities(@PathVariable Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return commodityService.getAll(businessID);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public CommodityDTO get(@PathVariable Long businessID, @PathVariable Long id) throws NoSuchObjectException, NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		CommodityDTO commodityDTO = commodityService.get(businessID, id);
		commodityDTO.setPrices(commodityService.getPrices(businessID, id));
		return commodityDTO;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public CommodityDTO add(@RequestBody CommodityDTO commodityDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
		Long id  = commodityService.add(commodityDTO);
		CommodityDTO commodity = new CommodityDTO();
		commodity.setId(id);
		return commodity;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void update(@RequestBody CommodityDTO commodityDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
		commodityService.update(commodityDTO);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void remove(@PathVariable Long businessID, @PathVariable Long id) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		commodityService.remove(businessID, id);
	}
	
	@RequestMapping(value = "/{commodityID}/pricelists/{priceListID}/prices", method = {RequestMethod.PUT, RequestMethod.POST})
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String, Object> addOrUpdatePrice(@PathVariable Long businessID, @RequestBody PriceDTO priceDTO) throws ValidationException, FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException {
		return ImmutableMap.of(JsonConst.VALUE, commodityService.addOrUpdatePrice(businessID, priceDTO));
	}
	
	@RequestMapping(value = "/{commodityID}/pricelists/{priceListID}/prices", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void removePrice(@PathVariable Long businessID, @PathVariable Long priceListID, @PathVariable Long commodityID) throws FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException {
		commodityService.removePrice(businessID, priceListID, commodityID);
	}
	
	@RequestMapping(value = "/prices/batch", method = {RequestMethod.PUT, RequestMethod.POST})
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<String> addOrUpdatePrices(@PathVariable Long businessID, @RequestBody List<PriceDTO> priceDTOs) throws ValidationException, FreeUserAccessForbiddenException, NotAuthenticatedException, DataAccessException {
		List<String> ids = new ArrayList<>(priceDTOs.size());
		for(PriceDTO priceDTO: priceDTOs)
			ids.add(commodityService.addOrUpdatePrice(businessID, priceDTO).toString());
		return ids;
	}
	
}
