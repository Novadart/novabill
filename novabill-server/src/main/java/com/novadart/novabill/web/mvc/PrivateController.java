package com.novadart.novabill.web.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/private")
@Controller
public class PrivateController {


	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView dashboard(){
		ModelAndView mav = new ModelAndView("private.dashboard");
		return mav;
	}
	
	@RequestMapping(value = "/firstrun/", method = RequestMethod.GET)
	public ModelAndView firstRun(){
		ModelAndView mav = new ModelAndView("private.firstrun");
		return mav;
	}

}
