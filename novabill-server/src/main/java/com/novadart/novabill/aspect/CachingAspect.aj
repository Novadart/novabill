package com.novadart.novabill.aspect;

import org.springframework.cache.annotation.Cacheable;
import com.novadart.novabill.shared.client.dto.BusinessStatsDTO;
import java.math.BigDecimal;

public privileged aspect CachingAspect {

	public static final String BUSINESS_STATS_CACHE = "business-stats-cache";
	public static final String BUSINESS_CLIENT_COUNT_CACHE = "business-client-count-cache";
	public static final String BUSINESS_INVOICE_COUNT_CACHE = "business-invoice-count-cache";
	public static final String BUSINESS_INVOICE_YEAR_COUNT_CACHE = "business-invoice-year-count-cache";
	public static final String BUSINESS_TOTAL_AFTER_TAXES_YEAR_CACHE = "business-total-after-taxes-year-cache";
	
	declare @method : public BusinessStatsDTO com.novadart.novabill.web.gwt.BusinessServiceImpl.getStats(Long): @Cacheable(value = BUSINESS_STATS_CACHE, key = "#businessID");
	declare @method : public long com.novadart.novabill.web.gwt.BusinessServiceImpl.countClients(Long): @Cacheable(value = BUSINESS_CLIENT_COUNT_CACHE, key = "#businessID");
	declare @method : public long com.novadart.novabill.web.gwt.BusinessServiceImpl.countInvoices(Long): @Cacheable(value = BUSINESS_INVOICE_COUNT_CACHE, key = "#businessID");
	declare @method : public long com.novadart.novabill.web.gwt.BusinessServiceImpl.countInvoicesForYear(Long, Integer):
		@Cacheable(value = BUSINESS_INVOICE_YEAR_COUNT_CACHE, key = "T(String.format('%d-%d', #businessID, #year))");
	declare @method : public BigDecimal com.novadart.novabill.web.gwt.BusinessServiceImpl.getTotalAfterTaxesForYear(Long, Integer):
		@Cacheable(value = BUSINESS_TOTAL_AFTER_TAXES_YEAR_CACHE, key = "T(String.format('%d-%d', #businessID, #year))");

}
