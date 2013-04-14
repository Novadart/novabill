package com.novadart.novabill.web.mvc;

import java.io.StringWriter;
import java.util.concurrent.TimeUnit;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.dto.factory.BusinessDTOFactory;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.shared.client.dto.BusinessDTO;

@Controller
public class PrivateController {
	
	private static final ObjectMapper jsonSerializer = new ObjectMapper();
	
	@Autowired
	private UtilsService utilsService;
	
	@Value("${devMode.enabled}")
	private boolean devMode;
	
	@RequestMapping(value = "/private", method = RequestMethod.GET)
	public ModelAndView privateArea(){
		ModelAndView mav = new ModelAndView("private");
		Business business =  Business.findBusiness(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId());
		BusinessDTO businessDTO = BusinessDTOFactory.toDTO(business);
		StringWriter sw = new StringWriter();
		try {
			jsonSerializer.writeValue(sw, businessDTO);
		} catch (Exception e) {
			return null;
		}
		mav.addObject("business", sw.toString());
		mav.addObject("daysToExpiration", business.getNonFreeExpirationDelta(TimeUnit.DAYS));
		mav.addObject("notesBitMask", Principal.findPrincipal(utilsService.getAuthenticatedPrincipalDetails().getId()).getNotesBitMask());
		mav.addObject("devMode", devMode);
		return mav;
	}

}
