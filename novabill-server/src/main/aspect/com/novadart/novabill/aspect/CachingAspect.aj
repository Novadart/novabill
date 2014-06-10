package com.novadart.novabill.aspect;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.PriceList;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.web.CacheEvictHooksService;
import com.novadart.novabill.shared.client.dto.ClientAddressDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.CommodityDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.PaymentTypeDTO;
import com.novadart.novabill.shared.client.dto.PriceListDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.dto.TransporterDTO;

public privileged aspect CachingAspect {
	
	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private CacheEvictHooksService cacheEvictHooksService;
	
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
	 * Dependencies: Client caching, Invoice caching, CreditNote caching, Estimation caching, TransportDocument caching, PaymentType caching, Commodity caching, PriceList caching
	 */
	//public static final String BUSINESS_CACHE = "business-cache";
	
	public static final String CLIENT_CACHE = "client-cache";
	
	public static final String INVOICE_CACHE = "invoice-cache";
	
	public static final String CREDITNOTE_CACHE = "creditnote-cache";
	
	public static final String ESTIMATION_CACHE = "estimation-cache";
	
	public static final String TRANSPORTDOCUMENT_CACHE = "transportdocument-cache";
	
	public static final String PAYMENTTYPE_CACHE = "paymenttype-cache";
	
	public static final String COMMODITY_CACHE = "commodity-cache";
	
	public static final String DOCSYEARS_CACHE = "docsyears-cache";
	
	public static final String PRICELIST_CACHE = "pricelist-cache";
	
	public static final String TRANSPORTER_CACHE = "transporter-cache";
	
	public static final String CLIENTADDRESS_CACHE = "clientaddress-cache";
	
	//declare @method : public BusinessDTO com.novadart.novabill.service.web.BusinessServiceImpl.get(Long): @Cacheable(value = BUSINESS_CACHE, key = "#businessID");
	
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
	
	declare @method : public List<TransporterDTO> com.novadart.novabill.service.web.BusinessServiceImpl.getTransporters(Long): @Cacheable(value = TRANSPORTER_CACHE, key = "#businessID");
	
	declare @method : public List<CommodityDTO> com.novadart.novabill.service.web.BusinessServiceImpl.getCommodities(Long): @Cacheable(value = COMMODITY_CACHE, key = "#businessID");
	
	declare @method : public List<PriceListDTO>  com.novadart.novabill.service.web.BusinessServiceImpl.getPriceLists(Long): @Cacheable(value = PRICELIST_CACHE, key = "#businessID.toString().concat('-all')");
	
	declare @method : public List<Integer> com.novadart.novabill.service.web.BusinessServiceImpl.getInvoceYears(Long): @Cacheable(value = DOCSYEARS_CACHE, key = "#businessID.toString().concat('-invoices')");
	
	declare @method : public List<Integer> com.novadart.novabill.service.web.BusinessServiceImpl.getCreditNoteYears(Long): @Cacheable(value = DOCSYEARS_CACHE, key = "#businessID.toString().concat('-creditnotes')");
	
	declare @method : public List<Integer> com.novadart.novabill.service.web.BusinessServiceImpl.getEstimationYears(Long): @Cacheable(value = DOCSYEARS_CACHE, key = "#businessID.toString().concat('-estimations')");
	
	declare @method : public List<Integer> com.novadart.novabill.service.web.BusinessServiceImpl.getTransportDocumentYears(Long): @Cacheable(value = DOCSYEARS_CACHE, key = "#businessID.toString().concat('-transportdocs')");
	
	//declare @method : public void com.novadart.novabill.service.web.BusinessServiceImpl.update(BusinessDTO): @CacheEvict(value = BUSINESS_CACHE, key = "#businessDTO.id");
	
	
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
	
	declare @method : public void com.novadart.novabill.service.web.InvoiceServiceImpl.remove(Long, Long, Long): @Caching(evict = {
			@CacheEvict(value = INVOICE_CACHE, beforeInvocation = true,
					key = "#businessID.toString().concat('-').concat(T(com.novadart.novabill.domain.Invoice).findInvoice(#id).accountingDocumentYear.toString())"),
			@CacheEvict(value = DOCSYEARS_CACHE, key = "#businessID.toString().concat('-invoices')")
	}); 
		
	declare @method : public Long com.novadart.novabill.service.web.InvoiceServiceImpl.add(InvoiceDTO): @Caching(evict = {
			@CacheEvict(value = INVOICE_CACHE,
					key = "#invoiceDTO.business.id.toString().concat('-').concat(new java.text.SimpleDateFormat('yyyy').format(#invoiceDTO.accountingDocumentDate).toString())"),
			@CacheEvict(value = DOCSYEARS_CACHE, key = "#invoiceDTO.business.id.toString().concat('-invoices')")
	});
		
	declare @method : public void com.novadart.novabill.service.web.InvoiceServiceImpl.update(InvoiceDTO): @Caching(evict = {
		@CacheEvict(value = INVOICE_CACHE, beforeInvocation = true, 
			key = "#invoiceDTO.business.id.toString().concat('-').concat(new java.text.SimpleDateFormat('yyyy').format(T(com.novadart.novabill.domain.Invoice).findInvoice(#invoiceDTO.id).accountingDocumentDate).toString())"),
		@CacheEvict(value = INVOICE_CACHE, key = "#invoiceDTO.business.id.toString().concat('-').concat(new java.text.SimpleDateFormat('yyyy').format(#invoiceDTO.accountingDocumentDate).toString())")
	});
	
	declare @method : public void com.novadart.novabill.service.web.InvoiceServiceImpl.setPayed(..):
		@CacheEvict(value = INVOICE_CACHE, key = "#businessID.toString().concat('-').concat(T(com.novadart.novabill.domain.Invoice).findInvoice(#id).accountingDocumentYear.toString())");
	
	declare @method : public void com.novadart.novabill.service.web.InvoiceServiceImpl.markViewedByClient(..):
		@CacheEvict(value = INVOICE_CACHE, key = "#businessID.toString().concat('-').concat(T(com.novadart.novabill.domain.Invoice).findInvoice(#id).accountingDocumentYear.toString())");
	
	declare @method : public boolean com.novadart.novabill.service.web.InvoiceServiceImpl.email(..):
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
	
	declare @method : public void com.novadart.novabill.service.web.CreditNoteService.update(CreditNoteDTO): @Caching(evict = {
		@CacheEvict(value = CREDITNOTE_CACHE, beforeInvocation = true,
			key = "#creditNoteDTO.business.id.toString().concat('-').concat(new java.text.SimpleDateFormat('yyyy').format(T(com.novadart.novabill.domain.CreditNote).findCreditNote(#creditNoteDTO.id).accountingDocumentDate).toString())"),
		@CacheEvict(value = CREDITNOTE_CACHE, key = "#creditNoteDTO.business.id.toString().concat('-').concat(new java.text.SimpleDateFormat('yyyy').format(#creditNoteDTO.accountingDocumentDate).toString())")
	});
	
	
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
	
	declare @method : public void com.novadart.novabill.service.web.EstimationService.update(EstimationDTO): @Caching(evict = {
		@CacheEvict(value = ESTIMATION_CACHE, beforeInvocation = true,
			key = "#estimationDTO.business.id.toString().concat('-').concat(new java.text.SimpleDateFormat('yyyy').format(T(com.novadart.novabill.domain.Estimation).findEstimation(#estimationDTO.id).accountingDocumentDate).toString())"),
			@CacheEvict(value = ESTIMATION_CACHE, key = "#estimationDTO.business.id.toString().concat('-').concat(new java.text.SimpleDateFormat('yyyy').format(#estimationDTO.accountingDocumentDate).toString())")
	});
	
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
		
	declare @method : public void com.novadart.novabill.service.web.TransportDocumentService.update(TransportDocumentDTO): @Caching(evict = {
		@CacheEvict(value = TRANSPORTDOCUMENT_CACHE, beforeInvocation = true,
			key = "#transportDocDTO.business.id.toString().concat('-').concat(new java.text.SimpleDateFormat('yyyy').format(T(com.novadart.novabill.domain.TransportDocument).findTransportDocument(#transportDocDTO.id).accountingDocumentDate).toString())"),
		@CacheEvict(value = TRANSPORTDOCUMENT_CACHE, key = "#transportDocDTO.business.id.toString().concat('-').concat(new java.text.SimpleDateFormat('yyyy').format(#transportDocDTO.accountingDocumentDate).toString())")
	});
	
	declare @method : public void com.novadart.novabill.service.web.TransportDocumentService.setInvoice(Long, Long, Long):
		@CacheEvict(value = TRANSPORTDOCUMENT_CACHE, key = "#businessID.toString().concat('-').concat(T(com.novadart.novabill.domain.TransportDocument).findTransportDocument(#transportDocID).accountingDocumentYear.toString())");
	
	declare @method : public void com.novadart.novabill.service.web.TransportDocumentService.clearInvoice(Long, Long):
		@CacheEvict(value = TRANSPORTDOCUMENT_CACHE, key = "#businessID.toString().concat('-').concat(T(com.novadart.novabill.domain.TransportDocument).findTransportDocument(#transportDocID).accountingDocumentYear.toString())");
	
	/*
	 * PaymentType caching
	 * Dependencies: None
	 */
	
	declare @method : public void com.novadart.novabill.service.web.PaymentTypeService.remove(Long, Long): @CacheEvict(value = {PAYMENTTYPE_CACHE, CLIENT_CACHE}, key = "#businessID");
	
	declare @method : public Long com.novadart.novabill.service.web.PaymentTypeService.add(PaymentTypeDTO): @CacheEvict(value = PAYMENTTYPE_CACHE, key = "#paymentTypeDTO.business.id");
	
	declare @method : public void com.novadart.novabill.service.web.PaymentTypeService.update(PaymentTypeDTO): @CacheEvict(value = PAYMENTTYPE_CACHE, key = "#paymentTypeDTO.business.id");
	
	/*
	 * Transporter caching
	 * Dependencies: None
	 */
	
	declare @method : public void com.novadart.novabill.service.web.TransporterService.remove(Long, Long): @CacheEvict(value = TRANSPORTER_CACHE, key = "#businessID");
	
	declare @method : public Long com.novadart.novabill.service.web.TransporterService.add(TransporterDTO): @CacheEvict(value = TRANSPORTER_CACHE, key = "#transporterDTO.business.id");
	
	declare @method : public void com.novadart.novabill.service.web.TransporterService.update(TransporterDTO): @CacheEvict(value = TRANSPORTER_CACHE, key = "#transporterDTO.business.id");
	
	/*
	 * Commodity caching
	 * Dependencies: None
	 */
	
	declare @method : public void com.novadart.novabill.service.web.CommodityService.remove(Long, Long): @CacheEvict(value = COMMODITY_CACHE, key = "#businessID");
	
	declare @method : public Long com.novadart.novabill.service.web.CommodityService.add(CommodityDTO): @CacheEvict(value = COMMODITY_CACHE, key = "#commodityDTO.business.id");
	
	declare @method : public void com.novadart.novabill.service.web.CommodityService.update(CommodityDTO): @CacheEvict(value = COMMODITY_CACHE, key = "#commodityDTO.business.id");
	
	
	/*
	 * PriceList caching
	 * Dependencies: Commodity caching
	 */
	
	declare @method : public PriceListDTO com.novadart.novabill.service.web.PriceListService.get(Long): @Cacheable(value = PRICELIST_CACHE, key = "#id");
	
	declare @method : public void com.novadart.novabill.service.web.PriceListService.remove(Long, Long): @Caching(evict = {
		@CacheEvict(value = PRICELIST_CACHE, key = "#businessID.toString().concat('-all')"),
		@CacheEvict(value = PRICELIST_CACHE,  key = "#id")
	});
	
	declare @method : public Long com.novadart.novabill.service.web.PriceListService.add(PriceListDTO): @CacheEvict(value = PRICELIST_CACHE, key = "#priceListDTO.business.id.toString().concat('-all')");
	
	declare @method : public void com.novadart.novabill.service.web.PriceListService.update(PriceListDTO): @Caching(evict = {
		@CacheEvict(value = PRICELIST_CACHE, key = "#priceListDTO.business.id.toString().concat('-all')"),
		@CacheEvict(value = PRICELIST_CACHE,  key = "#priceListDTO.id")
	});
	
	declare @method : public Long com.novadart.novabill.service.web.PriceListService.clonePriceList(Long, Long, String): @CacheEvict(value = PRICELIST_CACHE, key = "#businessID.toString().concat('-all')");
	
	/*
	 * Client address caching
	 * Dependencies: None
	 */
	
	declare @method : public List<ClientAddressDTO> com.novadart.novabill.service.web.ClientService.getClientAddresses(Long): @Cacheable(value = CLIENTADDRESS_CACHE, key = "#clientID");
	
	declare @method : public Long com.novadart.novabill.service.web.ClientService.addClientAddress(ClientAddressDTO): @CacheEvict(value = CLIENTADDRESS_CACHE, key = "#clientAddressDTO.client.id");
	
	declare @method : public void com.novadart.novabill.service.web.ClientService.removeClientAddress(Long, Long): @CacheEvict(value = CLIENTADDRESS_CACHE, key = "#clientID");
	
	declare @method : public void com.novadart.novabill.service.web.ClientService.updateClientAddress(ClientAddressDTO): @CacheEvict(value = CLIENTADDRESS_CACHE, key = "#clientAddressDTO.client.id");
	
	pointcut removeCommodity(): execution(public void com.novadart.novabill.service.web.CommodityService.remove(..));
	pointcut addCommodity(): execution(public Long com.novadart.novabill.service.web.CommodityService.add(..));
	pointcut updateCommodity(): execution(public void com.novadart.novabill.service.web.CommodityService.update(..));
	pointcut removePrice(): execution(public void com.novadart.novabill.service.web.CommodityService.removePrice(..));
	pointcut addOrUpdatePrice(): execution(public Long com.novadart.novabill.service.web.CommodityService.addOrUpdatePrice(..));
	
	after() returning: removeCommodity() || addCommodity() || updateCommodity() || removePrice() || addOrUpdatePrice(){
		Long businessID = utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId();
		for(PriceList priceList: Business.findBusiness(businessID).getPriceLists())
			cacheEvictHooksService.evictPriceList(priceList.getId());
	}
	
	
}
