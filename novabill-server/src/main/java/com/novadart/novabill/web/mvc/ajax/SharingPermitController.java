package com.novadart.novabill.web.mvc.ajax;

import com.novadart.novabill.annotation.MailMixin;
import com.novadart.novabill.annotation.RestExceptionProcessingMixin;
import com.novadart.novabill.annotation.Restrictions;
import com.novadart.novabill.authorization.PremiumChecker;
import com.novadart.novabill.domain.SharingPermit;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.web.SharingPermitService;
import com.novadart.novabill.shared.client.dto.SharingPermitDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping("/private/ajax/businesses/{businessID}/sharepermits")
@RestExceptionProcessingMixin
@MailMixin
public class SharingPermitController {

	private static final String EMAIL_TEMPLATE_LOCATION = "mail-templates/sharing-permit-notification.vm";

	@Value("${sharing.request.url}")
	private String sharingRequestUrl;

	@Autowired
	private SharingPermitService sharingPermitService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UtilsService utilsService;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<SharingPermitDTO> getAll(@PathVariable Long businessID) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return sharingPermitService.getAll(businessID);
	}

	private void sendMessage(String email, Long businessID, Locale locale){
		Map<String, Object> templateVars = new HashMap<String, Object>();
		templateVars.put("shareRequestUrl", sharingRequestUrl);
		Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		templateVars.put("businessName", principal.getBusiness().getName());
		sendMessage(email, messageSource.getMessage("sharing.permit.notification", null, locale), templateVars, EMAIL_TEMPLATE_LOCATION);
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	@Restrictions(checkers = {PremiumChecker.class})
	public Long add(@PathVariable Long businessID, @RequestParam(required=false, defaultValue="false") boolean sendEmail, @RequestBody SharingPermitDTO sharingPermitDTO,
			Locale locale) throws ValidationException, NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		Long id = sharingPermitService.add(businessID, sharingPermitDTO);
		if(sendEmail)
			sendMessage(sharingPermitDTO.getEmail(), businessID, locale);
		return id;
	}

	@RequestMapping(value = "/{id}/email", method = RequestMethod.POST)
	@ResponseBody
	@Restrictions(checkers = {PremiumChecker.class})
	public void sendEmail(@PathVariable Long id, Locale locale) throws  NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		SharingPermit sharingPermit = SharingPermit.findSharingPermit(id);
		if(!sharingPermit.getBusiness().getId().equals(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId()))
			throw new DataAccessException();
		sendMessage(sharingPermit.getEmail(), sharingPermit.getBusiness().getId(), locale);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void remove(@PathVariable Long businessID, @PathVariable Long id) throws  NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		sharingPermitService.remove(businessID, id);
	}

}
