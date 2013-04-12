package com.novadart.novabill.web.mvc;

import java.io.StringWriter;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.novadart.novabill.shared.client.dto.BusinessDTO;

@Controller
public class DemoController {
	
	private static final ObjectMapper jsonSerializer = new ObjectMapper();
	
	private static final BusinessDTO BUSINESS = new BusinessDTO();
	
	static {
		BUSINESS.setAddress("via Novadart, 123");
		BUSINESS.setCity("Padova");
		BUSINESS.setCountry("IT");
		BUSINESS.setEmail("info@example.org");
		BUSINESS.setFax("049/123456789");
		BUSINESS.setId(0L);
		BUSINESS.setMobile("321 123456789");
		BUSINESS.setName("Soluzioni Semplici S.r.l");
		BUSINESS.setPhone("049/987654321");
		BUSINESS.setPostcode("12345");
		BUSINESS.setPremium(false);
		BUSINESS.setProvince("PD");
		BUSINESS.setSsn("AAABBB12C34D567E");
		BUSINESS.setVatID("IT12345678901");
		BUSINESS.setWeb("www.example.org");
	}
	
	@RequestMapping(value = "/demo", method = RequestMethod.GET)
	public ModelAndView privateArea(){
		ModelAndView mav = new ModelAndView("demo");
		StringWriter sw = new StringWriter();
		try {
			jsonSerializer.writeValue(sw, BUSINESS);
		} catch (Exception e) {
			return null;
		}
		mav.addObject("business", sw.toString());
		return mav;
	}

}
