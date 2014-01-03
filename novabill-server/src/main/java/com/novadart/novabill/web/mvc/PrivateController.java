package com.novadart.novabill.web.mvc;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.security.Principal;

@Controller
public class PrivateController {
	
	private final ObjectMapper mapper = new ObjectMapper();
	
	public static enum PAGES {
		DASHBOARD, CLIENTS, COMMODITIES, PRICE_LISTS, PAYMENTS, SETTINGS, INVOICES, ESTIMATIONS, TRANSPORT_DOCUMENTS, CREDIT_NOTES
	}


	@RequestMapping(value = Urls.PRIVATE_HOME, method = RequestMethod.GET)
	public ModelAndView dashboard(){
		ModelAndView mav = new ModelAndView("private.dashboard");
		mav.addObject("activePage", PAGES.DASHBOARD);
		return mav;
	}
	
	@RequestMapping(value = Urls.PRIVATE_CLIENTS, method = RequestMethod.GET)
	public ModelAndView clients() throws JsonGenerationException, JsonMappingException, IOException{
		ModelAndView mav = new ModelAndView("private.clients");
		mav.addObject("activePage", PAGES.CLIENTS);
		
		Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Business business = principal.getBusiness();
		mav.addObject("invoiceYears", mapper.writeValueAsString(business.getInvoiceYears()));
		mav.addObject("estimationYears", mapper.writeValueAsString(business.getEstimationYears()));
		mav.addObject("creditNoteYears", mapper.writeValueAsString(business.getCreditNoteYears()));
		mav.addObject("transportDocumentYears", mapper.writeValueAsString(business.getTransportDocumentYears()));
		return mav;
	}
	
	@RequestMapping(value = Urls.PRIVATE_DOCS_INVOICES, method = RequestMethod.GET)
	public ModelAndView invoices() throws JsonGenerationException, JsonMappingException, IOException{
		ModelAndView mav = new ModelAndView("private.invoices");
		mav.addObject("activePage", PAGES.INVOICES);
		Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Business business = principal.getBusiness();
		mav.addObject("invoiceYears", mapper.writeValueAsString(business.getInvoiceYears()));
		return mav;
	}
	
	@RequestMapping(value = Urls.PRIVATE_DOCS_ESTIMATIONS, method = RequestMethod.GET)
	public ModelAndView estimations() throws JsonGenerationException, JsonMappingException, IOException{
		ModelAndView mav = new ModelAndView("private.estimations");
		mav.addObject("activePage", PAGES.ESTIMATIONS);
		Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Business business = principal.getBusiness();
		mav.addObject("estimationYears", mapper.writeValueAsString(business.getEstimationYears()));
		return mav;
	}
	
	@RequestMapping(value = Urls.PRIVATE_DOCS_TRANSPORT_DOCUMENTS, method = RequestMethod.GET)
	public ModelAndView transportDocuments() throws JsonGenerationException, JsonMappingException, IOException{
		ModelAndView mav = new ModelAndView("private.transportDocuments");
		mav.addObject("activePage", PAGES.TRANSPORT_DOCUMENTS);
		Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Business business = principal.getBusiness();
		mav.addObject("transportDocumentYears", mapper.writeValueAsString(business.getTransportDocumentYears()));
		return mav;
	}
	
	@RequestMapping(value = Urls.PRIVATE_DOCS_CREDIT_NOTES, method = RequestMethod.GET)
	public ModelAndView creditNotes() throws JsonGenerationException, JsonMappingException, IOException{
		ModelAndView mav = new ModelAndView("private.creditNotes");
		mav.addObject("activePage", PAGES.CREDIT_NOTES);
		Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Business business = principal.getBusiness();
		mav.addObject("creditNoteYears", mapper.writeValueAsString(business.getCreditNoteYears()));
		return mav;
	}
	
	@RequestMapping(value = Urls.PRIVATE_COMMODITIES, method = RequestMethod.GET)
	public ModelAndView commodities(){
		ModelAndView mav = new ModelAndView("private.commodities");
		mav.addObject("activePage", PAGES.COMMODITIES);
		return mav;
	}
	
	@RequestMapping(value = Urls.PRIVATE_PRICE_LISTS, method = RequestMethod.GET)
	public ModelAndView priceLists(){
		ModelAndView mav = new ModelAndView("private.priceLists");
		mav.addObject("activePage", PAGES.PRICE_LISTS);
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
