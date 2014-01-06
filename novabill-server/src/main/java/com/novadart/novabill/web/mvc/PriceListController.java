package com.novadart.novabill.web.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novadart.novabill.service.web.PriceListService;
import com.novadart.novabill.shared.client.dto.PriceListDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;

@Controller
@RequestMapping("/private/pricelists")
public class PriceListController {
	
	@Autowired
	private PriceListService priceListService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = "application/json")
	@ResponseBody
	public PriceListDTO get(@PathVariable Long id) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException{
		return priceListService.get(id);
	}

}
