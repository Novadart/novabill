package com.novadart.novabill.aspect;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.CacheEvict;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import java.math.BigDecimal;
import java.util.List;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;

public privileged aspect CachingAspect {

	/*
	 * Business service caching
	 * Dependencies: Client caching, Invoice caching
	 */
	public static final String BUSINESS_STATS_CACHE = "business-stats-cache";
	public static final String BUSINESS_CLIENT_COUNT_CACHE = "business-client-count-cache";
	public static final String BUSINESS_INVOICE_COUNT_CACHE = "business-invoice-count-cache";
	public static final String BUSINESS_INVOICE_YEAR_COUNT_CACHE = "business-invoice-year-count-cache";
	public static final String BUSINESS_TOTAL_AFTER_TAXES_YEAR_CACHE = "business-total-after-taxes-year-cache";
	
	declare @method : public BusinessStatsDTO com.novadart.novabill.web.gwt.BusinessServiceImpl.getStats(Long): @Cacheable(value = BUSINESS_STATS_CACHE, key = "#businessID");
	declare @method : public Long com.novadart.novabill.web.gwt.BusinessServiceImpl.countClients(Long): @Cacheable(value = BUSINESS_CLIENT_COUNT_CACHE, key = "#businessID");
	declare @method : public Long com.novadart.novabill.web.gwt.BusinessServiceImpl.countInvoices(Long): @Cacheable(value = BUSINESS_INVOICE_COUNT_CACHE, key = "#businessID");
	declare @method : public Long com.novadart.novabill.web.gwt.BusinessServiceImpl.countInvoicesForYear(Long, Integer):
		@Cacheable(value = BUSINESS_INVOICE_YEAR_COUNT_CACHE, key = "T(String).format('%d-%d', #businessID, #year)");
	declare @method : public BigDecimal com.novadart.novabill.web.gwt.BusinessServiceImpl.getTotalAfterTaxesForYear(Long, Integer):
		@Cacheable(value = BUSINESS_TOTAL_AFTER_TAXES_YEAR_CACHE, key = "T(String).format('%d-%d', #businessID, #year)");
	
	
	/*
	 * Client service caching
	 * Dependencies: None
	 */
	public static final String CLIENT_BUSINESS_CLIENTS_CACHE = "client-business-clients-cache";
	
	declare @method : public List<ClientDTO> com.novadart.novabill.web.gwt.ClientServiceImpl.getAll(Long): @Cacheable(value = CLIENT_BUSINESS_CLIENTS_CACHE, key = "#businessID");
	declare @method : public Long com.novadart.novabill.web.gwt.ClientServiceImpl.add(Long, ClientDTO): 
		@Caching(evict = {
				@CacheEvict(value = BUSINESS_CLIENT_COUNT_CACHE, key = "#businessID"),
				@CacheEvict(value = CLIENT_BUSINESS_CLIENTS_CACHE, key = "#businessID")
		});
	declare @method : public void com.novadart.novabill.web.gwt.ClientServiceImpl.update(Long, ClientDTO):
		@Caching(evict = {
				@CacheEvict(value = BUSINESS_CLIENT_COUNT_CACHE, key = "#businessID"),
				@CacheEvict(value = CLIENT_BUSINESS_CLIENTS_CACHE, key = "#businessID")
		});
	declare @method : public void com.novadart.novabill.web.gwt.ClientServiceImpl.remove(Long, Long):
		@Caching(evict = {
				@CacheEvict(value = BUSINESS_CLIENT_COUNT_CACHE, key = "#businessID"),
				@CacheEvict(value = CLIENT_BUSINESS_CLIENTS_CACHE, key = "#businessID")
		});
	
	/*
	 * Invoice service caching
	 * Dependencies: None
	 */
	public static final String INVOICE_BUSINESS_RANGE_CACHE = "invoice-business-range-cache";
	public static final String INVOICE_CLIENT_CACHE = "invoice-client-cache";
	public static final String INVOICE_CLIENT_RANGE_CACHE = "invoice-client-range-cache";
	
	declare @method : public PageDTO<InvoiceDTO> com.novadart.novabill.web.gwt.InvoiceServiceImpl.getAllInRange(Long, Integer, Integer):
		@Cacheable(value = INVOICE_BUSINESS_RANGE_CACHE, key = "T(String).format('%d-%d-%d', #businessID, #start, #length)");
	declare @method : public List<InvoiceDTO> com.novadart.novabill.web.gwt.InvoiceServiceImpl.getAllForClient(Long):
		@Cacheable(value = INVOICE_CLIENT_CACHE, key = "#clientID");
	declare @method : public PageDTO<InvoiceDTO> com.novadart.novabill.web.gwt.InvoiceServiceImpl.getAllForClientInRange(Long, Integer, Integer):
		@Cacheable(value = INVOICE_CLIENT_RANGE_CACHE, key = "T(String).format('%d-%d-%d', #clientID, #start, #length)");
	declare @method : public void com.novadart.novabill.web.gwt.InvoiceServiceImpl.remove(Long, Long, Long):
		@Caching(evict = {
				@CacheEvict(value = BUSINESS_INVOICE_COUNT_CACHE, key = "#businessID"),
				@CacheEvict(value = BUSINESS_INVOICE_YEAR_COUNT_CACHE, key = "#businessID"),
				@CacheEvict(value = BUSINESS_TOTAL_AFTER_TAXES_YEAR_CACHE, key = "#businessID"),
				@CacheEvict(value = INVOICE_CLIENT_CACHE, key = "#clientID"),
				@CacheEvict(value = INVOICE_BUSINESS_RANGE_CACHE, condition = ""),
				@CacheEvict(value = INVOICE_CLIENT_RANGE_CACHE, condition = "")
		});
	
}
