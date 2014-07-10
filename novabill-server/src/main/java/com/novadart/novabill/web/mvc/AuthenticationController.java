package com.novadart.novabill.web.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.novadart.novabill.domain.Registration;


@Controller
public class AuthenticationController {
	
	@RequestMapping(value = Urls.PUBLIC_LOGIN, method = RequestMethod.GET)
	public String register(Model model){
		Registration registration = new Registration();
		model.addAttribute("registration", registration);
		model.addAttribute("pageName", "Login");
		return "frontend.login";
	}

}
