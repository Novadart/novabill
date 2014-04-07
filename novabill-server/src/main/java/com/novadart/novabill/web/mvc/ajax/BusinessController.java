package com.novadart.novabill.web.mvc.ajax;

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
import com.novadart.novabill.service.web.BusinessService;
import com.novadart.novabill.shared.client.data.LayoutType;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@Controller
@RestExceptionProcessingMixin
@RequestMapping("/private/ajax/businesses")
public class BusinessController {
	
	@Autowired
	private BusinessService businessService;
	
	@RequestMapping(value = "/{businessID}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public BusinessDTO get(@PathVariable Long businessID) throws NotAuthenticatedException, DataAccessException{
		return businessService.get(businessID);
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void update(@RequestBody BusinessDTO businessDTO) throws NotAuthenticatedException, NoSuchObjectException, ValidationException, DataAccessException{
		businessService.update(businessDTO);
	}

	@RequestMapping(value = "/{businessID}/defaulttemplate/{layoutType}")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void setDefaultLayout(@PathVariable Long businessID, @PathVariable LayoutType layoutType) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException{
		businessService.setDefaultLayout(businessID, layoutType);
	}
	
}
