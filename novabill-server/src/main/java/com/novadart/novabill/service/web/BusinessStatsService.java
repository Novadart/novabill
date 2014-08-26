package com.novadart.novabill.service.web;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.dto.DTOUtils;
import com.novadart.novabill.shared.client.dto.BIClientStatsDTO;
import com.novadart.novabill.shared.client.dto.BIGeneralStatsDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.tuple.Pair;
import com.novadart.novabill.shared.client.tuple.Triple;

@Service
public class BusinessStatsService {
	
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
	
	private List<Pair<ClientDTO, BigDecimal>> computeClientRankingByRevenue(List<InvoiceDTO> invoices, List<ClientDTO> clients) {
		Map<Long, BigDecimal> clientRevenues = new HashMap<>();
		for(InvoiceDTO invoice: invoices) {
			Long clientID = invoice.getClient().getId();
			if(clientRevenues.containsKey(clientID))
				clientRevenues.put(clientID, clientRevenues.get(clientID).add(invoice.getTotalBeforeTax()));
			else
				clientRevenues.put(clientID, invoice.getTotalBeforeTax());
		}
		List<Pair<ClientDTO, BigDecimal>> result = new ArrayList<>(clients.size());
		for(ClientDTO client: clients) {
			Long clientID = client.getId();
			result.add(new Pair<>(client, clientRevenues.containsKey(clientID)? clientRevenues.get(clientID).setScale(2, RoundingMode.HALF_UP): new BigDecimal("0.00")));
		}
		Collections.sort(result, new Comparator<Pair<ClientDTO, BigDecimal>>() {
			@Override
			public int compare(Pair<ClientDTO, BigDecimal> o1, Pair<ClientDTO, BigDecimal> o2) {
				return o2.getSecond().compareTo(o1.getSecond());
			}
		});
		return result;
	}
	
	private List<Pair<CommodityDTO, BigDecimal>> computeCommodityRankingByRevenue(Long businessID, Integer year, List<CommodityDTO> commodities) {
		Map<String, BigDecimal> commodityRevenues = Invoice.getCommodityRevenueDistrbutionForYear(businessID, year);
		List<Pair<CommodityDTO, BigDecimal>> result = new ArrayList<>(commodities.size());
		for(CommodityDTO commodity: commodities) {
			String sku = commodity.getSku();
			result.add(new Pair<>(commodity, commodityRevenues.containsKey(sku)? commodityRevenues.get(sku).setScale(2, RoundingMode.HALF_UP): new BigDecimal("0.00")));
		}
		Collections.sort(result, new Comparator<Pair<CommodityDTO, BigDecimal>>() {
			@Override
			public int compare(Pair<CommodityDTO, BigDecimal> o1, Pair<CommodityDTO, BigDecimal> o2) {
				return o2.getSecond().compareTo(o1.getSecond());
			}
		});
		return result;
	}
	
	public BIGeneralStatsDTO getGeneralBIStats(Long businessID, Integer year) throws NotAuthenticatedException, DataAccessException {
		BIGeneralStatsDTO generalStatsDTO = new BIGeneralStatsDTO();
		List<InvoiceDTO> invoices = businessService.getInvoices(businessID, year);
		List<ClientDTO> clients = businessService.getClients(businessID);
		generalStatsDTO.setTotalsPerMonths(computeTotalsPerMonths(year, invoices, businessService.getInvoices(businessID, year - 1)));
		generalStatsDTO.setTotals(businessService.getTotalsForYear(businessID, year));
		generalStatsDTO.setClientsVsReturningClients(new Pair<Integer, Integer>(clients.size(), computeNumberOfReturningClients(invoices)));
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
	
	public BIClientStatsDTO getClientBIStats(Long businessID, Long clientID, Integer year) throws NotAuthenticatedException, DataAccessException{
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

}
