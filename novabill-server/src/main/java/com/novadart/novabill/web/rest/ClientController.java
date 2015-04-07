package com.novadart.novabill.web.rest;

import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.web.BusinessService;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Controller
@RequestMapping("/rest/1/clients")
public class ClientController {

	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private UtilsService utilsService;
	
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<ClientDTO> getAll() throws NotAuthenticatedException, DataAccessException {
		Long businessID = utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId();
		return businessService.getClients(businessID);
	}
	
	
}
