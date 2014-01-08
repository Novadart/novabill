package com.novadart.novabill.aspect;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;

public privileged aspect CachingAspect {
	
	private String ehcacheDiskStore;
	
	private void initCacheStore() throws IOException{
		File folder = new File(ehcacheDiskStore);
		if(folder.exists())
			FileUtils.cleanDirectory(folder);
		else
			folder.mkdir();
	}
	
	public void setEhcacheDiskStore(String ehcacheDiskStore) throws IOException{
		this.ehcacheDiskStore = ehcacheDiskStore;
		initCacheStore();
	}

	/*
	 * Business service caching
	 * Dependencies: Client caching, Invoice caching, CreditNote caching, Estimation caching, TransportDocument caching, PaymentType caching
	 */
	public static final String BUSINESS_CACHE = "business-cache";
	
	public static final String CLIENT_CACHE = "client-cache";
	
	public static final String INVOICE_CACHE = "invoice-cache";
	
	public static final String CREDITNOTE_CACHE = "creditnote-cache";
	
	public static final String ESTIMATION_CACHE = "estimation-cache";
	
	public static final String TRANSPORTDOCUMENT_CACHE = "transportdocument-cache";
	
	public static final String PAYMENTTYPE_CACHE = "paymenttype-cache";
	
	public static final String COMMODITY_CACHE = "commodity-cache";
	
	public static final String DOCSYEARS_CACHE = "docsyears-cache";
	
	declare @method : public BusinessDTO com.novadart.novabill.service.web.BusinessServiceImpl.get(Long): @Cacheable(value = BUSINESS_CACHE, key = "#businessID");
	
	declare @method : public List<ClientDTO> com.novadart.novabill.service.web.BusinessServiceImpl.getClients(Long): @Cacheable(value = CLIENT_CACHE, key = "#businessID");
	
	declare @method : public List<InvoiceDTO> com.novadart.novabill.service.web.BusinessServiceImpl.getInvoices(Long, Integer):
		@Cacheable(value = INVOICE_CACHE, key = "#businessID.toString().concat('-').concat(#year.toString())",
				condition = "T(java.lang.Integer).parseInt(new java.text.SimpleDateFormat('yyyy').format(new java.util.Date())) - #year < 2");
	
	declare @method : public List<CreditNoteDTO> com.novadart.novabill.service.web.BusinessServiceImpl.getCreditNotes(Long, Integer):
		@Cacheable(value = CREDITNOTE_CACHE, key = "#businessID.toString().concat('-').concat(#year.toString())",
				condition = "T(java.lang.Integer).parseInt(new java.text.SimpleDateFormat('yyyy').format(new java.util.Date())) - #year < 2");
	
	declare @method : public List<EstimationDTO> com.novadart.novabill.service.web.BusinessServiceImpl.getEstimations(Long, Integer):
		@Cacheable(value = ESTIMATION_CACHE, key = "#businessID.toString().concat('-').concat(#year.toString())",
				condition = "T(java.lang.Integer).parseInt(new java.text.SimpleDateFormat('yyyy').format(new java.util.Date())) - #year < 2");
	
	declare @method : public List<TransportDocumentDTO> com.novadart.novabill.service.web.BusinessServiceImpl.getTransportDocuments(Long, Integer):
		@Cacheable(value = TRANSPORTDOCUMENT_CACHE, key = "#businessID.toString().concat('-').concat(#year.toString())",
				condition = "T(java.lang.Integer).parseInt(new java.text.SimpleDateFormat('yyyy').format(new java.util.Date())) - #year < 2");
	
	declare @method : public List<PaymentTypeDTO> com.novadart.novabill.service.web.BusinessServiceImpl.getPaymentTypes(Long): @Cacheable(value = PAYMENTTYPE_CACHE, key = "#businessID");
	
	declare @method : public List<CommodityDTO> com.novadart.novabill.service.web.BusinessServiceImpl.getCommodities(Long): @Cacheable(value = COMMODITY_CACHE, key = "#businessID");
	
	declare @method : public List<Integer> com.novadart.novabill.service.web.BusinessServiceImpl.getInvoceYears(Long): @Cacheable(value = DOCSYEARS_CACHE, key = "#businessID.toString().concat('-invoices')");
	
	declare @method : public List<Integer> com.novadart.novabill.service.web.BusinessServiceImpl.getCreditNoteYears(Long): @Cacheable(value = DOCSYEARS_CACHE, key = "#businessID.toString().concat('-creditnotes')");
	
	declare @method : public List<Integer> com.novadart.novabill.service.web.BusinessServiceImpl.getEstimationYears(Long): @Cacheable(value = DOCSYEARS_CACHE, key = "#businessID.toString().concat('-estimations')");
	
	declare @method : public List<Integer> com.novadart.novabill.service.web.BusinessServiceImpl.getTransportDocumentYears(Long): @Cacheable(value = DOCSYEARS_CACHE, key = "#businessID.toString().concat('-transportdocs')");
	
	declare @method : public void com.novadart.novabill.service.web.BusinessServiceImpl.update(BusinessDTO): @CacheEvict(value = BUSINESS_CACHE, key = "#businessDTO.id");
	
	
	/*
	 * Client service caching
	 * Dependencies: None
	 */
	
	declare @method : public Long com.novadart.novabill.service.web.ClientService.add(Long, ClientDTO): @CacheEvict(value = CLIENT_CACHE, key = "#businessID");
	
	declare @method : public void com.novadart.novabill.service.web.ClientService.update(Long, ClientDTO): @Caching(evict = {
			@CacheEvict(value = CLIENT_CACHE, key = "#businessID"),
			@CacheEvict(value = INVOICE_CACHE, key = "#businessID.toString().concat('-').concat(new java.text.SimpleDateFormat('yyyy').format(new java.util.Date()))"),
			@CacheEvict(value = CREDITNOTE_CACHE, key = "#businessID.toString().concat('-').concat(new java.text.SimpleDateFormat('yyyy').format(new java.util.Date()))"),
			@CacheEvict(value = ESTIMATION_CACHE, key = "#businessID.toString().concat('-').concat(new java.text.SimpleDateFormat('yyyy').format(new java.util.Date()))"),
			@CacheEvict(value = TRANSPORTDOCUMENT_CACHE, key = "#businessID.toString().concat('-').concat(new java.text.SimpleDateFormat('yyyy').format(new java.util.Date()))"),
			@CacheEvict(value = INVOICE_CACHE, key = "#businessID.toString().concat('-').concat(new java.text.SimpleDateFormat('yyyy').format(new java.util.Date()))"),
			@CacheEvict(value = INVOICE_CACHE, key = "#businessID.toString().concat('-').concat(T(java.lang.Integer).parseInt(new java.text.SimpleDateFormat('yyyy').format(new java.util.Date()))-1)"),
			@CacheEvict(value = CREDITNOTE_CACHE, key = "#businessID.toString().concat('-').concat(T(java.lang.Integer).parseInt(new java.text.SimpleDateFormat('yyyy').format(new java.util.Date()))-1)"),
			@CacheEvict(value = ESTIMATION_CACHE, key = "#businessID.toString().concat('-').concat(T(java.lang.Integer).parseInt(new java.text.SimpleDateFormat('yyyy').format(new java.util.Date()))-1)"),
			@CacheEvict(value = TRANSPORTDOCUMENT_CACHE, key = "#businessID.toString().concat('-').concat(T(java.lang.Integer).parseInt(new java.text.SimpleDateFormat('yyyy').format(new java.util.Date()))-1)"),
			});
	
	declare @method : public void com.novadart.novabill.service.web.ClientService.remove(Long, Long): @CacheEvict(value = CLIENT_CACHE, key = "#businessID");
	
	/*
	 * Invoice service caching
	 * 
	 * Dependencies: None
	 */
	
	declare @method : public void com.novadart.novabill.service.web.InvoiceService.remove(Long, Long, Long): @Caching(evict = {
			@CacheEvict(value = INVOICE_CACHE, beforeInvocation = true,
					key = "#businessID.toString().concat('-').concat(T(com.novadart.novabill.domain.Invoice).findInvoice(#id).accountingDocumentYear.toString())"),
			@CacheEvict(value = DOCSYEARS_CACHE, key = "#businessID.toString().concat('-invoices')")
	}); 
		
	declare @method : public Long com.novadart.novabill.service.web.InvoiceService.add(InvoiceDTO): @Caching(evict = {
			@CacheEvict(value = INVOICE_CACHE,
					key = "#invoiceDTO.business.id.toString().concat('-').concat(new java.text.SimpleDateFormat('yyyy').format(#invoiceDTO.accountingDocumentDate).toString())"),
			@CacheEvict(value = DOCSYEARS_CACHE, key = "#invoiceDTO.business.id.toString().concat('-invoices')")
	});
		
	declare @method : public void com.novadart.novabill.service.web.InvoiceService.update(InvoiceDTO): 
		@CacheEvict(value = INVOICE_CACHE, key = "#invoiceDTO.business.id.toString().concat('-').concat(new java.text.SimpleDateFormat('yyyy').format(#invoiceDTO.accountingDocumentDate).toString())");
	
	declare @method : public void com.novadart.novabill.service.web.InvoiceService.setPayed(Long, ..):
		@CacheEvict(value = INVOICE_CACHE, key = "#businessID.toString().concat('-').concat(T(com.novadart.novabill.domain.Invoice).findInvoice(#id).accountingDocumentYear.toString())");
	
	/*
	 * CreditNote caching
	 * Dependencies: None
	 */
	
	declare @method : public void com.novadart.novabill.service.web.CreditNoteService.remove(Long, Long, Long): @Caching(evict = {
			@CacheEvict(value = CREDITNOTE_CACHE, beforeInvocation = true,
					key = "#businessID.toString().concat('-').concat(T(com.novadart.novabill.domain.CreditNote).findCreditNote(#id).accountingDocumentYear.toString())"),
			@CacheEvict(value = DOCSYEARS_CACHE, key = "#businessID.toString().concat('-creditnotes')")
	});
		
	declare @method : public Long com.novadart.novabill.service.web.CreditNoteService.add(CreditNoteDTO): @Caching(evict = {
			@CacheEvict(value = CREDITNOTE_CACHE, key = "#creditNoteDTO.business.id.toString().concat('-').concat(new java.text.SimpleDateFormat('yyyy').format(#creditNoteDTO.accountingDocumentDate).toString())"),
			@CacheEvict(value = DOCSYEARS_CACHE, key = "#creditNoteDTO.business.id.toString().concat('-creditnotes')")
	}); 
	
	declare @method : public void com.novadart.novabill.service.web.CreditNoteService.update(CreditNoteDTO):
		@CacheEvict(value = CREDITNOTE_CACHE, key = "#creditNoteDTO.business.id.toString().concat('-').concat(new java.text.SimpleDateFormat('yyyy').format(#creditNoteDTO.accountingDocumentDate).toString())");
	
	
	/*
	 * Estimation caching
	 * Dependencies: None
	 */
	
	declare @method : public void com.novadart.novabill.service.web.EstimationService.remove(Long, Long, Long): @Caching(evict = {
			@CacheEvict(value = ESTIMATION_CACHE, beforeInvocation = true,
					key = "#businessID.toString().concat('-').concat(T(com.novadart.novabill.domain.Estimation).findEstimation(#id).accountingDocumentYear.toString())"),
			@CacheEvict(value = DOCSYEARS_CACHE, key = "#businessID.toString().concat('-estimations')")
	});
		
	declare @method : public Long com.novadart.novabill.service.web.EstimationService.add(EstimationDTO): @Caching(evict = {
			@CacheEvict(value = ESTIMATION_CACHE, key = "#estimationDTO.business.id.toString().concat('-').concat(new java.text.SimpleDateFormat('yyyy').format(#estimationDTO.accountingDocumentDate).toString())"),
			@CacheEvict(value = DOCSYEARS_CACHE, key = "#estimationDTO.business.id.toString().concat('-estimations')")
	});
	
	declare @method : public void com.novadart.novabill.service.web.EstimationService.update(EstimationDTO):
		@CacheEvict(value = ESTIMATION_CACHE, key = "#estimationDTO.business.id.toString().concat('-').concat(new java.text.SimpleDateFormat('yyyy').format(#estimationDTO.accountingDocumentDate).toString())");
	
	/*
	 * TransportDocument caching
	 * Dependencies: None
	 */
	
	declare @method : public void com.novadart.novabill.service.web.TransportDocumentService.remove(Long, Long, Long): @Caching(evict = {
			@CacheEvict(value = TRANSPORTDOCUMENT_CACHE, beforeInvocation = true,
					key = "#businessID.toString().concat('-').concat(T(com.novadart.novabill.domain.TransportDocument).findTransportDocument(#id).accountingDocumentYear.toString())"),
			@CacheEvict(value = DOCSYEARS_CACHE, key = "#businessID.toString().concat('-transportdocs')"),
	});

	declare @method : public Long com.novadart.novabill.service.web.TransportDocumentService.add(TransportDocumentDTO): @Caching(evict = {
			@CacheEvict(value = TRANSPORTDOCUMENT_CACHE,
					key = "#transportDocDTO.business.id.toString().concat('-').concat(new java.text.SimpleDateFormat('yyyy').format(#transportDocDTO.accountingDocumentDate).toString())"),
			@CacheEvict(value = DOCSYEARS_CACHE, key = "#transportDocDTO.business.id.toString().concat('-transportdocs')"),
	});
		
	declare @method : public void com.novadart.novabill.service.web.TransportDocumentService.update(TransportDocumentDTO):
		@CacheEvict(value = TRANSPORTDOCUMENT_CACHE, key = "#transportDocDTO.business.id.toString().concat('-').concat(new java.text.SimpleDateFormat('yyyy').format(#transportDocDTO.accountingDocumentDate).toString())");
	
	/*
	 * PaymentType caching
	 * Dependencies: None
	 */
	
	declare @method : public void com.novadart.novabill.service.web.PaymentTypeService.remove(Long, Long): @CacheEvict(value = {PAYMENTTYPE_CACHE, CLIENT_CACHE}, key = "#businessID");
	
	declare @method : public Long com.novadart.novabill.service.web.PaymentTypeService.add(PaymentTypeDTO): @CacheEvict(value = PAYMENTTYPE_CACHE, key = "#paymentTypeDTO.business.id");
	
	declare @method : public void com.novadart.novabill.service.web.PaymentTypeService.update(PaymentTypeDTO): @CacheEvict(value = PAYMENTTYPE_CACHE, key = "#paymentTypeDTO.business.id");
	
	/*
	 * Commodity caching
	 * Dependencies: None
	 */
	
	declare @method : public void com.novadart.novabill.service.web.CommodityService.remove(Long, Long): @CacheEvict(value = COMMODITY_CACHE, key = "#businessID");
	
	declare @method : public Long com.novadart.novabill.service.web.CommodityService.add(CommodityDTO): @CacheEvict(value = COMMODITY_CACHE, key = "#commodityDTO.business.id");
	
	declare @method : public void com.novadart.novabill.service.web.CommodityService.update(CommodityDTO): @CacheEvict(value = COMMODITY_CACHE, key = "#commodityDTO.business.id");
	
	/*
	 * PriceList caching
	 * Dependencies: None
	 */
	
	public static final String PRICELIST_CACHE = "pricelist-cache";
	
	//declare @method : public PriceListDTO com.novadart.novabill.service.web.PriceListService.get(Long): @Cacheable(value = PRICELIST_CACHE, key = "#id");
	
}
