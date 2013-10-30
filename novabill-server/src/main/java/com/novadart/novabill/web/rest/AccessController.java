package com.novadart.novabill.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/rest/1")
public class AccessController {
	
	@RequestMapping("/authenticate")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void authenticate(){}

}
