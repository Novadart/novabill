package com.novadart.novabill.web.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novadart.novabill.domain.security.Principal;
import com.novadart.novabill.service.web.BusinessService;
import com.novadart.novabill.service.web.DocumentIDClassService;
import com.novadart.novabill.shared.client.dto.DocumentIDClassDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PrivateController {
	
	@Autowired
	private BusinessService businessService;

	@Autowired
	private DocumentIDClassService documentIDClassService;

	private final ObjectMapper mapper = new ObjectMapper();
	
	public enum PAGES {
		DASHBOARD, CLIENTS, COMMODITIES, PRICE_LISTS, PAYMENTS, SETTINGS, INVOICES, ESTIMATIONS, TRANSPORT_DOCUMENTS, 
		CREDIT_NOTES, PREMIUM, SHARE, STATISTICS_GENERAL, STATISTICS_CLIENTS, STATISTICS_COMMODITIES 
	}


	@RequestMapping(value = Urls.PRIVATE_HOME, method = RequestMethod.GET)
	public ModelAndView dashboard(){
		ModelAndView mav = new ModelAndView("private.dashboard");
		mav.addObject("activePage", PAGES.DASHBOARD);
		return mav;
	}
	
	@RequestMapping(value = Urls.PRIVATE_CLIENTS, method = RequestMethod.GET)
	public ModelAndView clients() throws IOException, NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		ModelAndView mav = new ModelAndView("private.clients");
		mav.addObject("activePage", PAGES.CLIENTS);
		
		Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long businessID = principal.getBusiness().getId();
		mav.addObject("invoiceYears", mapper.writeValueAsString(businessService.getInvoceYears(businessID)));
		mav.addObject("estimationYears", mapper.writeValueAsString(businessService.getEstimationYears(businessID)));
		mav.addObject("creditNoteYears", mapper.writeValueAsString(businessService.getCreditNoteYears(businessID)));
		mav.addObject("transportDocumentYears", mapper.writeValueAsString(businessService.getTransportDocumentYears(businessID)));

		List<String> suffixes = new ArrayList<>();
		for (DocumentIDClassDTO documentIDClassDTO : documentIDClassService.getAll(principal.getBusiness().getId())) {
			suffixes.add(documentIDClassDTO.getSuffix());
		}

		mav.addObject("invoiceSuffixes", mapper.writeValueAsString(suffixes));

		return mav;
	}
	
	@RequestMapping(value = Urls.PRIVATE_DOCS_INVOICES, method = RequestMethod.GET)
	public ModelAndView invoices() throws IOException, NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		ModelAndView mav = new ModelAndView("private.invoices");
		mav.addObject("activePage", PAGES.INVOICES);
		Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		mav.addObject("invoiceYears", mapper.writeValueAsString(businessService.getInvoceYears(principal.getBusiness().getId())));

		List<String> suffixes = new ArrayList<>();
		for (DocumentIDClassDTO documentIDClassDTO : documentIDClassService.getAll(principal.getBusiness().getId())) {
			suffixes.add(documentIDClassDTO.getSuffix());
		}

		mav.addObject("invoiceSuffixes", mapper.writeValueAsString(suffixes));
		return mav;
	}
	
	@RequestMapping(value = Urls.PRIVATE_DOCS_ESTIMATIONS, method = RequestMethod.GET)
	public ModelAndView estimations() throws IOException, NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		ModelAndView mav = new ModelAndView("private.estimations");
		mav.addObject("activePage", PAGES.ESTIMATIONS);
		Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		mav.addObject("estimationYears", mapper.writeValueAsString(businessService.getEstimationYears(principal.getBusiness().getId())));
		return mav;
	}
	
	@RequestMapping(value = Urls.PRIVATE_DOCS_TRANSPORT_DOCUMENTS, method = RequestMethod.GET)
	public ModelAndView transportDocuments() throws IOException, NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		ModelAndView mav = new ModelAndView("private.transportDocuments");
		mav.addObject("activePage", PAGES.TRANSPORT_DOCUMENTS);
		Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		mav.addObject("transportDocumentYears", mapper.writeValueAsString(businessService.getTransportDocumentYears(principal.getBusiness().getId())));
		return mav;
	}
	
	@RequestMapping(value = Urls.PRIVATE_DOCS_CREDIT_NOTES, method = RequestMethod.GET)
	public ModelAndView creditNotes() throws IOException, NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		ModelAndView mav = new ModelAndView("private.creditNotes");
		mav.addObject("activePage", PAGES.CREDIT_NOTES);
		Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		mav.addObject("creditNoteYears", mapper.writeValueAsString(businessService.getCreditNoteYears(principal.getBusiness().getId())));
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
	
	@RequestMapping(value = Urls.PRIVATE_HELLO, method = RequestMethod.GET)
	public ModelAndView hello(){
		ModelAndView mav = new ModelAndView("private.hello");
		return mav;
	}
	
	@RequestMapping(value = Urls.PRIVATE_SETTINGS, method = RequestMethod.GET)
	public ModelAndView settings(){
		ModelAndView mav = new ModelAndView("private.settings");
		mav.addObject("activePage", PAGES.SETTINGS);
		return mav;
	}
	
	@RequestMapping(value = Urls.PRIVATE_SHARE, method = RequestMethod.GET)
	public ModelAndView share(){
		ModelAndView mav = new ModelAndView("private.share");
		mav.addObject("activePage", PAGES.SHARE);
		return mav;
	}
	
	@RequestMapping(value = Urls.PRIVATE_STATISTICS_GENERAL, method = RequestMethod.GET)
	public ModelAndView statsGeneral(){
		ModelAndView mav = new ModelAndView("private.statistics.general");
		mav.addObject("activePage", PAGES.STATISTICS_GENERAL);
		return mav;
	}
	
	@RequestMapping(value = Urls.PRIVATE_STATISTICS_CLIENTS, method = RequestMethod.GET)
	public ModelAndView statsClients(){
		ModelAndView mav = new ModelAndView("private.statistics.clients");
		mav.addObject("activePage", PAGES.STATISTICS_CLIENTS);
		return mav;
	}
	
	@RequestMapping(value = Urls.PRIVATE_STATISTICS_COMMODITIES, method = RequestMethod.GET)
	public ModelAndView statsCommodities(){
		ModelAndView mav = new ModelAndView("private.statistics.commodities");
		mav.addObject("activePage", PAGES.STATISTICS_COMMODITIES);
		return mav;
	}
	
	@RequestMapping(value = Urls.PRIVATE_PRINT_PDF, method = RequestMethod.GET)
	public ModelAndView printPdf(@RequestParam String pdfUrl) throws UnsupportedEncodingException{
		ModelAndView mav = new ModelAndView("private.printPDF");
		mav.addObject("pdfUrl", URLDecoder.decode(pdfUrl, "UTF-8"));
		return mav;
	}
	
}
