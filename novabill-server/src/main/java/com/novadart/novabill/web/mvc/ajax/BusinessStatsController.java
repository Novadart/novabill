package com.novadart.novabill.web.mvc.ajax;

import com.novadart.novabill.annotation.RestExceptionProcessingMixin;
import com.novadart.novabill.service.web.BusinessStatsService;
import com.novadart.novabill.shared.client.dto.BIClientStatsDTO;
import com.novadart.novabill.shared.client.dto.BICommodityStatsDTO;
import com.novadart.novabill.shared.client.dto.BIGeneralStatsDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestExceptionProcessingMixin
@RequestMapping("/private/ajax/businesses/{businessID}/bizintel")
public class BusinessStatsController {
	
	@Autowired
	private BusinessStatsService businessStatsService;
	
	
	@RequestMapping(value = "/genstats/{year}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public BIGeneralStatsDTO getGeneralBIStats(@PathVariable Long businessID, @PathVariable Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return businessStatsService.getGeneralBIStats(businessID, year);
	}
	
	@RequestMapping(value = "/clientstats/{clientID}/{year}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public BIClientStatsDTO getClientBIStats(@PathVariable Long businessID, @PathVariable Long clientID, @PathVariable Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException, NoSuchObjectException{
		return businessStatsService.getClientBIStats(businessID, clientID, year);
	}
	
	@RequestMapping(value = "/commoditystats/{commodityID}/{year}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public BICommodityStatsDTO getCommodityBIStats(@PathVariable Long businessID, @PathVariable Long commodityID, @PathVariable Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException{
		return businessStatsService.getCommodityBIStats(businessID, commodityID, year);
	}
	
	
}
