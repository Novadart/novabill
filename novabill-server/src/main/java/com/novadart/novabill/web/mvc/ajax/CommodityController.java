package com.novadart.novabill.web.mvc.ajax;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.novadart.novabill.annotation.RestExceptionProcessingMixin;
import com.novadart.novabill.service.web.CommodityService;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.PriceDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@Controller
@RestExceptionProcessingMixin
@RequestMapping("/private/ajax/businesses/{businessID}/commodities")
public class CommodityController {

	@Autowired
	private CommodityService commodityService;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<CommodityDTO> getCommodities(@PathVariable Long businessID) throws NotAuthenticatedException, DataAccessException {
		return commodityService.getAll(businessID);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public CommodityDTO get(@PathVariable Long businessID, @PathVariable Long id) throws NoSuchObjectException, NotAuthenticatedException, DataAccessException{
		CommodityDTO commodityDTO = commodityService.get(businessID, id);
		commodityDTO.setPrices(commodityService.getPrices(businessID, id));
		return commodityDTO;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Long add(@RequestBody CommodityDTO commodityDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
		return commodityService.add(commodityDTO);
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void update(@RequestBody CommodityDTO commodityDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
		commodityService.update(commodityDTO);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void remove(@PathVariable Long businessID, @PathVariable Long id) throws NotAuthenticatedException, DataAccessException{
		commodityService.remove(businessID, id);
	}
	
	@RequestMapping(value = "/{commodityID}/pricelists/{priceListID}/prices", method = {RequestMethod.PUT, RequestMethod.POST})
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Long addOrUpdatePrice(@PathVariable Long businessID, @RequestBody PriceDTO priceDTO) throws ValidationException{
		return commodityService.addOrUpdatePrice(businessID, priceDTO);
	}
	
	@RequestMapping(value = "/{commodityID}/pricelists/{priceListID}/prices", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void removePrice(@PathVariable Long businessID, @PathVariable Long priceListID, @PathVariable Long commodityID){
		commodityService.removePrice(businessID, priceListID, commodityID);
	}
	
	@RequestMapping(value = "/{commodityID}/pricelists/{priceListID}/prices/batch", method = {RequestMethod.PUT, RequestMethod.POST})
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<Long> addOrUpdatePrices(@PathVariable Long businessID, @RequestBody List<PriceDTO> priceDTOs) throws ValidationException{
		List<Long> ids = new ArrayList<>(priceDTOs.size());
		for(PriceDTO priceDTO: priceDTOs)
			ids.add(commodityService.addOrUpdatePrice(businessID, priceDTO));
		return ids;
	}
	
}
