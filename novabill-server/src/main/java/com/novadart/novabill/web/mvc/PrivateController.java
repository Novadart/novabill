package com.novadart.novabill.web.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PrivateController {
	
	public static enum PAGES {
		DASHBOARD, CLIENTS, ITEMS, PAYMENTS, SETTINGS, INVOICES, ESTIMATIONS, TRANSPORT_DOCUMENTS, CREDIT_NOTES
	}


	@RequestMapping(value = Urls.PRIVATE_HOME, method = RequestMethod.GET)
	public ModelAndView dashboard(){
		ModelAndView mav = new ModelAndView("private.dashboard");
		mav.addObject("activePage", PAGES.DASHBOARD);
		return mav;
	}
	
	@RequestMapping(value = Urls.PRIVATE_CLIENTS, method = RequestMethod.GET)
	public ModelAndView clients(){
		ModelAndView mav = new ModelAndView("private.clients");
		mav.addObject("activePage", PAGES.CLIENTS);
		return mav;
	}
	
	@RequestMapping(value = Urls.PRIVATE_DOCS_INVOICES, method = RequestMethod.GET)
	public ModelAndView invoices(){
		ModelAndView mav = new ModelAndView("private.invoices");
		mav.addObject("activePage", PAGES.INVOICES);
		return mav;
	}
	
	@RequestMapping(value = Urls.PRIVATE_DOCS_ESTIMATIONS, method = RequestMethod.GET)
	public ModelAndView estimations(){
		ModelAndView mav = new ModelAndView("private.estimations");
		mav.addObject("activePage", PAGES.ESTIMATIONS);
		return mav;
	}
	
	@RequestMapping(value = Urls.PRIVATE_DOCS_TRANSPORT_DOCUMENTS, method = RequestMethod.GET)
	public ModelAndView transportDocuments(){
		ModelAndView mav = new ModelAndView("private.transportDocuments");
		mav.addObject("activePage", PAGES.TRANSPORT_DOCUMENTS);
		return mav;
	}
	
	@RequestMapping(value = Urls.PRIVATE_DOCS_CREDIT_NOTES, method = RequestMethod.GET)
	public ModelAndView creditNotes(){
		ModelAndView mav = new ModelAndView("private.creditNotes");
		mav.addObject("activePage", PAGES.CREDIT_NOTES);
		return mav;
	}
	
	@RequestMapping(value = Urls.PRIVATE_ITEMS, method = RequestMethod.GET)
	public ModelAndView items(){
		ModelAndView mav = new ModelAndView("private.items");
		mav.addObject("activePage", PAGES.ITEMS);
		return mav;
	}
	
	@RequestMapping(value = Urls.PRIVATE_PAYMENTS, method = RequestMethod.GET)
	public ModelAndView payments(){
		ModelAndView mav = new ModelAndView("private.payments");
		mav.addObject("activePage", PAGES.PAYMENTS);
		return mav;
	}
	
	@RequestMapping(value = Urls.PRIVATE_SETTINGS, method = RequestMethod.GET)
	public ModelAndView settings(){
		ModelAndView mav = new ModelAndView("private.settings");
		mav.addObject("activePage", PAGES.SETTINGS);
		return mav;
	}
	
	@RequestMapping(value = Urls.PRIVATE_FIRST_RUN, method = RequestMethod.GET)
	public ModelAndView firstRun(){
		ModelAndView mav = new ModelAndView("private.firstrun");
		return mav;
	}

}
