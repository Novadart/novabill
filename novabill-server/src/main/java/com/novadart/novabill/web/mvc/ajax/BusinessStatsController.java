package com.novadart.novabill.web.mvc.ajax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.novadart.novabill.annotation.RestExceptionProcessingMixin;
import com.novadart.novabill.service.web.BusinessStatsService;
import com.novadart.novabill.shared.client.dto.BIClientStatsDTO;
import com.novadart.novabill.shared.client.dto.BICommodityStatsDTO;
import com.novadart.novabill.shared.client.dto.BIGeneralStatsDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;

@Controller
@RestExceptionProcessingMixin
@RequestMapping("/private/ajax/businesses/{businessID}/bizintel")
public class BusinessStatsController {
	
	@Autowired
	private BusinessStatsService businessStatsService;
	
	
	@RequestMapping(value = "/genstats/{year}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public BIGeneralStatsDTO getGeneralBIStats(@PathVariable Long businessID, @PathVariable Integer year) throws NotAuthenticatedException, DataAccessException {
		return businessStatsService.getGeneralBIStats(businessID, year);
	}
	
	@RequestMapping(value = "/clientstats/{clientID}/{year}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public BIClientStatsDTO getClientBIStats(@PathVariable Long businessID, @PathVariable Long clientID, @PathVariable Integer year) throws NotAuthenticatedException, DataAccessException{
		return businessStatsService.getClientBIStats(businessID, clientID, year);
	}
	
	@RequestMapping(value = "/commoditystats/{year}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public BICommodityStatsDTO getCommodityBIStats(@PathVariable Long businessID, @PathVariable Integer year, @RequestParam String sku){
		return businessStatsService.getCommodityBIStats(businessID, sku, year);
	}
	
	
}
