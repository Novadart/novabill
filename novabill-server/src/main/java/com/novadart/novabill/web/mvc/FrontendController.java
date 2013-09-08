package com.novadart.novabill.web.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FrontendController {

	public static enum PAGES {
		HOME, PRICES, BLOG, ABOUT, CONTACT
	}
	
	@RequestMapping(value = {"/", "home", "index"}, method = RequestMethod.GET)
	public String index(Model model){
		model.addAttribute("activePage", PAGES.HOME);
		return "frontend.index";
	}

	@RequestMapping(value = "/contact", method = RequestMethod.GET)
	public String contact(Model model){
		model.addAttribute("activePage", PAGES.CONTACT);
		model.addAttribute("pageName", "Contact Us");
		return "frontend.contact";
	}
	
	@RequestMapping(value = "/about", method = RequestMethod.GET)
	public String about(Model model){
		model.addAttribute("activePage", PAGES.ABOUT);
		model.addAttribute("pageName", "About");
		return "frontend.about";
	}

	@RequestMapping(value = "/prices", method = RequestMethod.GET)
	public String prices(Model model){
		model.addAttribute("activePage", PAGES.PRICES);
		model.addAttribute("pageName", "Prices");
		return "frontend.prices";
	}
	
	
}
