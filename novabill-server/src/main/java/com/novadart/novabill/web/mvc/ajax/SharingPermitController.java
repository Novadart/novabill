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
import com.novadart.novabill.service.web.SharingPermitService;
import com.novadart.novabill.shared.client.dto.SharingPermitDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@Controller
@RequestMapping("/private/businesses/{businessID}/sharepermits")
@RestExceptionProcessingMixin
public class SharingPermitController {

	@Autowired
	private SharingPermitService sharingPermitService;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<SharingPermitDTO> getAll(@PathVariable Long businessID) throws NotAuthenticatedException, DataAccessException{
		return sharingPermitService.getAll(businessID);
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Long add(@RequestBody SharingPermitDTO sharingPermitDTO) throws ValidationException{
		return sharingPermitService.add(sharingPermitDTO);
	}
	
	@RequestMapping(value = "/remove", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void remove(@PathVariable Long businessID, Long id){
		sharingPermitService.remove(businessID, id);
	}
	
}
