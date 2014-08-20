package com.novadart.novabill.web.mvc.ajax;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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
import com.novadart.novabill.shared.client.tuple.Pair;

@Controller
@RestExceptionProcessingMixin
@RequestMapping("/private/ajax/businesses/{businessID}/bizintel")
public class BusinessStatsController {
	
	@Autowired
	private BusinessService businessService;
	
	private BigDecimal[] computeTotalsPerMonthsForYear(List<InvoiceDTO> invoices) throws NotAuthenticatedException, DataAccessException{
		Calendar cal = Calendar.getInstance();
		BigDecimal[] totals = new BigDecimal[12];
		for(int i = 0; i < 12; ++i) totals[i] = new BigDecimal("0.00");
		for(InvoiceDTO invoice: invoices){
			cal.setTime(invoice.getAccountingDocumentDate());
			int month = cal.get(Calendar.MONTH); 
			totals[month] = totals[month].add(invoice.getTotalBeforeTax());
		}
		for(int i = 0; i < 12; ++i) totals[i].setScale(2, RoundingMode.HALF_UP);
		return totals;
	}
	
	private Map<Integer, BigDecimal[]> computeTotalsPerMonths(Integer year, List<InvoiceDTO> invoices, List<InvoiceDTO> prevInvoices) throws NotAuthenticatedException, DataAccessException {
		Map<Integer, BigDecimal[]> totalsPerMonths = new HashMap<>();
		totalsPerMonths.put(year, computeTotalsPerMonthsForYear(invoices));
		totalsPerMonths.put(year - 1, computeTotalsPerMonthsForYear(prevInvoices));
		return totalsPerMonths;
	}
	
	private Integer computeNumberOfReturningClients(List<InvoiceDTO> invoices){
		Map<Long, Integer> clientInvCount = new HashMap<>();
		for(InvoiceDTO invoice: invoices) {
			Long clientID = invoice.getClient().getId();
			if(clientInvCount.containsKey(clientID))
				clientInvCount.put(clientID, clientInvCount.get(clientID) + 1);
			else
				clientInvCount.put(clientID, 1);
		}
		int returningClientsCount = 0;
		for(Integer cnt: clientInvCount.values())
			if(cnt > 1) returningClientsCount += 1;
		return returningClientsCount;
	}
	
	@RequestMapping(value = "/genstats/{year}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public BIGeneralStatsDTO getGeneralBIStats(@PathVariable Long businessID, @PathVariable Integer year) throws NotAuthenticatedException, DataAccessException {
		BIGeneralStatsDTO generalStatsDTO = new BIGeneralStatsDTO();
		List<InvoiceDTO> invoices = businessService.getInvoices(businessID, year);
		generalStatsDTO.setTotalsPerMonths(computeTotalsPerMonths(year, invoices, businessService.getInvoices(businessID, year - 1)));
		generalStatsDTO.setTotals(businessService.getTotalsForYear(businessID, year));
		generalStatsDTO.setClientsVsReturningClients(new Pair<Integer, Integer>(businessService.getClients(businessID).size(), computeNumberOfReturningClients(invoices)));
		return generalStatsDTO;
	}
	
	
}
