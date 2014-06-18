package com.novadart.novabill.web.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FrontendController {

	public static enum PAGES {
		HOME, PRICES, ABOUT, SECURITY
	}
	
	@RequestMapping(value = Urls.PUBLIC_HOME, method = RequestMethod.GET)
	public String index(Model model){
		model.addAttribute("activePage", PAGES.HOME);
		return "frontend.index";
	}

	@RequestMapping(value = Urls.PUBLIC_ABOUT, method = RequestMethod.GET)
	public String about(Model model){
		model.addAttribute("activePage", PAGES.ABOUT);
		model.addAttribute("pageName", "Chi Siamo");
		return "frontend.about";
	}

	@RequestMapping(value = Urls.PUBLIC_PRICES, method = RequestMethod.GET)
	public String prices(Model model){
		model.addAttribute("activePage", PAGES.PRICES);
		model.addAttribute("pageName", "Quanto Costa?");
		return "frontend.prices";
	}
	
	@RequestMapping(value = Urls.PUBLIC_PAGE_NOT_FOUND, method = RequestMethod.GET)
	public String pageNotFound(Model model){
		model.addAttribute("pageName", "Risorsa non Disponibile");
		return "frontend.pageNotFound";
	}
	
	@RequestMapping(value = Urls.PUBLIC_EXCEPTION, method = RequestMethod.GET)
	public String exception(Model model){
		model.addAttribute("pageName", "Oops");
		return "frontend.oops";
	}
	
	@RequestMapping(value = Urls.PUBLIC_TOS, method = RequestMethod.GET)
	public String tos(Model model){
		model.addAttribute("pageName", "Termini e Condizioni");
		return "frontend.tos";
	}
	
	@RequestMapping(value = Urls.PUBLIC_TOS_MINIMAL, method = RequestMethod.GET)
	public String tosMinimal(Model model){
		return "frontend.tosMinimal";
	}
	
	@RequestMapping(value = Urls.PUBLIC_PAYPAL_RETURN_PAGE, method = RequestMethod.GET)
	public String paypalreturnPage(Model model){
		model.addAttribute("pageName", "Attivazione Premium");
		return "frontend.paypalReturnPage";
	}
	
	
}
