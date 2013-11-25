package com.novadart.novabill.aspect;

import org.apache.commons.io.FileUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import java.io.File;
import java.io.IOException;
import java.util.List;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.PriceListDTO;

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
	
	declare @method : public List<InvoiceDTO> com.novadart.novabill.service.web.BusinessServiceImpl.getInvoices(Long): @Cacheable(value = INVOICE_CACHE, key = "#businessID");
	
	declare @method : public List<CreditNoteDTO> com.novadart.novabill.service.web.BusinessServiceImpl.getCreditNotes(Long): @Cacheable(value = CREDITNOTE_CACHE, key = "#businessID");
	
	declare @method : public List<EstimationDTO> com.novadart.novabill.service.web.BusinessServiceImpl.getEstimations(Long): @Cacheable(value = ESTIMATION_CACHE, key = "#businessID");
	
	declare @method : public List<TransportDocumentDTO> com.novadart.novabill.service.web.BusinessServiceImpl.getTransportDocuments(Long): @Cacheable(value = TRANSPORTDOCUMENT_CACHE, key = "#businessID");
	
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
	
	declare @method : public void com.novadart.novabill.service.web.ClientService.update(Long, ClientDTO): @CacheEvict(value = CLIENT_CACHE, key = "#businessID");
	
	declare @method : public void com.novadart.novabill.service.web.ClientService.remove(Long, Long): @CacheEvict(value = CLIENT_CACHE, key = "#businessID");
	
	/*
	 * Invoice service caching
	 * 
	 * Dependencies: None
	 */
	
	declare @method : public void com.novadart.novabill.service.web.InvoiceService.remove(Long, Long, Long): @Caching(evict = {
			@CacheEvict(value = INVOICE_CACHE, key = "#businessID"),
			@CacheEvict(value = DOCSYEARS_CACHE, key = "#businessID.toString().concat('-invoices')")
	}); 
		
	declare @method : public Long com.novadart.novabill.service.web.InvoiceService.add(InvoiceDTO): @Caching(evict = {
			@CacheEvict(value = INVOICE_CACHE, key = "#invoiceDTO.business.id"),
			@CacheEvict(value = DOCSYEARS_CACHE, key = "#invoiceDTO.business.id.toString().concat('-invoices')")
	});
		
	declare @method : public void com.novadart.novabill.service.web.InvoiceService.update(InvoiceDTO): @CacheEvict(value = INVOICE_CACHE, key = "#invoiceDTO.business.id");
	
	declare @method : public void com.novadart.novabill.service.web.InvoiceService.setPayed(Long, ..): @CacheEvict(value = INVOICE_CACHE, key = "#businessID");
	
	/*
	 * CreditNote caching
	 * Dependencies: None
	 */
	
	declare @method : public void com.novadart.novabill.service.web.CreditNoteService.remove(Long, Long, Long): @Caching(evict = {
			@CacheEvict(value = CREDITNOTE_CACHE, key = "#businessID"),
			@CacheEvict(value = DOCSYEARS_CACHE, key = "#businessID.toString().concat('-creditnotes')")
	});
		
	declare @method : public Long com.novadart.novabill.service.web.CreditNoteService.add(CreditNoteDTO): @Caching(evict = {
			@CacheEvict(value = CREDITNOTE_CACHE, key = "#creditNoteDTO.business.id"),
			@CacheEvict(value = DOCSYEARS_CACHE, key = "#creditNoteDTO.business.id.toString().concat('-creditnotes')")
	}); 
	
	declare @method : public void com.novadart.novabill.service.web.CreditNoteService.update(CreditNoteDTO): @CacheEvict(value = CREDITNOTE_CACHE, key = "#creditNoteDTO.business.id");
	
	
	/*
	 * Estimation caching
	 * Dependencies: None
	 */
	
	declare @method : public void com.novadart.novabill.service.web.EstimationService.remove(Long, Long, Long): @Caching(evict = {
			@CacheEvict(value = ESTIMATION_CACHE, key = "#businessID"),
			@CacheEvict(value = DOCSYEARS_CACHE, key = "#businessID.toString().concat('-estimations')")
	});
		
	declare @method : public Long com.novadart.novabill.service.web.EstimationService.add(EstimationDTO): @Caching(evict = {
			@CacheEvict(value = ESTIMATION_CACHE, key = "#estimationDTO.business.id"),
			@CacheEvict(value = DOCSYEARS_CACHE, key = "#estimationDTO.business.id.toString().concat('-estimations')")
	});
	
	declare @method : public void com.novadart.novabill.service.web.EstimationService.update(EstimationDTO): @CacheEvict(value = ESTIMATION_CACHE, key = "#estimationDTO.business.id");
	
	/*
	 * TransportDocument caching
	 * Dependencies: None
	 */
	
	declare @method : public void com.novadart.novabill.service.web.TransportDocumentService.remove(Long, Long, Long): @Caching(evict = {
			@CacheEvict(value = TRANSPORTDOCUMENT_CACHE, key = "#businessID"),
			@CacheEvict(value = DOCSYEARS_CACHE, key = "#businessID.toString().concat('-transportdocs')"),
	});

	declare @method : public Long com.novadart.novabill.service.web.TransportDocumentService.add(TransportDocumentDTO): @Caching(evict = {
			@CacheEvict(value = TRANSPORTDOCUMENT_CACHE, key = "#transportDocDTO.business.id"),
			@CacheEvict(value = DOCSYEARS_CACHE, key = "#transportDocDTO.business.id.toString().concat('-transportdocs')"),
	});
		
	declare @method : public void com.novadart.novabill.service.web.TransportDocumentService.update(TransportDocumentDTO): @CacheEvict(value = TRANSPORTDOCUMENT_CACHE, key = "#transportDocDTO.business.id");
	
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
	
	declare @method : public PriceListDTO com.novadart.novabill.service.web.PriceListService.get(Long): @Cacheable(value = PRICELIST_CACHE, key = "#id");
	
}
