package com.novadart.novabill.web.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/exp")
public class ExceptionController {

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public void get(){
		throw new RuntimeException();
	}
	
}
