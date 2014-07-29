package com.novadart.novabill.web.mvc.ajax;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.novadart.novabill.annotation.MailMixin;
import com.novadart.novabill.annotation.RestExceptionProcessingMixin;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.SimpleValidator;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.web.mvc.ajax.dto.RecommendByMailDTO;

@Controller
@MailMixin
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
	public boolean recommendByEmail(@RequestBody RecommendByMailDTO recommendByMailDTO) throws ValidationException{
		validator.validate(recommendByMailDTO);
		Map<String, Object> templateVars = new HashMap<String, Object>();
		templateVars.put("businessName", Business.findBusiness(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId()).getName());
		return sendMessage(recommendByMailDTO.getTo(), "Try Novabill", templateVars, EMAIL_TEMPLATE_LOCATION, false);
	}

}
