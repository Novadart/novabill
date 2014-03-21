package com.novadart.novabill.web.mvc.ajax;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.novadart.novabill.service.web.SharingPermitService;
import com.novadart.novabill.shared.client.dto.SharingPermitDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@Controller
@RequestMapping("/private/businesses/{businessID}/sharepermits")
public class SharingPermitController {

	@Autowired
	private SharingPermitService sharingPermitService;
	
	@RequestMapping(method = RequestMethod.GET)
	public List<SharingPermitDTO> getAll(@PathVariable Long businessID) throws NotAuthenticatedException, DataAccessException{
		return sharingPermitService.getAll(businessID);
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Long add(@RequestBody SharingPermitDTO sharingPermitDTO) throws ValidationException{
		return sharingPermitService.add(sharingPermitDTO);
	}
	
	@RequestMapping(value = "/remove", method = RequestMethod.DELETE)
	public void remove(@PathVariable Long businessID, Long id){
		sharingPermitService.remove(businessID, id);
	}
	
}
