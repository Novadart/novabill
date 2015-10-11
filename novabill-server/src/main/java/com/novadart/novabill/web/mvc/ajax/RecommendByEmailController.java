package com.novadart.novabill.web.mvc.ajax;

import com.google.common.collect.ImmutableMap;
import com.novadart.novabill.annotation.RestExceptionProcessingMixin;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.mail.EmailBuilder;
import com.novadart.novabill.service.mail.MailHandlingType;
import com.novadart.novabill.service.validator.SimpleValidator;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.web.mvc.ajax.dto.RecommendByMailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RestExceptionProcessingMixin
@RequestMapping("/private/ajax/recommend")
public class RecommendByEmailController {
	
	private static final String EMAIL_TEMPLATE_LOCATION = "mail-templates/recommend-novabill.vm";
	
	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private SimpleValidator validator;
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String, Object> recommendByEmail(@RequestBody RecommendByMailDTO recommendByMailDTO) throws ValidationException{
		validator.validate(recommendByMailDTO);
		String businessName = Business.findBusiness(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId()).getName();
		boolean emailSent = new EmailBuilder().to(recommendByMailDTO.getTo())
				.subject(businessName + " ti invita a provare Novabill")
				.template(EMAIL_TEMPLATE_LOCATION)
				.templateVar("businessName", businessName)
				.handlingType(MailHandlingType.EXTERNAL_UNACKNOWLEDGED)
				.build().send();
		return ImmutableMap.of(
				JsonConst.VALUE,
				emailSent
		);
	}

}
