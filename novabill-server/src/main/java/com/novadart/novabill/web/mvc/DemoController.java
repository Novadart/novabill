package com.novadart.novabill.web.mvc;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.servlet.ModelAndView;

import com.novadart.novabill.shared.client.dto.BusinessDTO;

@Controller
@RequestMapping("/demo")
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
	
	private ServletContextResource noLogoImage;
	
	@Value("${devMode.enabled}")
	private boolean devMode;
	
	@Autowired
	public void setServletContext(ServletContext servletContext){
		noLogoImage = new ServletContextResource(servletContext, "/images/no_logo.gif");
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView privateArea(){
		ModelAndView mav = new ModelAndView("demo");
		StringWriter sw = new StringWriter();
		try {
			jsonSerializer.writeValue(sw, BUSINESS);
		} catch (Exception e) {
			return null;
		}
		mav.addObject("business", sw.toString());
		mav.addObject("devMode", devMode);
		return mav;
	}
	
	@RequestMapping(value = "/logo", method = RequestMethod.GET)
	@ResponseBody
	public void getLogo(HttpServletResponse response) throws IOException{
		InputStream is = noLogoImage.getInputStream();
		response.setContentType("image/" + FilenameUtils.getExtension(noLogoImage.getPath()));
		response.setHeader ("Content-Disposition", String.format("attachment; filename=\"%s\"",FilenameUtils.getName(noLogoImage.getPath())));
		IOUtils.copy(is, response.getOutputStream());
	}

}
