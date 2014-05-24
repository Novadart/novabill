package com.novadart.novabill.web.mvc.ajax;

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
import com.novadart.novabill.service.web.TransporterService;
import com.novadart.novabill.shared.client.dto.TransporterDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@Controller
@RestExceptionProcessingMixin
@RequestMapping("/private/ajax/businesses/{businessID}/transporters")
public class TransporterController {

	@Autowired
	private TransporterService transporterService;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<TransporterDTO> getAll(@PathVariable Long businessID) throws NotAuthenticatedException, DataAccessException {
		return transporterService.getAll(businessID);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public TransporterDTO get(@PathVariable Long id) throws NotAuthenticatedException, NoSuchObjectException, DataAccessException{
		return transporterService.get(id);
	}
	
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public TransporterDTO add(@RequestBody TransporterDTO transporterDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException{
		Long transporterId = transporterService.add(transporterDTO);
		TransporterDTO result = new TransporterDTO();
		result.setId(transporterId);
		return result;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void update(@RequestBody TransporterDTO transporterDTO) throws NotAuthenticatedException, ValidationException, FreeUserAccessForbiddenException, DataAccessException, NoSuchObjectException{
		transporterService.update(transporterDTO);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void remove(@PathVariable Long businessID, @PathVariable Long id) throws NotAuthenticatedException, DataAccessException, DataIntegrityException{
		transporterService.remove(businessID, id);
	}
	
}
