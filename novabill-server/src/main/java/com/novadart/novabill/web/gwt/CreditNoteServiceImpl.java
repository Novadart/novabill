package com.novadart.novabill.web.gwt;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import com.novadart.novabill.domain.AccountingDocumentItem;
import com.novadart.novabill.domain.Business;
import com.novadart.novabill.domain.Client;
import com.novadart.novabill.domain.CreditNote;
import com.novadart.novabill.domain.dto.DTOUtils;
import com.novadart.novabill.domain.dto.DTOUtils.Predicate;
import com.novadart.novabill.domain.dto.factory.AccountingDocumentItemDTOFactory;
import com.novadart.novabill.domain.dto.factory.CreditNoteDTOFactory;
import com.novadart.novabill.service.UtilsService;
import com.novadart.novabill.service.validator.InvoiceValidator;
import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;
import com.novadart.novabill.shared.client.dto.CreditNoteDTO;
import com.novadart.novabill.shared.client.dto.PageDTO;
import com.novadart.novabill.shared.client.exception.AuthorizationException;
import com.novadart.novabill.shared.client.exception.ConcurrentAccessException;
import com.novadart.novabill.shared.client.exception.DataAccessException;
import com.novadart.novabill.shared.client.exception.NoSuchObjectException;
import com.novadart.novabill.shared.client.exception.NotAuthenticatedException;
import com.novadart.novabill.shared.client.exception.ValidationException;
import com.novadart.novabill.shared.client.facade.BusinessService;
import com.novadart.novabill.shared.client.facade.CreditNoteService;

public class CreditNoteServiceImpl implements CreditNoteService {
	
	@Autowired
	private UtilsService utilsService;
	
	@Autowired
	private BusinessService businessService;
	
	@Autowired
	private InvoiceValidator validator;

	@Override
	@PreAuthorize("T(com.novadart.novabill.domain.CreditNote).findCreditNote(#id)?.business?.id == principal.business.id")
	public CreditNoteDTO get(Long id) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException {
		return DTOUtils.findDocumentInCollection(businessService.getCreditNotes(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId()), id);
	}

	@Override
	@PreAuthorize("#businessID == principal.business.id")
	public PageDTO<CreditNoteDTO> getAllInRange(Long businessID, Integer start, Integer length) throws NotAuthenticatedException, ConcurrentAccessException {
		List<CreditNoteDTO> allCreditNotes = businessService.getCreditNotes(businessID);
		return new PageDTO<CreditNoteDTO>(DTOUtils.range(allCreditNotes, start, length), start, length, new Long(allCreditNotes.size()));
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
	
	@Override
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#clientID)?.business?.id == principal.business.id")
	public List<CreditNoteDTO> getAllForClient(Long clientID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException {
		return new ArrayList<CreditNoteDTO>(DTOUtils.filter(businessService.getCreditNotes(utilsService.getAuthenticatedPrincipalDetails().getBusiness().getId()), new EqualsClientIDPredicate(clientID)));
	}
	
	@Override
	//@Restrictions(checkers = {NumberOfCreditNotesPerYearQuotaReachedChecker.class})
	@PreAuthorize("#creditNoteDTO?.business?.id == principal.business.id and " +
				  "T(com.novadart.novabill.domain.Client).findClient(#creditNoteDTO?.client?.id)?.business?.id == principal.business.id and " +
				  "#creditNoteDTO != null and #creditNoteDTO.id == null")
	public Long add(CreditNoteDTO creditNoteDTO) throws NotAuthenticatedException, DataAccessException, ValidationException, ConcurrentAccessException, AuthorizationException {
		CreditNote creditNote = new CreditNote();//create new credit note
		CreditNoteDTOFactory.copyFromDTO(creditNote, creditNoteDTO, true);
		validator.validate(creditNote);
		Client client = Client.findClient(creditNoteDTO.getClient().getId());
		Business business = Business.findBusiness(creditNoteDTO.getBusiness().getId());
		creditNote.setClient(client);
		client.getCreditNotes().add(creditNote);
		creditNote.setBusiness(business);
		business.getCreditNotes().add(creditNote);
		creditNote.flush();
		return creditNote.getId();
	}
	
	@Override
	@Transactional(readOnly = false)
	@PreAuthorize("#businessID == principal.business.id and " +
				  "T(com.novadart.novabill.domain.CreditNote).findCreditNote(#creditNoteID)?.business?.id == #businessID and " +
				  "T(com.novadart.novabill.domain.CreditNote).findCreditNote(#creditNoteID)?.client?.id == #clientID")
	public void remove(Long businessID, Long clientID, Long creditNoteID) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ConcurrentAccessException {
		CreditNote creditNote = CreditNote.findCreditNote(creditNoteID);
		creditNote.remove(); //removing credit note
		if(Hibernate.isInitialized(creditNote.getBusiness().getCreditNotes()))
			creditNote.getBusiness().getCreditNotes().remove(creditNote);
		if(Hibernate.isInitialized(creditNote.getClient().getCreditNotes()))
			creditNote.getClient().getCreditNotes().remove(creditNote);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = {ValidationException.class})
	@PreAuthorize("#creditNoteDTO?.business?.id == principal.business.id and " +
			  	  "T(com.novadart.novabill.domain.Client).findClient(#creditNoteDTO?.client?.id)?.business?.id == principal.business.id and " +
			  	  "#creditNoteDTO?.id != null")
	public void update(CreditNoteDTO creditNoteDTO) throws NotAuthenticatedException, DataAccessException, NoSuchObjectException, ValidationException, ConcurrentAccessException {
		CreditNote persistedCreditNote = CreditNote.findCreditNote(creditNoteDTO.getId());
		if(persistedCreditNote == null)
			throw new NoSuchObjectException();
		CreditNoteDTOFactory.copyFromDTO(persistedCreditNote, creditNoteDTO, false);
		persistedCreditNote.getAccountingDocumentItems().clear();
		for(AccountingDocumentItemDTO itemDTO: creditNoteDTO.getItems()){
			AccountingDocumentItem item = new AccountingDocumentItem();
			AccountingDocumentItemDTOFactory.copyFromDTO(item, itemDTO);
			item.setAccountingDocument(persistedCreditNote);
			persistedCreditNote.getAccountingDocumentItems().add(item);
		}
		validator.validate(persistedCreditNote);
		
	}

	@Override
	public Long getNextCreditNoteDocumentID() throws NotAuthenticatedException, ConcurrentAccessException {
		return utilsService.getAuthenticatedPrincipalDetails().getBusiness().getNextCreditNoteDocumentID();
	}

	@Override
	@PreAuthorize("T(com.novadart.novabill.domain.Client).findClient(#clientID)?.business?.id == principal.business.id")
	public PageDTO<CreditNoteDTO> getAllForClientInRange(Long clientID, int start, int length) throws NotAuthenticatedException, DataAccessException,	NoSuchObjectException, ConcurrentAccessException {
		List<CreditNoteDTO> allCreditNotes = getAllForClient(clientID);
		return new PageDTO<CreditNoteDTO>(DTOUtils.range(allCreditNotes, start, length), start, length, new Long(allCreditNotes.size()));
	}

}
