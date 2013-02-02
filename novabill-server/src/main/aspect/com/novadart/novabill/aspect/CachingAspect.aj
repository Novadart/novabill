package com.novadart.novabill.aspect;

import org.apache.commons.io.FileUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import java.io.File;
import java.io.IOException;
import java.util.List;
import com.novadart.novabill.shared.client.dto.BusinessDTO;
import com.novadart.novabill.shared.client.dto.ClientDTO;
import com.novadart.novabill.shared.client.dto.InvoiceDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.EstimationDTO;
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
	 * Dependencies: Client caching, Invoice caching
	 */
	public static final String BUSINESS_CACHE = "business-cache";
	
	public static final String CLIENT_CACHE = "client-cache";
	
	public static final String INVOICE_CACHE = "invoice-cache";
	
	public static final String CREDITNOTE_CACHE = "creditnote-cache";
	
	public static final String ESTIMATION_CACHE = "estimation-cache";
	
	public static final String TRANSPORTDOCUMENT_CACHE = "transportdocument-cache";
	
	declare @method : public BusinessDTO com.novadart.novabill.web.gwt.BusinessServiceImpl.get(Long): @Cacheable(value = BUSINESS_CACHE, key = "#businessID");
	
	declare @method : public List<ClientDTO> com.novadart.novabill.web.gwt.BusinessServiceImpl.getClients(Long): @Cacheable(value = CLIENT_CACHE, key = "#businessID");
	
	declare @method : public List<InvoiceDTO> com.novadart.novabill.web.gwt.BusinessServiceImpl.getInvoices(Long): @Cacheable(value = INVOICE_CACHE, key = "#businessID");
	
	declare @method : public List<CreditNoteDTO> com.novadart.novabill.web.gwt.BusinessServiceImpl.getCreditNotes(Long): @Cacheable(value = CREDITNOTE_CACHE, key = "#businessID");
	
	declare @method : public List<EstimationDTO> com.novadart.novabill.web.gwt.BusinessServiceImpl.getEstimations(Long): @Cacheable(value = ESTIMATION_CACHE, key = "#businessID");
	
	declare @method : public List<TransportDocumentDTO> com.novadart.novabill.web.gwt.BusinessServiceImpl.getTransportDocuments(Long): @Cacheable(value = TRANSPORTDOCUMENT_CACHE, key = "#businessID");
	
	declare @method : public void com.novadart.novabill.web.gwt.BusinessServiceImpl.update(BusinessDTO): @CacheEvict(value = BUSINESS_CACHE, key = "#businessDTO.id");
	
	
	/*
	 * Client service caching
	 * Dependencies: None
	 */
	
	declare @method : public Long com.novadart.novabill.web.gwt.ClientServiceImpl.add(Long, ClientDTO): @CacheEvict(value = CLIENT_CACHE, key = "#businessID");
	
	declare @method : public void com.novadart.novabill.web.gwt.ClientServiceImpl.update(Long, ClientDTO): @CacheEvict(value = CLIENT_CACHE, key = "#businessID");
	
	declare @method : public void com.novadart.novabill.web.gwt.ClientServiceImpl.remove(Long, Long): @CacheEvict(value = CLIENT_CACHE, key = "#businessID");
	
	/*
	 * Invoice service caching
	 * Dependencies: None
	 */
	
	declare @method : public void com.novadart.novabill.web.gwt.InvoiceServiceImpl.remove(Long, Long, Long): @CacheEvict(value = INVOICE_CACHE, key = "#businessID");
	
	declare @method : public Long com.novadart.novabill.web.gwt.InvoiceServiceImpl.add(InvoiceDTO): @CacheEvict(value = INVOICE_CACHE, key = "#invoiceDTO.business.id");
	
	declare @method : public void com.novadart.novabill.web.gwt.InvoiceServiceImpl.update(InvoiceDTO): @CacheEvict(value = INVOICE_CACHE, key = "#invoiceDTO.business.id");
	
	declare @method : public void com.novadart.novabill.web.gwt.InvoiceServiceImpl.setPayed(Long, ..): @CacheEvict(value = INVOICE_CACHE, key = "#businessID");
	
	/*
	 * CreditNote caching
	 * Dependencies: None
	 */
	
	declare @method : public void com.novadart.novabill.web.gwt.CreditNoteServiceImpl.remove(Long, Long, Long): @CacheEvict(value = CREDITNOTE_CACHE, key = "#businessID");
	
	declare @method : public Long com.novadart.novabill.web.gwt.CreditNoteServiceImpl.add(CreditNoteDTO): @CacheEvict(value = CREDITNOTE_CACHE, key = "#creditNoteDTO.business.id");
	
	declare @method : public void com.novadart.novabill.web.gwt.CreditNoteServiceImpl.update(CreditNoteDTO): @CacheEvict(value = CREDITNOTE_CACHE, key = "#creditNoteDTO.business.id");
	
	
	/*
	 * Estimation caching
	 * Dependencies: None
	 */
	
	declare @method : public void com.novadart.novabill.web.gwt.EstimationServiceImpl.remove(Long, Long, Long): @CacheEvict(value = ESTIMATION_CACHE, key = "#businessID");
	
	declare @method : public Long com.novadart.novabill.web.gwt.EstimationServiceImpl.add(EstimationDTO): @CacheEvict(value = ESTIMATION_CACHE, key = "#estimationDTO.business.id");
	
	declare @method : public void com.novadart.novabill.web.gwt.EstimationServiceImpl.update(EstimationDTO): @CacheEvict(value = ESTIMATION_CACHE, key = "#estimationDTO.business.id");
	
	/*
	 * TransportDocument caching
	 * Dependencies: None
	 */
	
	declare @method : public void com.novadart.novabill.web.gwt.TransportDocumentServiceImpl.remove(Long, Long, Long): @CacheEvict(value = TRANSPORTDOCUMENT_CACHE, key = "#businessID");
	
	declare @method : public Long com.novadart.novabill.web.gwt.TransportDocumentServiceImpl.add(TransportDocumentDTO): @CacheEvict(value = TRANSPORTDOCUMENT_CACHE, key = "#transportDocDTO.business.id");
	
	declare @method : public void com.novadart.novabill.web.gwt.TransportDocumentServiceImpl.update(TransportDocumentDTO): @CacheEvict(value = TRANSPORTDOCUMENT_CACHE, key = "#transportDocDTO.business.id");
	
	
}
