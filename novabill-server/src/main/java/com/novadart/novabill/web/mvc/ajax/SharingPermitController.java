package com.novadart.novabill.web.mvc.ajax;

import com.google.common.collect.ImmutableMap;
import com.novadart.novabill.annotation.RestExceptionProcessingMixin;
import com.novadart.novabill.annotation.Restrictions;
import com.novadart.novabill.authorization.PremiumChecker;
import com.novadart.novabill.domain.SharingPermit;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.mail.EmailBuilder;
import com.novadart.novabill.service.mail.MailHandlingType;
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

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping("/private/ajax/businesses/{businessID}/sharepermits")
@RestExceptionProcessingMixin
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

	private void sendMessage(String email, Locale locale){
		Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		new EmailBuilder().to(email)
				.subject(messageSource.getMessage("sharing.permit.notification", null, locale))
				.template(EMAIL_TEMPLATE_LOCATION)
				.templateVar("shareRequestUrl", sharingRequestUrl)
				.templateVar("businessName", principal.getBusiness().getName())
				.handlingType(MailHandlingType.EXTERNAL_UNACKNOWLEDGED)
				.build().send();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	@Restrictions(checkers = {PremiumChecker.class})
	public Map<String, Object> add(@PathVariable Long businessID, @RequestParam(required=false, defaultValue="false") boolean sendEmail, @RequestBody SharingPermitDTO sharingPermitDTO,
			Locale locale) throws ValidationException, NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		Long id = sharingPermitService.add(businessID, sharingPermitDTO);
		if(sendEmail)
			sendMessage(sharingPermitDTO.getEmail(), locale);
		return ImmutableMap.of(JsonConst.VALUE, id);
	}

	@RequestMapping(value = "/{id}/email", method = RequestMethod.POST)
	@ResponseBody
	@Restrictions(checkers = {PremiumChecker.class})
	public void sendEmail(@PathVariable Long id, Locale locale) throws  NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		SharingPermit sharingPermit = SharingPermit.findSharingPermit(id);
		if(!sharingPermit.getBusiness().getId().equals(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId()))
			throw new DataAccessException();
		sendMessage(sharingPermit.getEmail(), locale);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void remove(@PathVariable Long businessID, @PathVariable Long id) throws  NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		sharingPermitService.remove(businessID, id);
	}

}
