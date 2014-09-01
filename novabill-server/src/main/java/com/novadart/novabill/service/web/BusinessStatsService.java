package com.novadart.novabill.service.web;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableMap;
import com.novadart.novabill.annotation.Restrictions;
import com.novadart.novabill.authorization.PremiumChecker;
import com.novadart.novabill.domain.AccountingDocumentItem;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.dto.DTOUtils;
import com.novadart.novabill.shared.client.dto.BIClientStatsDTO;
import com.novadart.novabill.shared.client.dto.BICommodityStatsDTO;
import com.novadart.novabill.shared.client.dto.BIGeneralStatsDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.tuple.Pair;
import com.novadart.novabill.shared.client.tuple.Triple;

@Service
public class BusinessStatsService {
	
	@Autowired
	private BusinessService businessService;
	
	private int extractMonthFromDate(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH);
	}
	
	private BigDecimal[] computeTotalsPerMonthsForYear(List<InvoiceDTO> invoices) throws NotAuthenticatedException, DataAccessException{
		BigDecimal[] totals = new BigDecimal[12];
		for(int i = 0; i < 12; ++i) totals[i] = new BigDecimal("0.00");
		for(InvoiceDTO invoice: invoices){
			int month = extractMonthFromDate(invoice.getAccountingDocumentDate()); 
			totals[month] = totals[month].add(invoice.getTotalBeforeTax());
		}
		for(int i = 0; i < 12; ++i)
			totals[i] = totals[i].setScale(2, RoundingMode.HALF_UP);
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
	
	private List<Map<String, Object>> computeClientRankingByRevenue(List<InvoiceDTO> invoices, List<ClientDTO> clients) {
		Map<Long, BigDecimal> clientRevenues = new HashMap<>();
		for(InvoiceDTO invoice: invoices) {
			Long clientID = invoice.getClient().getId();
			if(clientRevenues.containsKey(clientID))
				clientRevenues.put(clientID, clientRevenues.get(clientID).add(invoice.getTotalBeforeTax()));
			else
				clientRevenues.put(clientID, invoice.getTotalBeforeTax());
		}
		List<Map<String, Object>> result = new ArrayList<>(clients.size());
		for(ClientDTO client: clients) {
			Long clientID = client.getId();
			Map<String, Object> el = new HashMap<>();
			el.put("id", clientID);
			el.put("name", client.getName());
			el.put("revenue", clientRevenues.containsKey(clientID)? clientRevenues.get(clientID).setScale(2, RoundingMode.HALF_UP): new BigDecimal("0.00"));
			result.add(el);
		}
		Collections.sort(result, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				BigDecimal rev1 = (BigDecimal)o1.get("revenue"), rev2 = (BigDecimal)o2.get("revenue");
				return rev2.compareTo(rev1);
			}
		});
		return result;
	}
	
	private List<Map<String, Object>> computeCommodityRankingByRevenue(Long businessID, Integer year, List<CommodityDTO> commodities) {
		Map<String, BigDecimal> commodityRevenues = Invoice.getCommodityRevenueDistrbutionForYear(businessID, year);
		List<Map<String, Object>> result = new ArrayList<>(commodities.size());
		for(CommodityDTO commodity: commodities) {
			Map<String, Object> el = new HashMap<>();
			String sku = commodity.getSku();
			el.put("sku", sku);
			el.put("id", commodity.getId());
			el.put("description", commodity.getDescription());
			el.put("revenue", commodityRevenues.containsKey(sku)? commodityRevenues.get(sku).setScale(2, RoundingMode.HALF_UP): new BigDecimal("0.00"));
			result.add(el);
		}
		Collections.sort(result, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				BigDecimal rev1 = (BigDecimal)o1.get("revenue"), rev2 = (BigDecimal)o2.get("revenue");
				return rev2.compareTo(rev1);
			}
		});
		return result;
	}
	
	
	@PreAuthorize("#businessID == principal.business.id")
	@Restrictions(checkers = {PremiumChecker.class})
	public BIGeneralStatsDTO getGeneralBIStats(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		BIGeneralStatsDTO generalStatsDTO = new BIGeneralStatsDTO();
		List<InvoiceDTO> invoices = businessService.getInvoices(businessID, year);
		List<ClientDTO> clients = businessService.getClients(businessID);
		generalStatsDTO.setTotalsPerMonths(computeTotalsPerMonths(year, invoices, businessService.getInvoices(businessID, year - 1)));
		Pair<BigDecimal, BigDecimal> totals = businessService.getTotalsForYear(businessID, year);
		generalStatsDTO.setTotals(ImmutableMap.of("totalBeforeTax", totals.getFirst(), "totalAfterTax", totals.getSecond()));
		generalStatsDTO.setClientsVsReturningClients(ImmutableMap.of("numberOfClients", clients.size(), "numberOfReturningClients", computeNumberOfReturningClients(invoices)));
		generalStatsDTO.setClientRankingByRevenue(computeClientRankingByRevenue(invoices, clients));
		generalStatsDTO.setCommodityRankingByRevenue(computeCommodityRankingByRevenue(businessID, year, businessService.getCommodities(businessID)));
		return generalStatsDTO;
	}
	
	private List<Triple<CommodityDTO, BigDecimal, BigDecimal>> computeCommodityRevenueStatsForClientForYear(Long businessID, Long clientID, Integer year, List<CommodityDTO> commodities) {
		Map<String, CommodityDTO> commodityMap = new HashMap<>(commodities.size());
		for(CommodityDTO commodity: commodities)
			commodityMap.put(commodity.getSku(), commodity);
		List<Triple<String, BigDecimal, BigDecimal>> commodityStats = Invoice.getCommodityRevenueStatsForClientForYear(businessID, clientID, year);
		List<Triple<CommodityDTO, BigDecimal, BigDecimal>> r = new ArrayList<>(commodityStats.size());
		for(Triple<String, BigDecimal, BigDecimal> s: commodityStats)
			if(commodityMap.containsKey(s.getFirst()))
				r.add(new Triple<>(commodityMap.get(s.getFirst()), s.getSecond(), s.getThird()));
		return r;
	}
	
	
	@PreAuthorize("#businessID == principal.business.id")
	@Restrictions(checkers = {PremiumChecker.class})
	public BIClientStatsDTO getClientBIStats(Long businessID, Long clientID, Integer year) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException{
		BIClientStatsDTO clientStatsDTO = new BIClientStatsDTO();
		List<Invoice> invoices = Invoice.getAllInvoicesForClient(businessID, clientID);
		BigDecimal totalBeforeTaxes = new BigDecimal("0.00");
		BigDecimal totalBeforeTaxesCurrentYear = new BigDecimal("0.00");
		Map<Integer, List<Invoice>> invoicesPerYears = new HashMap<>();
		for(Invoice inv: invoices){
			totalBeforeTaxes = totalBeforeTaxes.add(inv.getTotalBeforeTax());
			Integer invYear = inv.getAccountingDocumentYear();
			if(invYear.equals(year))
				totalBeforeTaxesCurrentYear = totalBeforeTaxesCurrentYear.add(inv.getTotalBeforeTax());
			if(!invoicesPerYears.containsKey(invYear))
				invoicesPerYears.put(invYear, new ArrayList<Invoice>());
			invoicesPerYears.get(invYear).add(inv);
		}
		Map<Integer, BigDecimal[]> totalsPerMonths = new HashMap<>();
		for(Integer y: invoicesPerYears.keySet())
			totalsPerMonths.put(y, computeTotalsPerMonthsForYear(DTOUtils.toDTOList(invoicesPerYears.get(y), DTOUtils.invoiceDTOConverter, false)));
		
		if(invoices.size() > 0)
			clientStatsDTO.setFirstInvoiceDate(invoices.get(0).getAccountingDocumentDate());
		clientStatsDTO.setTotalBeforeTaxes(totalBeforeTaxes.setScale(2, RoundingMode.HALF_UP));
		clientStatsDTO.setTotalBeforeTaxesCurrentYear(totalBeforeTaxesCurrentYear.setScale(2, RoundingMode.HALF_UP));
		clientStatsDTO.setTotalsPerMonths(totalsPerMonths);
		clientStatsDTO.setCommodityStatsForCurrentYear(computeCommodityRevenueStatsForClientForYear(businessID, clientID, year, businessService.getCommodities(businessID)));
		clientStatsDTO.setCommodityStatsForPrevYear(computeCommodityRevenueStatsForClientForYear(businessID, clientID, year - 1, businessService.getCommodities(businessID)));
		return clientStatsDTO;
	}
	
	private void updateClientStatsForCommodity(AccountingDocumentItem item, Map<Long, Pair<BigDecimal, BigDecimal>> clientStats){
		Long clientID = item.getAccountingDocument().getClient().getId();
		if(!clientStats.containsKey(clientID))
			clientStats.put(clientID, new Pair<>(item.getTotalBeforeTax(), item.getQuantity()));
		else{
			Pair<BigDecimal, BigDecimal> currVal = clientStats.get(clientID);
			clientStats.put(clientID, new Pair<>(currVal.getFirst().add(item.getTotalBeforeTax()), currVal.getSecond().add(item.getQuantity())));
		}
	}
	
	private List<Triple<ClientDTO, BigDecimal, BigDecimal>> enrichClientStatsForCommodityWithClientData(Map<Long, Pair<BigDecimal, BigDecimal>> clientStats,
			Map<Long, ClientDTO> clientMap) {
		List<Triple<ClientDTO, BigDecimal, BigDecimal>> result = new ArrayList<>(clientStats.size());
		for(Long clientID: clientStats.keySet()) {
			Pair<BigDecimal, BigDecimal> stats = clientStats.get(clientID);
			result.add(new Triple<>(clientMap.get(clientID),
					stats.getFirst().setScale(2, RoundingMode.HALF_UP), stats.getSecond().setScale(2, RoundingMode.HALF_UP)));
		}
		Collections.sort(result, new Comparator<Triple<ClientDTO, BigDecimal, BigDecimal>>() {
			@Override
			public int compare(Triple<ClientDTO, BigDecimal, BigDecimal> o1, Triple<ClientDTO, BigDecimal, BigDecimal> o2) {
				return o2.getSecond().compareTo(o1.getSecond());
			}
		});
		return result;
	}
	
	private Map<Long, ClientDTO> computeClientMap(List<ClientDTO> clients) {
		Map<Long, ClientDTO> clientMap = new HashMap<>(clients.size());
		for(ClientDTO clientDTO: clients)
			clientMap.put(clientDTO.getId(), clientDTO);
		return clientMap;
	}
	
	@PreAuthorize("#businessID == principal.business.id")
	@Restrictions(checkers = {PremiumChecker.class})
	public BICommodityStatsDTO getCommodityBIStats(Long businessID, String sku, Integer year) throws NotAuthenticatedException,
				DataAccessException, FreeUserAccessForbiddenException {
		BICommodityStatsDTO commodityStatsDTO = new BICommodityStatsDTO();
		List<Invoice> invoices = Invoice.getAllInvoicesContainingCommodity(businessID, sku);
		BigDecimal total = new BigDecimal("0.00");
		BigDecimal totalCurrentYear = new BigDecimal("0.00");
		Map<Integer, BigDecimal[]> totalsPerMonths = new HashMap<>();
		Map<Long, Pair<BigDecimal, BigDecimal>> clientStatsCurrentYear = new HashMap<>();
		Map<Long, Pair<BigDecimal, BigDecimal>> clientStatsPrevYear = new HashMap<>();
		for(Invoice invoice: invoices){
			for(AccountingDocumentItem item: invoice.getAccountingDocumentItems()){
				if(sku.equals(item.getSku())){
					total = total.add(item.getTotalBeforeTax());
					if(invoice.getAccountingDocumentYear().equals(year))
						totalCurrentYear = totalCurrentYear.add(item.getTotalBeforeTax());
					if(!totalsPerMonths.containsKey(invoice.getAccountingDocumentYear())){
						BigDecimal[] totalsPerMonthsPerYear = new BigDecimal[12];
						for(int i = 0; i < 12; ++i) totalsPerMonthsPerYear[i] = new BigDecimal("0.00");
						totalsPerMonths.put(invoice.getAccountingDocumentYear(), totalsPerMonthsPerYear);
					}
					BigDecimal[] totalsPerMonthsPerYear = totalsPerMonths.get(invoice.getAccountingDocumentYear());
					int month = extractMonthFromDate(invoice.getAccountingDocumentDate());
					totalsPerMonthsPerYear[month] = totalsPerMonthsPerYear[month].add(item.getTotalBeforeTax());
					if(item.getAccountingDocument().getAccountingDocumentYear().equals(year))
						updateClientStatsForCommodity(item, clientStatsCurrentYear);
					if(item.getAccountingDocument().getAccountingDocumentYear().equals(year - 1))
						updateClientStatsForCommodity(item, clientStatsPrevYear);
				}
			}
		}
		for(BigDecimal[] totalsPerMonthsPerYear: totalsPerMonths.values())
			for(int i = 0; i < 12; ++i) totalsPerMonthsPerYear[i] = totalsPerMonthsPerYear[i].setScale(2, RoundingMode.HALF_UP);
		commodityStatsDTO.setTotalBeforeTaxes(total.setScale(2, RoundingMode.HALF_UP));
		commodityStatsDTO.setTotalBeforeTaxesCurrentYear(totalCurrentYear.setScale(2, RoundingMode.HALF_UP));
		commodityStatsDTO.setTotalsPerMonths(totalsPerMonths);
		Map<Long, ClientDTO> clientMap = computeClientMap(businessService.getClients(businessID));
		commodityStatsDTO.setClientStatsForCurrentYear(enrichClientStatsForCommodityWithClientData(clientStatsCurrentYear, clientMap));
		commodityStatsDTO.setClientStatsForPrevYear(enrichClientStatsForCommodityWithClientData(clientStatsPrevYear, clientMap));
		return commodityStatsDTO;
	}

}
