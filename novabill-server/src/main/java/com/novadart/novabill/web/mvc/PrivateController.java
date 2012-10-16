package com.novadart.novabill.web.mvc;

import java.io.StringWriter;
import java.util.concurrent.TimeUnit;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.dto.factory.BusinessDTOFactory;
import com.novadart.novabill.domain.security.PrincipalDetails;
import com.novadart.novabill.shared.client.dto.BusinessDTO;

@Controller
public class PrivateController {
	
	private static final ObjectMapper jsonSerializer = new ObjectMapper();
	
	@RequestMapping(value = "/private", method = RequestMethod.GET)
	public ModelAndView privateArea(){
		ModelAndView mav = new ModelAndView("private");
		Business business = Business.findBusiness(((PrincipalDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPrincipal().getId());
		BusinessDTO businessDTO = BusinessDTOFactory.toDTO(business);
		StringWriter sw = new StringWriter();
		try {
			jsonSerializer.writeValue(sw, businessDTO);
		} catch (Exception e) {
			return null;
		}
		mav.addObject("business", sw.toString());
		mav.addObject("daysToExpiration", business.getNonFreeExpirationDelta(TimeUnit.DAYS));
		return mav;
	}

}
