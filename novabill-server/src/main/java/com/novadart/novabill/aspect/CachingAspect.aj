package com.novadart.novabill.aspect;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.CacheEvict;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import java.math.BigDecimal;
import java.util.List;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;

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
		@Cacheable(value = BUSINESS_INVOICE_YEAR_COUNT_CACHE, key = "#businessID");
	declare @method : public BigDecimal com.novadart.novabill.web.gwt.BusinessServiceImpl.getTotalAfterTaxesForYear(Long, Integer):
		@Cacheable(value = BUSINESS_TOTAL_AFTER_TAXES_YEAR_CACHE, key = "#businessID");
	
	
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
	public static final String INVOICE_CACHE = "invoice-cache";
	
	declare @method : public List<InvoiceDTO> com.novadart.novabill.web.gwt.InvoiceServiceImpl.getAll(Long): @Cacheable(value = INVOICE_CACHE, key = "#businessID");
	
	declare @method : public void com.novadart.novabill.web.gwt.InvoiceServiceImpl.remove(Long, Long, Long):
		@Caching(evict = {
				@CacheEvict(value = BUSINESS_INVOICE_COUNT_CACHE, key = "#businessID"),
				@CacheEvict(value = BUSINESS_INVOICE_YEAR_COUNT_CACHE, key = "#businessID"),
				@CacheEvict(value = BUSINESS_CLIENT_COUNT_CACHE, key = "#businessID"),
				@CacheEvict(value = BUSINESS_TOTAL_AFTER_TAXES_YEAR_CACHE, key = "#businessID"),
				@CacheEvict(value = INVOICE_CACHE, key = "#businessID"),
		});
	
	declare @method : public Long com.novadart.novabill.web.gwt.InvoiceServiceImpl.add(InvoiceDTO):
		@Caching(evict = {
				@CacheEvict(value = BUSINESS_INVOICE_COUNT_CACHE, key = "#invoiceDTO.business.id"),
				@CacheEvict(value = BUSINESS_INVOICE_YEAR_COUNT_CACHE, key = "#invoiceDTO.business.id"),
				@CacheEvict(value = BUSINESS_CLIENT_COUNT_CACHE, key = "#invoiceDTO.business.id"),
				@CacheEvict(value = BUSINESS_TOTAL_AFTER_TAXES_YEAR_CACHE, key = "#invoiceDTO.business.id"),
				@CacheEvict(value = INVOICE_CACHE, key = "#invoiceDTO.business.id"),
		});
	
	declare @method : public void com.novadart.novabill.web.gwt.InvoiceServiceImpl.update(InvoiceDTO):
		@Caching(evict = {
				@CacheEvict(value = BUSINESS_INVOICE_COUNT_CACHE, key = "#invoiceDTO.business.id"),
				@CacheEvict(value = BUSINESS_INVOICE_YEAR_COUNT_CACHE, key = "#invoiceDTO.business.id"),
				@CacheEvict(value = BUSINESS_CLIENT_COUNT_CACHE, key = "#invoiceDTO.business.id"),
				@CacheEvict(value = BUSINESS_TOTAL_AFTER_TAXES_YEAR_CACHE, key = "#invoiceDTO.business.id"),
				@CacheEvict(value = INVOICE_CACHE, key = "#invoiceDTO.business.id"),
		});
	
	/*
	 * CreditNote caching
	 * Dependencies None
	 */
	
	public static final String CREDITNOTE_CACHE = "creditnote-cache";
	
	declare @method : public List<CreditNoteDTO> com.novadart.novabill.web.gwt.CreditNoteServiceImpl.getAll(Long): @Cacheable(value = CREDITNOTE_CACHE, key = "#businessID");
	
	declare @method : public void com.novadart.novabill.web.gwt.CreditNoteServiceImpl.remove(Long, Long, Long):
		@Caching(evict = {
				@CacheEvict(value = CREDITNOTE_CACHE, key = "#businessID"),
		});
	
	declare @method : public Long com.novadart.novabill.web.gwt.CreditNoteServiceImpl.add(CreditNoteDTO):
		@Caching(evict = {
				@CacheEvict(value = CREDITNOTE_CACHE, key = "#creditNoteDTO.business.id"),
		});
	
	declare @method : public void com.novadart.novabill.web.gwt.CreditNoteServiceImpl.update(CreditNoteDTO):
		@Caching(evict = {
				@CacheEvict(value = CREDITNOTE_CACHE, key = "#creditNoteDTO.business.id"),
		});
	
	
	
}
