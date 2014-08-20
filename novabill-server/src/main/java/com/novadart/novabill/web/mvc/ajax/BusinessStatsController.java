package com.novadart.novabill.web.mvc.ajax;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.novadart.novabill.annotation.RestExceptionProcessingMixin;
import com.novadart.novabill.service.web.BusinessService;
import com.novadart.novabill.shared.client.dto.BIGeneralStatsDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;

@Controller
@RestExceptionProcessingMixin
@RequestMapping("/private/ajax/businesses/{businessID}/bizintel")
public class BusinessStatsController {
	
	@Autowired
	private BusinessService businessService;
	
	private BigDecimal[] computeTotalsPerMonthsForYear(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException{
		Calendar cal = Calendar.getInstance();
		BigDecimal[] totals = new BigDecimal[12];
		for(int i = 0; i < 12; ++i) totals[i] = new BigDecimal("0.00");
		for(InvoiceDTO invoice: businessService.getInvoices(businessID, year)){
			cal.setTime(invoice.getAccountingDocumentDate());
			int month = cal.get(Calendar.MONTH); 
			totals[month] = totals[month].add(invoice.getTotalBeforeTax());
		}
		for(int i = 0; i < 12; ++i) totals[i].setScale(2, RoundingMode.HALF_UP);
		return totals;
	}
	
	private Map<Integer, BigDecimal[]> computeTotalsPerMonths(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException {
		Map<Integer, BigDecimal[]> totalsPerMonths = new HashMap<>();
		totalsPerMonths.put(year, computeTotalsPerMonthsForYear(businessID, year));
		totalsPerMonths.put(year - 1, computeTotalsPerMonthsForYear(businessID, year - 1));
		return totalsPerMonths;
	}
	
	@RequestMapping(value = "/genstats/{year}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public BIGeneralStatsDTO getGeneralBIStats(@PathVariable Long businessID, @PathVariable Integer year) throws NotAuthenticatedException, DataAccessException {
		BIGeneralStatsDTO generalStatsDTO = new BIGeneralStatsDTO();
		generalStatsDTO.setTotalsPerMonths(computeTotalsPerMonths(businessID, year));
		generalStatsDTO.setTotals(businessService.getTotalsForYear(businessID, year));
		return generalStatsDTO;
	}
	
	
}
