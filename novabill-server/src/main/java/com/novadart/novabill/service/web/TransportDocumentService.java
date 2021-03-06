package com.novadart.novabill.service.web;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.novadart.novabill.annotation.Restrictions;
import com.novadart.novabill.authorization.TrialOrPremiumChecker;
import com.novadart.novabill.report.DocumentType;
import com.novadart.novabill.service.PDFStorageService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.novadart.novabill.domain.AccountingDocumentItem;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.Invoice;
import com.novadart.novabill.domain.TransportDocument;
import com.novadart.novabill.domain.dto.DTOUtils;
import com.novadart.novabill.domain.dto.DTOUtils.Predicate;
import com.novadart.novabill.domain.dto.transformer.AccountingDocumentItemDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.TransportDocumentDTOTransformer;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.AccountingDocumentValidator;
import com.novadart.novabill.service.validator.Groups.HeavyClient;
import com.novadart.novabill.service.validator.SimpleValidator;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.dto.TransportDocumentDTO;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.DataIntegrityException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@Service
public class TransportDocumentService {


	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private AccountingDocumentValidator validator;
	
	@Autowired
	private SimpleValidator simpleValidator;

	@Autowired
	private PDFStorageService pdfStorageService;

	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("T(com.novadart.novabill.domain.TransportDocument).findTransportDocument(#id)?.business?.id == principal.business.id")
	public TransportDocumentDTO get(Long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		TransportDocument transDoc = TransportDocument.findTransportDocument(id);
		if(transDoc == null)
			throw new NoSuchObjectException();
		return TransportDocumentDTOTransformer.toDTO(transDoc, true);
	}
	
	private static class EqualsClientIDPredicate implements Predicate<TransportDocumentDTO>{
		
		private Long id;
		
		public EqualsClientIDPredicate(Long id) {
			this.id = id;
		}
		
		@Override
		public boolean isTrue(TransportDocumentDTO doc) {
			return doc.getClient().getId().equals(id);
		}
		
	}

	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#clientID)?.business?.id == principal.business.id")
	public List<TransportDocumentDTO> getAllForClient(Long clientID, Integer year) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		return new ArrayList<>(DTOUtils.filter(businessService.getTransportDocuments(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(), year), new EqualsClientIDPredicate(clientID)));
	}

	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#transportDocDTO?.business?.id == principal.business.id and " +
		  	  	  "T(com.novadart.novabill.domain.Client).findClient(#transportDocDTO?.client?.id)?.business?.id == principal.business.id and " +
		  	  	  "#transportDocDTO != null and #transportDocDTO.id == null")
	public Long add(TransportDocumentDTO transportDocDTO) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException, ValidationException {
		TransportDocument transportDoc = new TransportDocument();
		TransportDocumentDTOTransformer.copyFromDTO(transportDoc, transportDocDTO, true);
		validator.validate(TransportDocument.class, transportDoc);
		Client client = Client.findClient(transportDocDTO.getClient().getId());
		simpleValidator.validate(client, HeavyClient.class);
		transportDoc.setClient(client);
		client.getTransportDocuments().add(transportDoc);
		Business business = Business.findBusiness(transportDocDTO.getBusiness().getId());
		transportDoc.setBusiness(business);
		business.getTransportDocuments().add(transportDoc);
		String docPath = pdfStorageService.generateAndStorePdfForAccountingDocument(transportDoc, DocumentType.TRANSPORT_DOCUMENT);
		transportDoc.setDocumentPath(docPath);
		transportDoc.persist();
		transportDoc.flush();
		return transportDoc.getId();
	}

	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@Transactional(readOnly = false)
	@PreAuthorize("#businessID == principal.business.id and " +
		  	  	 "T(com.novadart.novabill.domain.TransportDocument).findTransportDocument(#id)?.business?.id == #businessID and " +
		  	  	 "T(com.novadart.novabill.domain.TransportDocument).findTransportDocument(#id)?.client?.id == #clientID")
	public void remove(Long businessID, Long clientID, Long id) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException, FreeUserAccessForbiddenException {
		TransportDocument transportDoc = TransportDocument.findTransportDocument(id);
		transportDoc.remove();
		if(Hibernate.isInitialized(transportDoc.getBusiness().getTransportDocuments()))
			transportDoc.getBusiness().getTransportDocuments().remove(transportDoc);
		if(Hibernate.isInitialized(transportDoc.getClient().getTransportDocuments()))
			transportDoc.getClient().getTransportDocuments().remove(transportDoc);
		
	}

	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("#transportDocDTO?.business?.id == principal.business.id and " +
	  	  	  	  "T(com.novadart.novabill.domain.Client).findClient(#transportDocDTO?.client?.id)?.business?.id == principal.business.id and " +
	  	  	  	  "#transportDocDTO?.id != null")
	public void update(TransportDocumentDTO transportDocDTO) throws DataAccessException, NotAuthenticatedException, NoSuchObjectException,
			ValidationException, DataIntegrityException, FreeUserAccessForbiddenException {
		TransportDocument persistedTransportDoc = TransportDocument.findTransportDocument(transportDocDTO.getId());
		if(persistedTransportDoc == null)
			throw new NoSuchObjectException();
		if(persistedTransportDoc.getInvoice() != null)
			throw new DataIntegrityException();
		TransportDocumentDTOTransformer.copyFromDTO(persistedTransportDoc, transportDocDTO, false);
		persistedTransportDoc.getAccountingDocumentItems().clear();
		for(AccountingDocumentItemDTO itemDTO: transportDocDTO.getItems()){
			AccountingDocumentItem item = new AccountingDocumentItem();
			AccountingDocumentItemDTOTransformer.copyFromDTO(item, itemDTO);
			item.setAccountingDocument(persistedTransportDoc);
			persistedTransportDoc.getAccountingDocumentItems().add(item);
		}
		validator.validate(TransportDocument.class, persistedTransportDoc);
		String docPath = pdfStorageService.generateAndStorePdfForAccountingDocument(persistedTransportDoc, DocumentType.TRANSPORT_DOCUMENT);
		persistedTransportDoc.setDocumentPath(docPath);
	}

	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	public Long getNextTransportDocId() throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		return utilsService.getAuthenticatedPrincipalDetails().getBusiness().getNextTransportDocDocumentID();
	}

	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#clientID)?.business?.id == principal.business.id")
	public PageDTO<TransportDocumentDTO> getAllForClientInRange(Long clientID, Integer year, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		List<TransportDocumentDTO> allTransportDocs = getAllForClient(clientID, year);
		return new PageDTO<>(DTOUtils.range(allTransportDocs, start, length), start, length, new Long(allTransportDocs.size()));
	}

	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#businessID == principal.business.id")
	public PageDTO<TransportDocumentDTO> getAllInRange(Long businessID, Integer year, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		List<TransportDocumentDTO> allTransportDocs = businessService.getTransportDocuments(businessID, year);
		return new PageDTO<>(DTOUtils.range(allTransportDocs, start, length), start, length, new Long(allTransportDocs.size()));
	}

	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	public List<TransportDocumentDTO>  getAllWithIDs(List<Long> ids) throws DataAccessException, NoSuchObjectException, NotAuthenticatedException, FreeUserAccessForbiddenException{
		Long businessID = utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId();
		Set<Long> idsSet = new HashSet<>(ids);
		List<TransportDocument> transportDocs = TransportDocument.getTransportDocumentsWithIDs(ids);
		for(TransportDocument transDoc: transportDocs){
			if(!idsSet.contains(transDoc.getId())) throw new IllegalStateException();
			if(!transDoc.getBusiness().getId().equals(businessID)) throw new DataAccessException(); //authorization
		}
		if(transportDocs.size() < ids.size()) throw new NoSuchObjectException(); //cardinality check
		return DTOUtils.toDTOList(transportDocs, DTOUtils.transportDocDTOConverter, true);
	}

	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@Transactional(readOnly = false)
	@PreAuthorize("#businessID == principal.business.id and " +
				  "T(com.novadart.novabill.domain.Invoice).findInvoice(#invoiceID)?.business?.id == #businessID and " +
				  "T(com.novadart.novabill.domain.TransportDocument).findTransportDocument(#transportDocID)?.business?.id == #businessID")
	public void setInvoice(Long businessID, Long invoiceID, Long transportDocID) throws DataAccessException, NotAuthenticatedException, DataIntegrityException, FreeUserAccessForbiddenException {
		TransportDocument transDoc = TransportDocument.findTransportDocument(transportDocID);
		if(transDoc.getInvoice() != null)
			throw new DataIntegrityException();
		Invoice invoice = Invoice.findInvoice(invoiceID);
		transDoc.setInvoice(invoice);
		if(Hibernate.isInitialized(invoice.getTransportDocuments()))
			invoice.getTransportDocuments().add(transDoc);
	}

	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@Transactional(readOnly = false)
	@PreAuthorize("#businessID == principal.business.id and " +
				  "T(com.novadart.novabill.domain.TransportDocument).findTransportDocument(#transportDocID)?.business?.id == #businessID")
	public void clearInvoice(Long businessID, Long transportDocID) throws DataAccessException, NotAuthenticatedException, DataIntegrityException, FreeUserAccessForbiddenException {
		TransportDocument transDoc = TransportDocument.findTransportDocument(transportDocID);
		Invoice invoice = transDoc.getInvoice();
		if(invoice == null)
			throw new DataIntegrityException();
		transDoc.setInvoice(null);
		invoice.getTransportDocuments().remove(transDoc);
	}
	
}
