package com.novadart.novabill.service.web;

import java.util.ArrayList;
import java.util.List;

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
import com.novadart.novabill.domain.CreditNote;
import com.novadart.novabill.domain.dto.DTOUtils;
import com.novadart.novabill.domain.dto.DTOUtils.Predicate;
import com.novadart.novabill.domain.dto.transformer.AccountingDocumentItemDTOTransformer;
import com.novadart.novabill.domain.dto.transformer.CreditNoteDTOTransformer;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.AccountingDocumentValidator;
import com.novadart.novabill.service.validator.Groups.HeavyClient;
import com.novadart.novabill.service.validator.SimpleValidator;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.FreeUserAccessForbiddenException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;

@Service
public class CreditNoteService {
	
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
	@PreAuthorize("T(com.novadart.novabill.domain.CreditNote).findCreditNote(#id)?.business?.id == principal.business.id")
	public CreditNoteDTO get(Long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		CreditNote creditNote = CreditNote.findCreditNote(id);
		if(creditNote == null)
			throw new NoSuchObjectException();
		return CreditNoteDTOTransformer.toDTO(creditNote, true);
	}

	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#businessID == principal.business.id")
	public PageDTO<CreditNoteDTO> getAllInRange(Long businessID, Integer year, Integer start, Integer length) throws NotAuthenticatedException, DataAccessException, FreeUserAccessForbiddenException {
		List<CreditNoteDTO> allCreditNotes = businessService.getCreditNotes(businessID, year);
		return new PageDTO<>(DTOUtils.range(allCreditNotes, start, length), start, length, new Long(allCreditNotes.size()));
	}
	
	private static class EqualsClientIDPredicate implements Predicate<CreditNoteDTO>{
		
		private Long id;
		
		public EqualsClientIDPredicate(Long id) {
			this.id = id;
		}
		
		@Override
		public boolean isTrue(CreditNoteDTO doc) {
			return doc.getClient().getId().equals(id);
		}
		
	}

	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#clientID)?.business?.id == principal.business.id")
	public List<CreditNoteDTO> getAllForClient(Long clientID, Integer year) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		return new ArrayList<>(DTOUtils.filter(businessService.getCreditNotes(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId(), year), new EqualsClientIDPredicate(clientID)));
	}

	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("#creditNoteDTO?.business?.id == principal.business.id and " +
				  "T(com.novadart.novabill.domain.Client).findClient(#creditNoteDTO?.client?.id)?.business?.id == principal.business.id and " +
				  "#creditNoteDTO != null and #creditNoteDTO.id == null")
	public Long add(CreditNoteDTO creditNoteDTO) throws NotAuthenticatedException, DataAccessException, ValidationException, FreeUserAccessForbiddenException {
		CreditNote creditNote = new CreditNote();//create new credit note
		CreditNoteDTOTransformer.copyFromDTO(creditNote, creditNoteDTO, true);
		validator.validate(CreditNote.class, creditNote);
		Client client = Client.findClient(creditNoteDTO.getClient().getId());
		simpleValidator.validate(client, HeavyClient.class);
		Business business = Business.findBusiness(creditNoteDTO.getBusiness().getId());
		creditNote.setClient(client);
		client.getCreditNotes().add(creditNote);
		creditNote.setBusiness(business);
		business.getCreditNotes().add(creditNote);
		String docPath = pdfStorageService.generateAndStorePdfForAccountingDocument(creditNote, DocumentType.CREDIT_NOTE);
		creditNote.setDocumentPath(docPath);
		creditNote.persist();
		creditNote.flush();
		return creditNote.getId();
	}

	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@Transactional(readOnly = false)
	@PreAuthorize("#businessID == principal.business.id and " +
				  "T(com.novadart.novabill.domain.CreditNote).findCreditNote(#id)?.business?.id == #businessID and " +
				  "T(com.novadart.novabill.domain.CreditNote).findCreditNote(#id)?.client?.id == #clientID")
	public void remove(Long businessID, Long clientID, Long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		CreditNote creditNote = CreditNote.findCreditNote(id);
		creditNote.remove(); //removing credit note
		if(Hibernate.isInitialized(creditNote.getBusiness().getCreditNotes()))
			creditNote.getBusiness().getCreditNotes().remove(creditNote);
		if(Hibernate.isInitialized(creditNote.getClient().getCreditNotes()))
			creditNote.getClient().getCreditNotes().remove(creditNote);
	}

	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("#creditNoteDTO?.business?.id == principal.business.id and " +
			  	  "T(com.novadart.novabill.domain.Client).findClient(#creditNoteDTO?.client?.id)?.business?.id == principal.business.id and " +
			  	  "#creditNoteDTO?.id != null")
	public void update(CreditNoteDTO creditNoteDTO) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, FreeUserAccessForbiddenException {
		CreditNote persistedCreditNote = CreditNote.findCreditNote(creditNoteDTO.getId());
		if(persistedCreditNote == null)
			throw new NoSuchObjectException();
		CreditNoteDTOTransformer.copyFromDTO(persistedCreditNote, creditNoteDTO, false);
		persistedCreditNote.getAccountingDocumentItems().clear();
		for(AccountingDocumentItemDTO itemDTO: creditNoteDTO.getItems()){
			AccountingDocumentItem item = new AccountingDocumentItem();
			AccountingDocumentItemDTOTransformer.copyFromDTO(item, itemDTO);
			item.setAccountingDocument(persistedCreditNote);
			persistedCreditNote.getAccountingDocumentItems().add(item);
		}
		validator.validate(CreditNote.class, persistedCreditNote);
		String docPath = pdfStorageService.generateAndStorePdfForAccountingDocument(persistedCreditNote, DocumentType.CREDIT_NOTE);
		persistedCreditNote.setDocumentPath(docPath);
	}

	public Long getNextCreditNoteDocumentID() throws NotAuthenticatedException {
		return utilsService.getAuthenticatedPrincipalDetails().getBusiness().getNextCreditNoteDocumentID();
	}

	@Restrictions(checkers = {TrialOrPremiumChecker.class})
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#clientID)?.business?.id == principal.business.id")
	public PageDTO<CreditNoteDTO> getAllForClientInRange(Long clientID, Integer year, int start, int length) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, FreeUserAccessForbiddenException {
		List<CreditNoteDTO> allCreditNotes = getAllForClient(clientID, year);
		return new PageDTO<>(DTOUtils.range(allCreditNotes, start, length), start, length, new Long(allCreditNotes.size()));
	}

}
