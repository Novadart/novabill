package com.novadart.novabill.web.mvc.ajax;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.novadart.novabill.annotation.MailMixin;
import com.novadart.novabill.annotation.RestExceptionProcessingMixin;
import com.novadart.novabill.service.web.SharingPermitService;
import com.novadart.novabill.shared.client.dto.SharingPermitDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@Controller
@RequestMapping("/private/businesses/{businessID}/sharepermits")
@RestExceptionProcessingMixin
@MailMixin
public class SharingPermitController {

	private static final String EMAIL_TEMPLATE_LOCATION = "mail-templates/sharing-notification.vm";
	
	@Value("${sharing.request.url}")
	private String sharingRequestUrl;
	
	@Autowired
	private SharingPermitService sharingPermitService;
	
	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<SharingPermitDTO> getAll(@PathVariable Long businessID) throws NotAuthenticatedException, DataAccessException{
		return sharingPermitService.getAll(businessID);
	}
	
	private void sendMessage(String email, Long businessID, Locale locale){
		Map<String, Object> templateVars = new HashMap<String, Object>();
		templateVars.put("shareRequestUrl", sharingRequestUrl);
		sendMessage(email, messageSource.getMessage("sharing.notification", null, locale), templateVars, EMAIL_TEMPLATE_LOCATION);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	public Long add(@PathVariable Long businessID, @RequestBody SharingPermitDTO sharingPermitDTO, Locale locale) throws ValidationException{
		Long id = sharingPermitService.add(businessID, sharingPermitDTO);
		sendMessage(sharingPermitDTO.getEmail(), businessID, locale);
		return id;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void remove(@PathVariable Long businessID, @PathVariable Long id){
		sharingPermitService.remove(businessID, id);
	}
	
}
